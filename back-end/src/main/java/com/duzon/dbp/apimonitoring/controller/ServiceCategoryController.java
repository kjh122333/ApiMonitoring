package com.duzon.dbp.apimonitoring.controller;

import com.duzon.dbp.apimonitoring.dto.ServiceCategoryDto;
import com.duzon.dbp.apimonitoring.dto.message._List;
import com.duzon.dbp.apimonitoring.service.ServiceCategoryService;
import com.duzon.dbp.apimonitoring.service.response.ResponseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * ServiceCategoryController
 */
@Api(tags = { "1. ServiceCategory" })
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
public class ServiceCategoryController {

    @Autowired
    ServiceCategoryService serviceCategoryService;

    @Autowired
    ResponseService responseService;

    // Service Category 'T'/'F' 리스트
    @ApiOperation(value = "Service Category List (isDeleted 'T'/'F')", notes = "서비스 카테고리 리스트 (isDeleted 'T'/'F')")
    @GetMapping("/category")
    public _List<ServiceCategoryDto> serviceCategoryList(){
        return responseService.getResult_List(serviceCategoryService.serviceCategoryListAll());
    }

    @ApiOperation(value = "Service Category List (isDeleted 'F')", notes = "서비스 카테고리 리스트 (isDeleted 'F')")
    @GetMapping("/category/f")
    public _List<ServiceCategoryDto> serviceCategoryListF(){
        return responseService.getResult_List(serviceCategoryService.serviceCategoryListF());
    }
}