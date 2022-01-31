package com.duzon.dbp.apimonitoring.controller;

import java.util.Map;

import com.duzon.dbp.apimonitoring.dto.message.Common;
import com.duzon.dbp.apimonitoring.dto.message._List;
import com.duzon.dbp.apimonitoring.dto.message._Single;
import com.duzon.dbp.apimonitoring.dto.socket.NotificationDto;
import com.duzon.dbp.apimonitoring.service.NotificationService;
import com.duzon.dbp.apimonitoring.service.response.ResponseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * NotificationController
 */
@Api(tags = { "10. Notification" })
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @Autowired
    ResponseService responseService;

    @ApiOperation(value = "Notification List", notes = "Notification 리스트")
    @GetMapping("/notification/list/{employee_no}")
    public _List<Map<String, Object>> NotificationGetList(@PathVariable long employee_no){
        return responseService.getResult_List_Map(notificationService.NotificationGetList(employee_no));
    }

    @ApiOperation(value = "Notification Get", notes = "Notification 단건")
    @GetMapping("/notification/{notify_no}")
    public _Single<NotificationDto> NotificationGet(@PathVariable long notify_no){
        return responseService.getResult_Single(notificationService.NotificationGet(notify_no));
    }

    @ApiOperation(value = "Notification Delete", notes = "Notification 단건 삭제")
    @DeleteMapping("/notification/{notify_no}")
    public Common NotificationDelete(@PathVariable long notify_no){
        notificationService.NotificationDelete(notify_no);
        return responseService.getResultSuccess();
    }
}