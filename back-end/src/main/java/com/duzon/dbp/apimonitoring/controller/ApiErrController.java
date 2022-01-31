package com.duzon.dbp.apimonitoring.controller;

import java.util.Map;

import javax.validation.Valid;

import com.duzon.dbp.apimonitoring.dto.ApiErrDto;
import com.duzon.dbp.apimonitoring.dto.message.Common;
import com.duzon.dbp.apimonitoring.dto.message._List;
import com.duzon.dbp.apimonitoring.dto.message._Single;
import com.duzon.dbp.apimonitoring.dto.request.ReqApiErrDto;
import com.duzon.dbp.apimonitoring.service.ApiErrService;
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
 * ApiErrController
 */
@Api(tags = { "5. ApiErr" })
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
public class ApiErrController {

    @Autowired
    ApiErrService apiErrService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    ResponseService responseService;

    @ApiOperation(value = "API Err List", notes = "API Err 리스트")
    @GetMapping("/apierr")
    public _List<Map<String, Object>> apiErrList(){
        return responseService.getResult_List_Map(apiErrService.apiErrList());
    }

    @ApiOperation(value = "API Err Get", notes = "API Err 단건")
    @GetMapping("/apierr/{idx}")
    public _Single<Map<String, Object>> apiErrGet(@ApiParam(value = "API Err 번호", required = true) @PathVariable Long idx){
        return responseService.getResult_Single_Map(apiErrService.apiErrGet(idx));
    }

    @ApiOperation(value = "API Err List", notes = "API Err Status 리스트 'F'")
    @GetMapping("/apierr/status")
    public _List<Map<String, Object>> findByStatusList(){
        return responseService.getResult_List_Map(apiErrService.findByStatusList());
    }

    @ApiOperation(value = "API Err Create", notes = "API Err 생성")
    @PostMapping("/apierr")
    public Common apiErrCreate(@ApiParam(value = "API Err 생성 객체", required = true) @RequestBody @Valid ReqApiErrDto Dto){
        ApiErrDto a = apiErrService.apiErrCreate(Dto);
        notificationService.ErrNoti(a);
        return responseService.getResultSuccess();
    }
    
    @ApiOperation(value = "API Err Update", notes = "API Err 수정")
    @PatchMapping("/apierr/{idx}")
    public Common apiErrUpdate(
        @ApiParam(value = "API Err 번호", required = true)@PathVariable Long idx,
        @ApiParam(value = "API Err 수정 객체", required = true) @RequestBody ApiErrDto Dto
        ){
        apiErrService.apiErrUpdate(idx, Dto);
        return responseService.getResultSuccess();
    }
}