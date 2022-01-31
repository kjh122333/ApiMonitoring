package com.duzon.dbp.apimonitoring.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.duzon.dbp.apimonitoring.advice.exception.ApiAuthorityError;
import com.duzon.dbp.apimonitoring.advice.exception.ApiDelayNotFoundWithNumberError;
import com.duzon.dbp.apimonitoring.advice.exception.ApiNotFoundError;
import com.duzon.dbp.apimonitoring.advice.exception.ApiNotFoundWithNumberError;
import com.duzon.dbp.apimonitoring.advice.exception.ApiNotFoundWithURLandMethodError;
import com.duzon.dbp.apimonitoring.advice.exception.EmployeeIdSigninFailedError;
import com.duzon.dbp.apimonitoring.dto.ApiDelayDto;
import com.duzon.dbp.apimonitoring.dto.ApiDto;
import com.duzon.dbp.apimonitoring.dto.Employee;
import com.duzon.dbp.apimonitoring.dto.request.ReqApiDelayDto;
import com.duzon.dbp.apimonitoring.repo.ApiDelayRepo;
import com.duzon.dbp.apimonitoring.repo.EmployeeRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * ServiceApiResService
 */
@Slf4j
@Service
public class ApiDelayService {

    @Autowired
    ApiDelayRepo apiDelayRepo;

    @Autowired
    EmployeeRepo employeeRepo;

    @Autowired
    ApiService apiService;

    @Autowired
    JsonParseService jsonParseService;

    public List<Map<String, Object>> apiDelayList() {
        return apiDelayRepo.findByList();
    }

    public Map<String, Object> apiDelayGet(Long idx) {
        apiDelayRepo.findById(idx).orElseThrow(ApiDelayNotFoundWithNumberError::new);
        Map<String, Object> delayGetList = apiDelayRepo.findApiDelayGet(idx);
        Map<String, Object> delayTime = apiDelayRepo.findApiDelayTime(Long.valueOf(delayGetList.get("api_no").toString()));
        Map<String, Object> list = new HashMap<String, Object>();
        list.putAll(delayGetList);
        list.putAll(delayTime);
        
        return jsonParseService.jsonMapParse(list);
	}

    public List<Map<String, Object>> findByStatusList() {
        return apiDelayRepo.findByStatusList();
    }
    
    public void apiDelayUpdate(long api_delay_no, ApiDelayDto dto) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Employee authEm = employeeRepo.findByEmployeeNo(userDetails.getUsername()).orElseThrow(EmployeeIdSigninFailedError::new);

        ApiDto Apidto = apiService.findByApi_no(dto.getApi_no()).orElseThrow(ApiNotFoundWithNumberError::new);

        if (!(Apidto.getEmployee_no() == 0 && Apidto.getEmployee_sub_no() == 0)) {
            if (!userDetails.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
                if (authEm.getEmployee_no() != Apidto.getEmployee_no() && authEm.getEmployee_no() != Apidto.getEmployee_sub_no()) {
                    throw new ApiAuthorityError();
                }            
            }            
        }

        ApiDelayDto apiDelayDto = apiDelayRepo.findById(api_delay_no).orElseThrow(ApiDelayNotFoundWithNumberError::new);
        apiDelayDto.setApi_delay_status(dto.getApi_delay_status());
        apiDelayDto.setApi_delay_comment(dto.getApi_delay_comment());
        apiDelayDto.setUpdated_timestamp(dto.getUpdated_timestamp());
        apiDelayDto.setJira_url(dto.getJira_url());
        apiDelayDto.setKibana_url(dto.getKibana_url());
        apiDelayDto.setRef(dto.getRef());

        apiDelayRepo.save(apiDelayDto);

