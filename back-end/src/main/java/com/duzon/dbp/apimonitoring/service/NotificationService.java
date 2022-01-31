package com.duzon.dbp.apimonitoring.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.duzon.dbp.apimonitoring.advice.exception.NotificationNotFoundWithNumberError;
import com.duzon.dbp.apimonitoring.config.socket.WebSockChatHandler;
import com.duzon.dbp.apimonitoring.dto.ApiDelayDto;
import com.duzon.dbp.apimonitoring.dto.ApiDto;
import com.duzon.dbp.apimonitoring.dto.ApiErrDto;
import com.duzon.dbp.apimonitoring.dto.Employee;
import com.duzon.dbp.apimonitoring.dto.socket.NotificationDto;
import com.duzon.dbp.apimonitoring.repo.EmployeeRepo;
import com.duzon.dbp.apimonitoring.repo.NotificationRepo;
import com.duzon.dbp.apimonitoring.service.socket.SocketService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * NotificationService
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final SocketService chatService;
    
    @Autowired
    NotificationRepo notificationRepo;

    @Autowired
    EmployeeRepo employeeRepo;
    
    @Autowired
    WebSockChatHandler webSockChatHandler;

    public enum SendType{
        API("api"),
        API_UPDATE("apiupdate"),
        ERROR("err"),
        DELAY("delay");
   
        private String type;
       
        private SendType(String type) {
            this.type = type;
        }
        
        public String getType(){
            return type;
        }
    }

    // 초기에 로그인 시 is_confirm = 'F' 개수
    public int NotificationListCount(String id) {
        long employee_no = notificationRepo.findEmID(id);
        int count = notificationRepo.NotificationGetListCount(employee_no);
        return count;
    }
    
    // 회원 번호에 해당하는 리스트 그리고 is_confirm = 'T'로 업데이트
    public List<Map<String, Object>> NotificationGetList(long employee_no) {
        List<Map<String, Object>> list = notificationRepo.NotificationGetList(employee_no);

        List<NotificationDto> listDto = notificationRepo.findList(employee_no);
        for (int i = 0; i < listDto.size(); i++) {
            listDto.get(i).setIs_confirm("T");
        }
        notificationRepo.saveAll(listDto);
        
        return list;
    }
    
    // noti 단건 그리고 ( is_confirm, is_read = 'T')로 업데이트
    public NotificationDto NotificationGet(long notify_no) {
        NotificationDto dto = notificationRepo.NotificationGet(notify_no).orElseThrow(NotificationNotFoundWithNumberError::new);
        if (dto.getIs_confirm().equals("F") || dto.getIs_read().equals("F")) {
            NotificationDto noti = notificationRepo.NotificationGet(notify_no).orElseThrow(NotificationNotFoundWithNumberError::new);
            noti.setIs_confirm("T");
            noti.setIs_read("T");
            notificationRepo.save(noti);
        }
        return dto;
    }
    
    // noti 삭제
    public void NotificationDelete(long notify_no) {
        notificationRepo.findById(notify_no).orElseThrow(NotificationNotFoundWithNumberError::new);
        notificationRepo.deleteById(notify_no);
    }

    // Api Noti
	public void ApiNoti(ApiDto dto) {
        String type = SendType.API.getType();
        List<NotificationDto> list = CreateApiNoti(dto.getApi_no(), type);
        notificationRepo.saveAll(list);
        
        String id = notificationRepo.findApiid(dto.getEmployee_no());
        int count = notificationRepo.countReadF(id);
        String sub_id = notificationRepo.findApiid(dto.getEmployee_sub_no());
        int sub_count = notificationRepo.countReadF(sub_id);
        
        String url = notificationRepo.findurl(dto.getApi_no());
        
        sendmsg(id, count, sub_id, sub_count, url, type);
    }
    
    // Api Update Noti
	public void ApiUpdateNoti(long em_no, Long api_no) {
        String type = SendType.API_UPDATE.getType();
        List<NotificationDto> list = CreateApiNoti(api_no, type);
        notificationRepo.saveAll(list);
        
        String id = notificationRepo.findApiid(em_no);
        int count = notificationRepo.countReadF(id);
        
        String url = notificationRepo.findurl(api_no);

        sendmsg(id, count, url, type);
	}

    // Err Noti
    public void ErrNoti(ApiErrDto dto) {
        String type = SendType.ERROR.getType();
        List<NotificationDto> list = CreateErrOrDelayNoti(dto.getApi_err_no(), dto.getApi_no(), type);
        notificationRepo.saveAll(list);
        
        String id = notificationRepo.findErrid(dto.getApi_err_no());
        int count = notificationRepo.countReadF(id);
        String sub_id = notificationRepo.findErrSubid(dto.getApi_err_no());
        int sub_count = notificationRepo.countReadF(sub_id);
        
        String url = notificationRepo.findurl(dto.getApi_no());

        sendmsg(id, count, sub_id, sub_count, url, type);
        adminSendmsg(id, sub_id, url, dto.getApi_no(), dto.getApi_err_no(), type);
    }

    // Delay Noti
    public void DelayNoti(ApiDelayDto dto) {
        String type = SendType.DELAY.getType();
        List<NotificationDto> list = CreateErrOrDelayNoti(dto.getApi_delay_no(), dto.getApi_no(), type);
        notificationRepo.saveAll(list);
        
        String id = notificationRepo.findDelayid(dto.getApi_delay_no());
        int count = notificationRepo.countReadF(id);
        String sub_id = notificationRepo.findDelaySubid(dto.getApi_delay_no());
        int sub_count = notificationRepo.countReadF(sub_id);
        
        String url = notificationRepo.findurl(dto.getApi_no());
        
        sendmsg(id, count, sub_id, sub_count, url, type);
        adminSendmsg(id, sub_id, url, dto.getApi_no(), dto.getApi_delay_no(), type);
    }

    // Api Noti 생성
    public List<NotificationDto> CreateApiNoti(long api_no, String type) {
        long employee_no = notificationRepo.findEmployee_no(api_no);
        long employee_sub_no = notificationRepo.findEmployee_Sub_no(api_no);
        long api_category_no = notificationRepo.findApi_category_no(api_no);
        long service_no = notificationRepo.findService_no(api_category_no);

        List<NotificationDto> list = new ArrayList<NotificationDto>();

        NotificationDto main_noti = new NotificationDto();
        main_noti.setApi_no(api_no);
        main_noti.setApi_category_no(api_category_no);
        main_noti.setService_no(service_no);
        main_noti.setEmployee_no(employee_no);
        main_noti.setType(type);
        list.add(main_noti);

        if (employee_no != employee_sub_no) {
            NotificationDto sub_noti = new NotificationDto();
            sub_noti.setApi_no(api_no);
            sub_noti.setApi_category_no(api_category_no);
            sub_noti.setService_no(service_no);
            sub_noti.setEmployee_no(employee_sub_no);
            sub_noti.setType(type);
            list.add(sub_noti);
        }

        return list;
    }

    // Err OR Delay Noti 생성
    public List<NotificationDto> CreateErrOrDelayNoti(long err_delay_no, long api_no, String type){
        long employee_no = notificationRepo.findEmployee_no(api_no);
        long employee_sub_no = notificationRepo.findEmployee_Sub_no(api_no);
        long api_category_no = notificationRepo.findApi_category_no(api_no);
        long service_no = notificationRepo.findService_no(api_category_no);

        List<NotificationDto> list = new ArrayList<NotificationDto>();

        NotificationDto main_noti = new NotificationDto();
        main_noti.setApi_no(api_no);
        main_noti.setApi_category_no(api_category_no);
        main_noti.setService_no(service_no);
        main_noti.setEmployee_no(employee_no);
        if (type.equals("err")) {
            main_noti.setErr_no(err_delay_no);
        }
        if (type.equals("delay")) {
            main_noti.setDelay_no(err_delay_no);
        }
        main_noti.setType(type);
        list.add(main_noti);

        if (employee_no != employee_sub_no) {
            NotificationDto sub_noti = new NotificationDto();
            sub_noti.setApi_no(api_no);
            sub_noti.setApi_category_no(api_category_no);
            sub_noti.setService_no(service_no);
            sub_noti.setEmployee_no(employee_sub_no);
            if (type.equals("err")) {
                sub_noti.setErr_no(err_delay_no);
            }
            if (type.equals("delay")) {
                sub_noti.setDelay_no(err_delay_no);
            }
            sub_noti.setType(type);
            list.add(sub_noti);
        }

        return list;
    }

    // message 보내기
    private void sendmsg(String id, int count, String sub_id, int sub_count, String url, String type) {
        log.info("    # # # # # # # # # # # # # # # # # # # # # #");
        log.info("# # # # # # # # # # SendMessage # # # # # # # # # #");
        log.info("# # # # # # # # # # id : " + id);
        log.info("# # # # # # # # # # sub_id : " + sub_id);
        log.info("# # # # # # # # # # # # # # # # # # # # # # # # # #");
        log.info("    # # # # # # # # # # # # # # # # # # # # # #");

        if (chatService.findRoomById(id) != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("count", count);
            map.put("url", url);
            map.put("type", type);
            Map<String, Object> message = new HashMap<>();
            message.put("message", map);

            chatService.findRoomById(id).sendMessage(message, chatService);
        }

        if (!id.equals(sub_id)) {
            if (chatService.findRoomById(sub_id) != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("count", sub_count);
                map.put("url", url);
                map.put("type", type);
                Map<String, Object> message = new HashMap<>();
                message.put("message", map);

                chatService.findRoomById(sub_id).sendMessage(message, chatService);
            }
        }
    }

    // Api Update message 보내기
    private void sendmsg(String id, int count, String url, String type) {
        log.info("    # # # # # # # # # # # # # # # # # # # # # #");
        log.info("# # # # # # # # # # SendMessage # # # # # # # # # #");
        log.info("# # # # # # # # # # id : " + id);
        log.info("# # # # # # # # # # # # # # # # # # # # # # # # # #");
        log.info("    # # # # # # # # # # # # # # # # # # # # # #");
        
        if (chatService.findRoomById(id) != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("count", count);
            map.put("url", url);
            map.put("type", type);
            Map<String, Object> message = new HashMap<>();
            message.put("message", map);

            chatService.findRoomById(id).sendMessage(message, chatService);
        }
    }

    // 관리자들에게 알림 보내기
    private void adminSendmsg(String id, String sub_id, String url, long api_no, long err_delay_no, String type){
        long api_category_no = notificationRepo.findApi_category_no(api_no);
        long service_no = notificationRepo.findService_no(api_category_no);

        String employee_name = employeeRepo.findName(id);
        String employee_sub_name = employeeRepo.findName(sub_id);

        List<Employee> list = employeeRepo.findAdminId();
        list.forEach(admin -> {
            NotificationDto admin_noti = new NotificationDto();
            admin_noti.setApi_no(api_no);
            admin_noti.setApi_category_no(api_category_no);
            admin_noti.setService_no(service_no);
            admin_noti.setEmployee_no(admin.getEmployee_no());
            if (type.equals("err")) {
                admin_noti.setErr_no(err_delay_no);
            }

            if (type.equals("delay")) {
                admin_noti.setDelay_no(err_delay_no);
            }
            admin_noti.setType(type);
            notificationRepo.save(admin_noti);
            
            if (chatService.findRoomById(admin.getId()) != null) {
                int adminCount = notificationRepo.countReadF(admin.getId());

                Map<String, Object> adminMap = new HashMap<>();
                adminMap.put("count", adminCount);
                adminMap.put("url", url);
                adminMap.put("type", type);
                adminMap.put("employee_name", employee_name);
                adminMap.put("employee_sub_name", employee_sub_name);
                Map<String, Object> adminMessage = new HashMap<>();
                adminMessage.put("message", adminMap);

                chatService.findRoomById(admin.getId()).sendMessage(adminMessage, chatService);
            }
        });
    }
}