package com.duzon.dbp.apimonitoring.controller.admin;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.duzon.dbp.apimonitoring.dto.ServiceCategoryDto;
import com.duzon.dbp.apimonitoring.dto.message.Common;
import com.duzon.dbp.apimonitoring.dto.message._List;
import com.duzon.dbp.apimonitoring.service.ServiceCategoryService;
import com.duzon.dbp.apimonitoring.service.response.ResponseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * AdminServiceCategoryController
 */
@Api(tags = { "0.1 Admin Service Category" })
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin")
public class AdminServiceCategoryController {

    @Autowired
    ServiceCategoryService serviceCategoryService;

    @Autowired
    ResponseService responseService;

    // Service Category 생성
    @ApiOperation(value = "Service Category Create", notes = "서비스 카테고리 생성")
    @PostMapping("/category")
    public Common serviceCategoryCreate(
        @ApiParam(value = "서비스 카테고리 생성 객체", required = true) @RequestBody @Valid ServiceCategoryDto Dto){
        serviceCategoryService.serviceCategoryCreate(Dto);
        return responseService.getResultSuccess();
    }

    // Service Category 수정
    @ApiOperation(value = "Service Category Update", notes = "서비스 카테고리 수정")
    @PutMapping("/category/{idx}")
    public Common serviceCategoryUpdate(
        @ApiParam(value = "서비스 카테고리 번호", required = true) @PathVariable long idx,
        @ApiParam(value = "서비스 카테고리 수정 객체", required = true) @RequestBody @Valid ServiceCategoryDto Dto
        ){
            serviceCategoryService.serviceCategoryUpdate(idx, Dto);
            return responseService.getResultSuccess();
    }

    // Service Category isDelete All 'T'
    @ApiOperation(value = "Service Category isDelete 'T'", notes = "서비스 카테고리 isDelete All 'T'")
    @PatchMapping("/category/t")
    public Common serviceCategoryT(@ApiParam(value = "서비스 카테고리 번호", required = true) @RequestBody List<Long> idx){
        serviceCategoryService.serviceCategoryT(idx);
        return responseService.getResultSuccess();
    }
    
    // Service Category isDelete Child Check
    @ApiOperation(value = "Service Category isDelete 'T' Child List", notes = "서비스 카테고리 isDelete T 될때 같이 T 되는 자식 리스트")
    @GetMapping("/category/tcheck")
    public _List<Map<String, Object>> serviceCategoryTChildCheck(@ApiParam(value = "서비스 카테고리 번호", required = true) @RequestParam List<Long> idx){
        return responseService.getResult_List_Map(serviceCategoryService.serviceCategoryTChildCheck(idx));
    }
}