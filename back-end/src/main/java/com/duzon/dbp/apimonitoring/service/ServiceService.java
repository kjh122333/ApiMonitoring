package com.duzon.dbp.apimonitoring.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.duzon.dbp.apimonitoring.advice.exception.SameNameError;
import com.duzon.dbp.apimonitoring.advice.exception.SameURLError;
import com.duzon.dbp.apimonitoring.advice.exception.ServiceNotFoundWithNumberError;
import com.duzon.dbp.apimonitoring.advice.exception.SqlIndexError;
import com.duzon.dbp.apimonitoring.dto.ServiceDto;
import com.duzon.dbp.apimonitoring.dto.request.ReqServiceDto;
import com.duzon.dbp.apimonitoring.repo.ServiceRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ServiceService
 */
@Service
public class ServiceService {
    
    @Autowired
    ServiceRepo serviceRepo;

    @Autowired
    JsonParseService jsonParseService;
    
    public List<Map<String, Object>> serviceList() {
        return jsonParseService.jsonListMapParseService(serviceRepo.FindList());
    }
    
    public List<Map<String, Object>> serviceAllList() {
        return jsonParseService.jsonListMapParseService(serviceRepo.FindAllList());
    }

    public void serviceCreate(ReqServiceDto dto) {
        ServiceDto serviceDto = new ServiceDto();
        serviceDto.setGroup_no(dto.getGroup_no());
        serviceDto.setService_category_no(dto.getService_category_no());
        serviceDto.setService_name_kr(dto.getService_name_kr());
        serviceDto.setService_url(dto.getService_url());
        serviceDto.setService_code(dto.getService_code());

        if (serviceRepo.NameCheck(serviceDto.getService_name_kr(), serviceDto.getService_category_no()) != null) {
            throw new SameNameError();
        }
        if (serviceRepo.UrlCheck(serviceDto.getService_url()) != null) {
            throw new SameURLError();
        }
        serviceRepo.save(serviceDto);
    }
    
    public void serviceUpdate(long service_no, ServiceDto dto) {
        ServiceDto serviceDto = serviceRepo.findById(service_no).orElseThrow(ServiceNotFoundWithNumberError::new);
        serviceDto.setService_category_no(dto.getService_category_no());
        serviceDto.setService_name_kr(dto.getService_name_kr());
        serviceDto.setGroup_no(dto.getGroup_no());
        serviceDto.setService_state(dto.getService_state());
        serviceDto.setService_url(dto.getService_url());
        serviceDto.setService_code(dto.getService_code());
        
        if (serviceRepo.NameCheckUp(dto.getService_name_kr(), dto.getService_no(), dto.getService_category_no()) != null) {
            throw new SameNameError();
        }
        if (serviceRepo.UrlCheckUp(dto.getService_url(), dto.getService_no()) != null) {
            throw new SameURLError();
        }
        serviceRepo.save(serviceDto);
    }
    
    @Transactional
    public void serviceT(List<Long> service_no) {
        try {
            for (int i = 0; i < service_no.size(); i++) {
                Optional<ServiceDto> serviceDto = serviceRepo.findById(service_no.get(i));
                serviceDto.get().setIs_deleted("T");
                serviceRepo.save(serviceDto.get());

                serviceRepo.isDeleteAllT(service_no.get(i));
                serviceRepo.isDeleteAlT(service_no.get(i));
                serviceRepo.isDeleteAT(service_no.get(i));
            }
        } catch (Exception e) {
            throw new SqlIndexError();
        }
    }
    
    @Transactional
	public List<Map<String, Object>> serviceTChildCheck(List<Long> service_no) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            for (int i = 0; i < service_no.size(); i++) {
                List<Map<String, Object>> apiDetailApiCategory = serviceRepo.ChildApiCategory(service_no.get(i));
                
                Map<String, Object> apiDetail = new HashMap<>();
                
                apiDetail.put("servicecategoryname", serviceRepo.findServiceCategoryName(service_no.get(i)));
                apiDetail.put("serviceList", jsonParseService.jsonMapParseService(serviceRepo.findServiceGet(service_no.get(i))));
                apiDetail.put("apicategoryList", apiDetailApiCategory);
                apiDetail.put("apiList", jsonParseService.jsonListMapParse(serviceRepo.ChildApi(service_no.get(i))));
                
                list.add(apiDetail);
            }
        } catch (Exception e) {
            throw new SqlIndexError();
        }
        return list;
    }
}