package com.duzon.dbp.apimonitoring.controller;

import java.util.Map;

import javax.validation.Valid;

import com.duzon.dbp.apimonitoring.dto.ApiDelayDto;
import com.duzon.dbp.apimonitoring.dto.message.Common;
import com.duzon.dbp.apimonitoring.dto.message._List;
import com.duzon.dbp.apimonitoring.dto.message._Single;
import com.duzon.dbp.apimonitoring.dto.request.ReqApiDelayDto;
import com.duzon.dbp.apimonitoring.service.ApiDelayService;
import com.duzon.dbp.apimonitoring.service.NotificationService;
import com.duzon.dbp.apimonitoring.service.response.ResponseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * ApiDelayController
 */
@Api(tags = { "6. ApiDelay" })
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
public class ApiDelayController {

    @Autowired
    ApiDelayService apiDelayService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    ResponseService responseService;

    @ApiOperation(value = "API Delay List", notes = "API Delay 리스트")
    @GetMapping("/apidelay")
    public _List<Map<String, Object>> apiDelayList(){
        return responseService.getResult_List(apiDelayService.apiDelayList());
    }

    @ApiOperation(value = "API Delay Get", notes = "API Delay 단건")
    @GetMapping("/apidelay/{idx}")
    public _Single<Map<String, Object>> apiDelayGet(@ApiParam(value = "API Delay 번호", required = true) @PathVariable Long idx){
        return responseService.getResult_Single_Map(apiDelayService.apiDelayGet(idx));
    }

    @ApiOperation(value = "API Delay List", notes = "API Delay Status 리스트 'F'")
    @GetMapping("/apidelay/status")
    public _List<Map<String, Object>> findByStatusList(){
        return responseService.getResult_List(apiDelayService.findByStatusList());
    }

    @ApiOperation(value = "API Delay Update", notes = "API Delay 수정")
    @PatchMapping("/apidelay/{idx}")
    public Common apiDelayUpdate(
        @ApiParam(value = "API Err 번호", required = true)@PathVariable long idx,
        @ApiParam(value = "API Delay 수정 객체", required = true) @RequestBody ApiDelayDto Dto
        ){
        apiDelayService.apiDelayUpdate(idx, Dto);
        return responseService.getResultSuccess();
    }

    @ApiOperation(value = "API Delay Create", notes = "API Delay 생성")
    @PostMapping("/apidelay")
    public Common apiDelayCreate(@ApiParam(value = "API Err 생성 객체", required = true) @RequestBody @Valid ReqApiDelayDto Dto){
        ApiDelayDto a = apiDelayService.apiDelayCreate(Dto);
        notificationService.DelayNoti(a);
        return responseService.getResultSuccess();
    }
}