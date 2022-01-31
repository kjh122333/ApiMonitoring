package com.duzon.dbp.apimonitoring.controller;

import java.util.List;
import java.util.Map;

import com.duzon.dbp.apimonitoring.dto.ApiCategoryDto;
import com.duzon.dbp.apimonitoring.dto.message.Common;
import com.duzon.dbp.apimonitoring.dto.message._List;
import com.duzon.dbp.apimonitoring.service.ApiCategoryService;
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
 * ApiCategoryController
 */
@Api(tags = { "3. ApiCategory" })
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
public class ApiCategoryController {

    @Autowired
    ApiCategoryService apiCategoryService;

    @Autowired
    ResponseService responseService;

    @ApiOperation(value = "API Category List", notes = "API 카테고리 리스트")
    @GetMapping("/apicategory")
    public _List<Map<String, Object>> apicategoryList() {
        return responseService.getResult_List_Map(apiCategoryService.apicategoryAllList());
    }

    @ApiOperation(value = "API Category List", notes = "API 카테고리 리스트")
    @GetMapping("/apicategory/f")
    public _List<ApiCategoryDto> apicategoryListF(){ 
        return responseService.getResult_List(apiCategoryService.apicategoryListF());
    }

    @ApiOperation(value = "API Category Create", notes = "API 카테고리 생성")
    @PostMapping("/apicategory")
    public Common apicategoryCreate(@ApiParam(value = "API 카테고리 생성 객체", required = true) @RequestBody ApiCategoryDto dto){
        apiCategoryService.apicategoryCreate(dto);
        return responseService.getResultSuccess();
    }
    
    @ApiOperation(value = "API Category Update", notes = "API 카테고리 수정")
    @PutMapping("/apicategory/{idx}")
    public Common apicategoryUpdate(
        @ApiParam(value = "API 카테고리 번호", required = true) @PathVariable long idx,
        @ApiParam(value = "API 카테고리 수정 객체", required = true)@RequestBody ApiCategoryDto Dto){
        apiCategoryService.apicategoryUpdate(idx, Dto);
        return responseService.getResultSuccess();
    }
    
    @ApiOperation(value = "API Category IsDelete 'T'", notes = "API 카테고리 isDelete 'T'")
    @PatchMapping("/apicategory/t/")
    public Common apicategoryT(@ApiParam(value = "API 카테고리 번호", required = true) @RequestBody List<Long> idx){
        apiCategoryService.apicategoryT(idx);
        return responseService.getResultSuccess();
    }

    // Service isDelete Child Check
    @ApiOperation(value = "API Category isDelete 'T' Child List", notes = "API 카테고리 isDelete T 될때 같이 T 되는 자식 리스트")
    @GetMapping("/apicategory/tcheck")
    public _List<Map<String, Object>> apicategoryTChildCheck(@ApiParam(value = "API 카테고리 번호", required = true) @RequestParam List<Long> idx) {
        return responseService.getResult_List_Map(apiCategoryService.apicategoryTChildCheck(idx));
    }    
}