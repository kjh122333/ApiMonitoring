package com.duzon.dbp.apimonitoring.controller;

import java.util.Map;

import javax.validation.Valid;

import com.duzon.dbp.apimonitoring.config.mapper.ListMapper;
import com.duzon.dbp.apimonitoring.dto.message.Common;
import com.duzon.dbp.apimonitoring.dto.message._List;
import com.duzon.dbp.apimonitoring.dto.message._Single;
import com.duzon.dbp.apimonitoring.dto.request.ReqApiDto;
import com.duzon.dbp.apimonitoring.service.ApiService;
import com.duzon.dbp.apimonitoring.service.response.ResponseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * ServiceApiController
 */
@Api(tags = { "4. Api" })
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
public class ApiController {

    @Autowired
    ApiService apiService;

    @Autowired
    ResponseService responseService;

    @Autowired
    ListMapper mapper;

    @ApiOperation(value = "API Get", notes = "API 단건")
    @GetMapping("/api/detail/{idx}")
    public _Single<Map<String, Object>> apiGet(
        @ApiParam(value = "서비스 단건 번호", required = true) @RequestParam Long service_no,
        @ApiParam(value = "API 카테고리 단건 번호", required = true) @RequestParam Long api_category_no,
        @ApiParam(value = "API 단건 번호", required = true) @PathVariable Long idx
        ){
        return responseService.getResult_Single(apiService.findApiDetail(service_no, api_category_no, idx));
    }

    @ApiOperation(value = "API isDelete List 'F'", notes = "API isDelete 리스트 'F'")
    @GetMapping("/api/isdeletef")
    public _List<Map<String, Object>> findByIsDeleteList(){ 
        return responseService.getResult_List_Map(apiService.findByIsDeleteList());
    }

    @ApiOperation(value = "API Create", notes = "API 생성")
    @PostMapping("/api")
    public Common apiCreate(@ApiParam(value = "API 생성 객체", required = true) @RequestBody @Valid ReqApiDto dto){
        apiService.apiCreate(dto);
        return responseService.getResultSuccess();
    }

    @ApiOperation(value = "API isDelete 'T'", notes = "API isDelete 'T'")
    @PatchMapping("/api/t/{idx}")
    public Common apiT(@ApiParam(value = "API 번호", required = true)@PathVariable long idx){
        apiService.apiT(idx);
        return responseService.getResultSuccess();
    }

    @ApiOperation(value = "API Update", notes = "API 수정")
    @PatchMapping("/api/{idx}")
    public Common apiUpdate(
        @ApiParam(value = "API 번호", required = true)@PathVariable long idx,
        @ApiParam(value = "API 수정 객체", required = true)@RequestBody @Valid ReqApiDto Dto
        ){
        apiService.apiUpdate(idx, Dto);
        return responseService.getResultSuccess();
    }
    
    // Err, Delay 생성 시 API Full URL, Method 리스트
    @ApiOperation(value = "Err, Delay Create API Full URL, Method List", notes = "Err, Delay 생성 시 API Full URL, Method 리스트")
    @GetMapping("/api/fullurlmethod")
    public _List<Map<String, Object>> apiFullURLAndMethodList(){ 
        return responseService.getResult_List_Map(apiService.apiFullURLAndMethodList());
    }
}