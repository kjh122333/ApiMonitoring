package com.duzon.dbp.apimonitoring.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.duzon.dbp.apimonitoring.dto.ServiceDto;
import com.duzon.dbp.apimonitoring.dto.message.Common;
import com.duzon.dbp.apimonitoring.dto.message._List;
import com.duzon.dbp.apimonitoring.dto.request.ReqServiceDto;
import com.duzon.dbp.apimonitoring.service.ServiceService;
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
 * ServiceController
 */
@Api(tags = { "2. Service" })
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
public class ServiceController {

    @Autowired
    ServiceService serviceService;

    @Autowired
    ResponseService responseService;

    @ApiOperation(value = "Service List", notes = "서비스 리스트")
    @GetMapping("/service")
    public _List<Map<String, Object>> serviceList(){
        return responseService.getResult_List_Map(serviceService.serviceAllList());
    }

    @ApiOperation(value = "Service List", notes = "서비스 리스트")
    @GetMapping("/service/f")
    public _List<Map<String, Object>> serviceListF(){
        return responseService.getResult_List_Map(serviceService.serviceList());
    }

    // Service 생성
    @ApiOperation(value = "Service Create", notes = "서비스 생성")
    @PostMapping("/service")
    public Common serviceCreate(@ApiParam(value = "서비스 생성 객체", required = true) @RequestBody @Valid ReqServiceDto dto){
        serviceService.serviceCreate(dto);
        return responseService.getResultSuccess();
    }

    @ApiOperation(value = "Service Update", notes = "서비스 수정")
    @PutMapping("/service/{idx}")
    public Common serviceUpdate(
        @ApiParam(value = "서비스 카테고리 번호", required = true) @PathVariable long idx,
        @ApiParam(value = "서비스 수정 객체", required = true)@RequestBody @Valid ServiceDto dto
        ){
        serviceService.serviceUpdate(idx, dto);
        return responseService.getResultSuccess();
    }

    // Service isDelete Child Check
    @ApiOperation(value = "Service isDelete 'T' Child List", notes = "서비스 isDelete T 될때 같이 T 되는 자식 리스트")
    @GetMapping("/service/tcheck")
    public _List<Map<String, Object>> serviceTChildCheck(@ApiParam(value = "서비스 번호", required = true) @RequestParam List<Long> idx) {
        return responseService.getResult_List_Map(serviceService.serviceTChildCheck(idx));
    }

    // Service isDelete All 'T'
    @ApiOperation(value = "Service IsDelete 'T'", notes = "서비스 isDelete 'T'")
    @PatchMapping("/service/t")
    public Common serviceT(@ApiParam(value = "서비스 번호", required = true) @RequestBody List<Long> idx){
        serviceService.serviceT(idx);
        return responseService.getResultSuccess();
    }
}