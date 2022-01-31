package com.duzon.dbp.apimonitoring.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.duzon.dbp.apimonitoring.advice.exception.ApiAuthorityError;
import com.duzon.dbp.apimonitoring.advice.exception.ApiNotFoundError;
import com.duzon.dbp.apimonitoring.advice.exception.ApiNotFoundWithNumberError;
import com.duzon.dbp.apimonitoring.advice.exception.EmployeeIdSigninFailedError;
import com.duzon.dbp.apimonitoring.advice.exception.ErrAndDelayCheckError;
import com.duzon.dbp.apimonitoring.advice.exception.SameNameError;
import com.duzon.dbp.apimonitoring.dto.ApiDto;
import com.duzon.dbp.apimonitoring.dto.Employee;
import com.duzon.dbp.apimonitoring.dto.request.ReqApiDto;
import com.duzon.dbp.apimonitoring.repo.ApiRepo;
import com.duzon.dbp.apimonitoring.repo.EmployeeRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * ServiceApiService
 */
@Slf4j
@Service
public class ApiService {
    
    @Autowired
    ApiRepo apiRepo;
    
    @Autowired
    EmployeeRepo employeeRepo;

    @Autowired
    NotificationService notificationService;
    
    @Autowired
    JsonParseService jsonParseService;
    
    
    public void apiCreate(ReqApiDto dto) {
        ApiDto apiDto = new ApiDto();
        apiDto.setApi_category_no(dto.getApi_category_no());
        apiDto.setApi_url(dto.getApi_url());
        apiDto.setDescription(dto.getDescription());
        apiDto.setEmployee_no(dto.getEmployee_no());
        apiDto.setEmployee_sub_no(dto.getEmployee_sub_no());
        apiDto.setMethod(dto.getMethod());
        apiDto.setParam(dto.getParam());
        apiDto.setParameter_type("[\"" + dto.getParameter_type() + "\"]");
        apiDto.setResponse_list(dto.getResponse_list());
        apiDto.setResponse_type("[\"" + dto.getResponse_type() + "\"]");
        
        Employee em = employeeRepo.findByEmployeeNo(dto.getUpdate_employee_id()).orElseThrow(EmployeeIdSigninFailedError::new);
        apiDto.setUpdate_employee_no(em.getEmployee_no());
        
        if (apiRepo.NameCheck(dto.getApi_url(), dto.getMethod(), dto.getApi_category_no()).isEmpty()) {
            ApiDto a = apiRepo.save(apiDto);
            notificationService.ApiNoti(a);
        } else {
            throw new SameNameError();
        }
    }
    
    public void apiUpdate(long idx, ReqApiDto dto) {
        ApiDto apiDto = apiRepo.findById(idx).orElseThrow(ApiNotFoundWithNumberError::new);

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Employee authEm = employeeRepo.findByEmployeeNo(userDetails.getUsername()).orElseThrow(EmployeeIdSigninFailedError::new);
        log.info("# # # # # # # # # # # # # # # # # # #");
        log.info("authEm.getEmployee_no : " + authEm.getEmployee_no());
        log.info("apiDto.getEmployee_no : " + apiDto.getEmployee_no());
        log.info("apiDto.getEmployee_sub_no : " + apiDto.getEmployee_sub_no());
        log.info("# # # # # # # # # # # # # # # # # # #");

        if (!(apiDto.getEmployee_no() == 0 && apiDto.getEmployee_sub_no() == 0)) {
            if (!userDetails.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
                if (authEm.getEmployee_no() != apiDto.getEmployee_no() && authEm.getEmployee_no() != apiDto.getEmployee_sub_no()) {
                    throw new ApiAuthorityError();
                }            
            }            
        }

        long em_no = apiDto.getEmployee_no();
        long em_sub_no = apiDto.getEmployee_sub_no();

        apiDto.setApi_category_no(dto.getApi_category_no());
        apiDto.setApi_url(dto.getApi_url());
        apiDto.setDescription(dto.getDescription());
        apiDto.setEmployee_no(dto.getEmployee_no());
        apiDto.setEmployee_sub_no(dto.getEmployee_sub_no());
        apiDto.setMethod(dto.getMethod());
        apiDto.setParam(dto.getParam());
        apiDto.setParameter_type("[\"" + dto.getParameter_type() + "\"]");
        apiDto.setResponse_list(dto.getResponse_list());
        apiDto.setResponse_type("[\"" + dto.getResponse_type() + "\"]");
    
        Employee em = employeeRepo.findByEmployeeNo(dto.getUpdate_employee_id()).orElseThrow(EmployeeIdSigninFailedError::new);
        apiDto.setUpdate_employee_no(em.getEmployee_no());

        if (apiRepo.NameCheckUp(dto.getApi_url(), dto.getMethod(), dto.getApi_category_no(), idx) == null) {
            System.out.println("들어온다");
            ApiDto a = apiRepo.save(apiDto);
            
            if (dto.getEmployee_no() == dto.getEmployee_sub_no()) {
                if (em_no != dto.getEmployee_no() || em_sub_no != dto.getEmployee_no()) {
                    notificationService.ApiUpdateNoti(dto.getEmployee_no(), a.getApi_no());
                }
            } else {
                if (em_no != dto.getEmployee_no()) {
                    notificationService.ApiUpdateNoti(dto.getEmployee_no(), a.getApi_no());
                }
                if(em_sub_no != dto.getEmployee_sub_no()) {
                    notificationService.ApiUpdateNoti(dto.getEmployee_sub_no(), a.getApi_no());
                }
            }

        } else {
            throw new SameNameError();
        }   
    }

