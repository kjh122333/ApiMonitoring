package com.duzon.dbp.apimonitoring.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.duzon.dbp.apimonitoring.advice.exception.SameNameError;
import com.duzon.dbp.apimonitoring.advice.exception.ServiceCategoryNotFoundWithNumberError;
import com.duzon.dbp.apimonitoring.advice.exception.SqlIndexError;
import com.duzon.dbp.apimonitoring.dto.ServiceCategoryDto;
import com.duzon.dbp.apimonitoring.repo.ServiceCategoryRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CategoryService
 */
@Service
public class ServiceCategoryService {

    @Autowired
    ServiceCategoryRepo serviceCategoryRepo;

    @Autowired
    JsonParseService jsonParseService;
    
    public List<ServiceCategoryDto> serviceCategoryListF() {
        return serviceCategoryRepo.findlistF();
    }
    
    public List<ServiceCategoryDto> serviceCategoryListAll() {
        return serviceCategoryRepo.findlist();
    }
    
    //! == admin ==
    public void serviceCategoryCreate(ServiceCategoryDto dto) {
        if (serviceCategoryRepo.NameCheck(dto.getCategory_name_kr()) == null) {
            serviceCategoryRepo.save(dto);
        } else {
            throw new SameNameError();
        }
    }

    //! == admin ==
    public void serviceCategoryUpdate(long service_category_no, ServiceCategoryDto dto) {
        ServiceCategoryDto serviceCategoryDto = serviceCategoryRepo.findById(service_category_no).orElseThrow(ServiceCategoryNotFoundWithNumberError::new);
        serviceCategoryDto.setCategory_name_kr(dto.getCategory_name_kr());
        serviceCategoryDto.setIs_deleted(dto.getIs_deleted());
        
        if (serviceCategoryRepo.NameCheckUp(dto.getCategory_name_kr(), dto.getService_category_no()) == null) {
            serviceCategoryRepo.save(serviceCategoryDto);
        } else {
            throw new SameNameError();
        }
    }

    //! == admin ==
    @Transactional
    public void serviceCategoryT(List<Long> service_category_no) {
        try {
            for (int i = 0; i < service_category_no.size(); i++) {
                Optional<ServiceCategoryDto> serviceCategoryDto = serviceCategoryRepo.findById(service_category_no.get(i));
    
                serviceCategoryDto.get().setIs_deleted("T");
                serviceCategoryRepo.save(serviceCategoryDto.get());
                serviceCategoryRepo.isDeleteAT(service_category_no.get(i));
                serviceCategoryRepo.isDeleteAlT(service_category_no.get(i));
                serviceCategoryRepo.isDeleteAllT(service_category_no.get(i));
            }         
        } catch (Exception e) {
            throw new SqlIndexError();
        }
    }
    
    //! == admin ==
    @Transactional
	public List<Map<String, Object>> serviceCategoryTChildCheck(List<Long> service_category_no) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            for (int i = 0; i < service_category_no.size(); i++) {
                
                List<Map<String, Object>> apiDetailservice = jsonParseService.jsonListMapParseService(serviceCategoryRepo.ChildSerivce(service_category_no.get(i)));
                List<Map<String, Object>> apiDetailApiCategory = serviceCategoryRepo.ChildApiCategory(service_category_no.get(i));
                
                Map<String, Object> apiDetail = new HashMap<>();
                apiDetail.put("servicecategoryname", serviceCategoryRepo.findName(service_category_no.get(i)));
                apiDetail.put("serviceList", apiDetailservice);
                apiDetail.put("apicategoryList", apiDetailApiCategory);
                apiDetail.put("apiList", jsonParseService.jsonListMapParse(serviceCategoryRepo.ChildApi(service_category_no.get(i))));
                
                list.add(apiDetail);
            }
        } catch (Exception e) {
            throw new SqlIndexError();
        }
        return list;
    }
}