        if (dto.getApi_delay_status().equals("F")) {
            apiService.apiDelayStatusF(dto.getApi_no());
        }
    }

	public ApiDelayDto apiDelayCreate(ReqApiDelayDto dto) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Employee authEm = employeeRepo.findByEmployeeNo(userDetails.getUsername()).orElseThrow(EmployeeIdSigninFailedError::new);
        
        ApiDelayDto apiDelaydto = new ApiDelayDto();
        apiDelaydto.setApi_delay_code(dto.getApi_delay_code());
        apiDelaydto.setApi_delay_msg(dto.getApi_delay_msg());
        apiDelaydto.setApi_delay_time(dto.getApi_delay_time());
        apiDelaydto.setJira_url(dto.getJira_url());
        apiDelaydto.setKibana_url(dto.getKibana_url());
        apiDelaydto.setRef(dto.getRef());

        ApiDto NumbetApiDto = new ApiDto();
        ApiDto UrlAndMethodApidto = new ApiDto();
        
        if (dto.getApi_no() != null && (dto.getUrl() != null && dto.getMethod() != null)) {
            log.info("api_no, api_url, method 모두 입력 받은 경우");
            NumbetApiDto = apiService.findByApi_no(dto.getApi_no()).orElseThrow(ApiNotFoundWithNumberError::new);
            UrlAndMethodApidto = apiService.findApino(dto.getUrl(), dto.getMethod()).orElseThrow(ApiNotFoundWithURLandMethodError::new);

            if (NumbetApiDto.getApi_no() == UrlAndMethodApidto.getApi_no()) {

                if (!(NumbetApiDto.getEmployee_no() == 0 && NumbetApiDto.getEmployee_sub_no() == 0)) {
                    if (!userDetails.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
                        if (authEm.getEmployee_no() != NumbetApiDto.getEmployee_no() && authEm.getEmployee_no() != NumbetApiDto.getEmployee_sub_no()) {
                            throw new ApiAuthorityError();
                        }            
                    }            
                }

                apiDelaydto.setApi_no(dto.getApi_no());
                ApiDelayDto a = apiDelayRepo.save(apiDelaydto);
                apiService.apiDelayStatusT(dto.getApi_no());
                return a;
            } else {
                throw new ApiNotFoundError();
            }
        }
        
        if (dto.getApi_no() != null && (dto.getUrl() == null && dto.getMethod() == null)) {
            log.info("api_no는 입력받고, api_url, method 모두 입력 받지 못했을 경우");
            NumbetApiDto = apiService.findByApi_no(dto.getApi_no()).orElseThrow(ApiNotFoundWithNumberError::new);
            
            if (!(NumbetApiDto.getEmployee_no() == 0 && NumbetApiDto.getEmployee_sub_no() == 0)) {
                if (!userDetails.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
                    if (authEm.getEmployee_no() != NumbetApiDto.getEmployee_no() && authEm.getEmployee_no() != NumbetApiDto.getEmployee_sub_no()) {
                        throw new ApiAuthorityError();
                    }            
                }            
            }

            apiDelaydto.setApi_no(NumbetApiDto.getApi_no());
            ApiDelayDto a = apiDelayRepo.save(apiDelaydto);
            apiService.apiDelayStatusT(NumbetApiDto.getApi_no());
            return a;       
        }
        
        if (dto.getApi_no() != null && (dto.getUrl() != null && dto.getMethod() == null) || (dto.getUrl() == null && dto.getMethod() != null)) {
            log.info("api_no는 입력받고, api_url 또는 method가 입력 받지 못했을 경우");
            throw new ApiNotFoundWithURLandMethodError();
        }
        
        if (dto.getApi_no() == null && (dto.getUrl() != null && dto.getMethod() != null)) {
            log.info("api_url과 method 모두 입력을 받고, api_no는 입력 받지 못했을 경우");
            UrlAndMethodApidto = apiService.findApino(dto.getUrl(), dto.getMethod()).orElseThrow(ApiNotFoundWithURLandMethodError::new);

            if (!(UrlAndMethodApidto.getEmployee_no() == 0 && UrlAndMethodApidto.getEmployee_sub_no() == 0)) {
                if (!userDetails.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
                    if (authEm.getEmployee_no() != UrlAndMethodApidto.getEmployee_no() && authEm.getEmployee_no() != UrlAndMethodApidto.getEmployee_sub_no()) {
                        throw new ApiAuthorityError();
                    }            
                }            
            }

            apiDelaydto.setApi_no(UrlAndMethodApidto.getApi_no());
            ApiDelayDto a = apiDelayRepo.save(apiDelaydto);
            apiService.apiDelayStatusT(UrlAndMethodApidto.getApi_no());
            return a;
        }
        
        if (dto.getApi_no() == null && (dto.getUrl() == null || dto.getMethod() == null)) {
            log.info("api_no와 api_url 또는 method 모두 입력 받지 못햇을 경우");
            throw new ApiNotFoundError();
        }
        throw new ApiNotFoundError();
	}
}