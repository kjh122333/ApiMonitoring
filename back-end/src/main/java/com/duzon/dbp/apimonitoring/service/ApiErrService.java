package com.duzon.dbp.apimonitoring.service;

import java.util.List;
import java.util.Map;

import com.duzon.dbp.apimonitoring.advice.exception.ApiAuthorityError;
import com.duzon.dbp.apimonitoring.advice.exception.ApiErrorNotFoundWithNumberError;
import com.duzon.dbp.apimonitoring.advice.exception.ApiNotFoundError;
import com.duzon.dbp.apimonitoring.advice.exception.ApiNotFoundWithNumberError;
import com.duzon.dbp.apimonitoring.advice.exception.ApiNotFoundWithURLandMethodError;
import com.duzon.dbp.apimonitoring.advice.exception.EmployeeIdSigninFailedError;
import com.duzon.dbp.apimonitoring.dto.ApiDto;
import com.duzon.dbp.apimonitoring.dto.ApiErrDto;
import com.duzon.dbp.apimonitoring.dto.Employee;
import com.duzon.dbp.apimonitoring.dto.request.ReqApiErrDto;
import com.duzon.dbp.apimonitoring.repo.ApiErrRepo;
import com.duzon.dbp.apimonitoring.repo.EmployeeRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * ServiceApiErrService
 */
@Slf4j
@Service
public class ApiErrService {

    @Autowired
    ApiErrRepo apiErrRepo;

    @Autowired
    EmployeeRepo employeeRepo;

    @Autowired
    ApiService apiService;

    @Autowired
    JsonParseService jsonParseService;

    public List<Map<String, Object>> apiErrList() {
        return apiErrRepo.findByList();
    }

    public Map<String, Object> apiErrGet(long idx) {
        apiErrRepo.findById(idx).orElseThrow(ApiErrorNotFoundWithNumberError::new);
        return jsonParseService.jsonMapParse(apiErrRepo.findApiErrGet(idx));
    }

    public List<Map<String, Object>> findByStatusList() {
        return apiErrRepo.findByStatusList();
    }

    public ApiErrDto apiErrCreate(ReqApiErrDto dto) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Employee authEm = employeeRepo.findByEmployeeNo(userDetails.getUsername()).orElseThrow(EmployeeIdSigninFailedError::new);

        ApiErrDto apiErrdto = new ApiErrDto();
        apiErrdto.setApi_err_code(dto.getApi_err_code());
        apiErrdto.setApi_err_msg(dto.getApi_err_msg());
        apiErrdto.setJira_url(dto.getJira_url());
        apiErrdto.setKibana_url(dto.getKibana_url());
        apiErrdto.setRef(dto.getRef());