    public void apiT(long api_no) {
        ApiDto apiDto = apiRepo.findById(api_no).orElseThrow(ApiNotFoundWithNumberError::new);
        apiDto.setIs_deleted("T");
        apiRepo.save(apiDto);
    }
    
    public void apiErrStatusT(long api_no) {
        if (apiRepo.findApiErrorList(api_no) != 0) {
            ApiDto apiDto = apiRepo.findById(api_no).orElseThrow(ApiNotFoundWithNumberError::new);
            apiDto.setErr_status("T");
            apiRepo.save(apiDto);
        } else {
            throw new ErrAndDelayCheckError();
        }
    }
    
    public void apiErrStatusF(long api_no) {
        if (apiRepo.findApiErrorList(api_no) == 0) {
            ApiDto apiDto = apiRepo.findById(api_no).orElseThrow(ApiNotFoundWithNumberError::new);
            apiDto.setErr_status("F");
            apiRepo.save(apiDto);
        } else {
            throw new ErrAndDelayCheckError();
        }
    }
    
    public void apiDelayStatusT(long api_no) {
        if (apiRepo.findApiDelayList(api_no) != 0) {
            ApiDto apiDto = apiRepo.findById(api_no).orElseThrow(ApiNotFoundWithNumberError::new);
            apiDto.setDelay_status("T");
            apiRepo.save(apiDto);
        } else {
            throw new ErrAndDelayCheckError();
        }
    }

    public void apiDelayStatusF(long api_no) {
        if (apiRepo.findApiDelayList(api_no) == 0) {
            ApiDto apiDto = apiRepo.findById(api_no).orElseThrow(ApiNotFoundWithNumberError::new);
            apiDto.setDelay_status("F");
            apiRepo.save(apiDto);
        } else {
            throw new ErrAndDelayCheckError();
        }
    }

    public List<Map<String, Object>> findByIsDeleteList() {
        return jsonParseService.jsonListMapParse(apiRepo.findByIsDeleteList());
    }

	public Map<String, Object> findApiDetail(long service_no, long api_category_no, long api_no) {
        apiRepo.ApiCategoryServicecheck(api_category_no, service_no).orElseThrow(ApiNotFoundError::new);

        Map<String, Object> apiDetailservice = jsonParseService.jsonMapParseService(apiRepo.findApiDetailservice(service_no));
        Map<String, Object> apiDetailApiCategory = apiRepo.findApiDetailApiCategory(api_category_no);
        
        Map<String, Object> apiDetail = new HashMap<>();

        apiDetail.put("service", apiDetailservice);
        apiDetail.put("apicategory", apiDetailApiCategory);
        apiDetail.put("apiList", jsonParseService.jsonListMapParse(apiRepo.findApiDetailApi(api_category_no)));

		return apiDetail;
    }
    
    // ApiErr 생성 시 url, method로 api 찾기
	public Optional<ApiDto> findApino(String api_url, String method) {
		return apiRepo.findApino(api_url, method);
	}

	public Optional<ApiDto> findByApi_no(Long api_no) {
        return apiRepo.findByApi_no(api_no);
	}

	public List<Map<String, Object>> apiFullURLAndMethodList() {
        return apiRepo.apiFullURLAndMethodList();
	}
}