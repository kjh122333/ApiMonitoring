package com.duzon.dbp.apimonitoring.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.duzon.dbp.apimonitoring.advice.exception.ApiCategoryNotFoundWithNumberError;
import com.duzon.dbp.apimonitoring.advice.exception.SameNameError;
import com.duzon.dbp.apimonitoring.advice.exception.SqlIndexError;
import com.duzon.dbp.apimonitoring.dto.ApiCategoryDto;
import com.duzon.dbp.apimonitoring.repo.ApiCategoryRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ApiCategoryService
 */
@Service
public class ApiCategoryService {
    
    @Autowired
    ApiCategoryRepo apiCategoryRepo;
    
    @Autowired
    JsonParseService jsonParseService;
    
    public List<ApiCategoryDto> apicategoryListF() {
        return apiCategoryRepo.findListF();
    }
    
    public List<Map<String, Object>> apicategoryAllList() {
        return apiCategoryRepo.findAllList();
    }
    
    public void apicategoryCreate(ApiCategoryDto dto) {
        if (apiCategoryRepo.NameCheck(dto.getApi_category_name_kr(), dto.getService_no()) == null) {
            apiCategoryRepo.save(dto);
        } else {
            throw new SameNameError();
        }
    }

    public void apicategoryUpdate(long api_category_no, ApiCategoryDto dto) {
        ApiCategoryDto apiCategoryDto = apiCategoryRepo.findById(api_category_no).orElseThrow(ApiCategoryNotFoundWithNumberError::new);
        apiCategoryDto.setApi_category_name_kr(dto.getApi_category_name_kr());
        apiCategoryDto.setIs_deleted(dto.getIs_deleted());
        apiCategoryDto.setService_no(dto.getService_no());
        
        if (apiCategoryRepo.NameCheckUp(dto.getApi_category_name_kr(), dto.getApi_category_no(), dto.getService_no()) == null) {
            apiCategoryRepo.save(apiCategoryDto);
        } else {
            throw new SameNameError();
        }
    }

    @Transactional
    public void apicategoryT(List<Long> api_category_no) {
        try {
            for (int i = 0; i < api_category_no.size(); i++) {
                Optional<ApiCategoryDto> apiCategoryDto = apiCategoryRepo.findById(api_category_no.get(i));
                apiCategoryDto.get().setIs_deleted("T");
                apiCategoryRepo.save(apiCategoryDto.get());

                apiCategoryRepo.isDeleteAllT(api_category_no.get(i));
                apiCategoryRepo.isDeleteAlT(api_category_no.get(i));
            }
        } catch (Exception e) {
            throw new SqlIndexError();
        }
    }

    @Transactional
    public List<Map<String, Object>> apicategoryTChildCheck(List<Long> service_no) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            for (int i = 0; i < service_no.size(); i++) {
                
                List<Map<String, Object>> apiDetailApiCategory = jsonParseService.jsonListMapParse(apiCategoryRepo.ChildApi(service_no.get(i)));
                
                Map<String, Object> apiDetail = new HashMap<>();
                apiDetail.put("apicategory", apiCategoryRepo.findName(service_no.get(i)));
                apiDetail.put("apiList", apiDetailApiCategory);
                
                list.add(apiDetail);
            }
        } catch (Exception e) {
            throw new SqlIndexError();
        }
        return list;
    }
}