        ApiDto NumbetApiDto = new ApiDto();
        ApiDto UrlAndMethodApidto = new ApiDto();
        if (dto.getApi_no() != null && (dto.getUrl() != null && dto.getMethod() != null)) {
            log.info("api_no, api_url, method 모두 입력 받은 경우");
            NumbetApiDto = apiService.findByApi_no(dto.getApi_no()).orElseThrow(ApiNotFoundWithNumberError::new);
            UrlAndMethodApidto = apiService.findApino(dto.getUrl(), dto.getMethod()).orElseThrow(ApiNotFoundWithURLandMethodError::new);

            if (NumbetApiDto.getApi_no() == UrlAndMethodApidto.getApi_no()) {

                if (!(NumbetApiDto.getEmployee_no() == 0 && NumbetApiDto.getEmployee_sub_no() == 0)) {
                    if (!userDetails.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
                        if (authEm.getEmployee_no() != NumbetApiDto.getEmployee_no() && authEm.getEmployee_no() !=NumbetApiDto.getEmployee_sub_no()) {
                            throw new ApiAuthorityError();
                        }            
                    }            
                }

                apiErrdto.setApi_no(dto.getApi_no());
                ApiErrDto a = apiErrRepo.save(apiErrdto);
                apiService.apiErrStatusT(dto.getApi_no());
                return a;
            } else {
                throw new ApiNotFoundError();
            }
        }
        // api_no는 입력받고, api_url, method 모두 입력 받지 못했을 경우
        if (dto.getApi_no() != null && (dto.getUrl() == null && dto.getMethod() == null)) {
            log.info("api_no는 입력받고, api_url, method 모두 입력 받지 못했을 경우");
            NumbetApiDto = apiService.findByApi_no(dto.getApi_no()).orElseThrow(ApiNotFoundWithNumberError::new);
            
            if (!(NumbetApiDto.getEmployee_no() == 0 && NumbetApiDto.getEmployee_sub_no() == 0)) {
                if (!userDetails.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
                    if (authEm.getEmployee_no() != NumbetApiDto.getEmployee_no() && authEm.getEmployee_no() !=NumbetApiDto.getEmployee_sub_no()) {
                        throw new ApiAuthorityError();
                    }            
                }            
            }

            apiErrdto.setApi_no(NumbetApiDto.getApi_no());
            ApiErrDto a = apiErrRepo.save(apiErrdto);
            apiService.apiErrStatusT(NumbetApiDto.getApi_no());
            return a;            
        }
        // api_no는 입력받고, api_url 또는 method가 입력 받지 못했을 경우
        if (dto.getApi_no() != null && (dto.getUrl() != null && dto.getMethod() == null) || (dto.getUrl() == null && dto.getMethod() != null)) {
            log.info("api_no는 입력받고, api_url 또는 method가 입력 받지 못했을 경우");
            throw new ApiNotFoundWithURLandMethodError();
        }
        // api_url과 method 모두 입력을 받고, api_no는 입력 받지 못했을 경우
        if (dto.getApi_no() == null && (dto.getUrl() != null && dto.getMethod() != null)) {
            log.info("api_url과 method 모두 입력을 받고, api_no는 입력 받지 못했을 경우");
            UrlAndMethodApidto = apiService.findApino(dto.getUrl(), dto.getMethod()).orElseThrow(ApiNotFoundWithURLandMethodError::new);

            if (!(UrlAndMethodApidto.getEmployee_no() == 0 && UrlAndMethodApidto.getEmployee_sub_no() == 0)) {
                if (!userDetails.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
                    if (authEm.getEmployee_no() != UrlAndMethodApidto.getEmployee_no() && authEm.getEmployee_no() !=UrlAndMethodApidto.getEmployee_sub_no()) {
                        throw new ApiAuthorityError();
                    }            
                }            
            }

            apiErrdto.setApi_no(UrlAndMethodApidto.getApi_no());
            ApiErrDto a = apiErrRepo.save(apiErrdto);
            apiService.apiErrStatusT(UrlAndMethodApidto.getApi_no());
            return a;
        }

        if (dto.getApi_no() == null && (dto.getUrl() == null || dto.getMethod() == null)) {
            log.info("api_no와 api_url 또는 method 모두 입력 받지 못햇을 경우");
            throw new ApiNotFoundError();
        }
        throw new ApiNotFoundError();
    }
    
    public void apiErrUpdate(long api_err_no, ApiErrDto dto) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Employee authEm = employeeRepo.findByEmployeeNo(userDetails.getUsername()).orElseThrow(EmployeeIdSigninFailedError::new);

        ApiDto Apidto = apiService.findByApi_no(dto.getApi_no()).orElseThrow(ApiNotFoundWithNumberError::new);

        if (!(Apidto.getEmployee_no() == 0 && Apidto.getEmployee_sub_no() == 0)) {
            if (!userDetails.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
                if (authEm.getEmployee_no() != Apidto.getEmployee_no() && authEm.getEmployee_no() !=Apidto.getEmployee_sub_no()) {
                    throw new ApiAuthorityError();
                }            
            }            
        }

        ApiErrDto apiErrDto = apiErrRepo.findById(api_err_no).orElseThrow(ApiErrorNotFoundWithNumberError::new);

        apiErrDto.setApi_err_comment(dto.getApi_err_comment());
        apiErrDto.setApi_err_status(dto.getApi_err_status());
        apiErrDto.setUpdated_timestamp(dto.getUpdated_timestamp());
        apiErrDto.setJira_url(dto.getJira_url());
        apiErrDto.setKibana_url(dto.getKibana_url());
        apiErrDto.setRef(dto.getRef());

        apiErrRepo.save(apiErrDto);

        if (dto.getApi_err_status().equals("F")) {
            apiService.apiErrStatusF(dto.getApi_no());
        }
    }
}