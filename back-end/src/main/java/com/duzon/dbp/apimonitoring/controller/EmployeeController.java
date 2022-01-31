package com.duzon.dbp.apimonitoring.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.validation.Valid;

import com.duzon.dbp.apimonitoring.dto.message.Common;
import com.duzon.dbp.apimonitoring.dto.message._List;
import com.duzon.dbp.apimonitoring.dto.message._Single;
import com.duzon.dbp.apimonitoring.dto.request.ReqCertificationDto;
import com.duzon.dbp.apimonitoring.dto.request.ReqEmployeeUpdateDto;
import com.duzon.dbp.apimonitoring.service.EmployeeService;
import com.duzon.dbp.apimonitoring.service.mail.EmailService;
import com.duzon.dbp.apimonitoring.service.response.ResponseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * EmployeeController
 */
@Api(tags = { "7. Employee" })
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
public class EmployeeController {
 
    @Autowired
    EmployeeService employeeService;

    @Autowired
    EmailService emailService;

    @Autowired
    ResponseService responseService;

    @ApiOperation(value = "Employee Get", notes = "Employee 단건")
    @GetMapping(value = "/employee/{idx}")
    public _Single<Map<String, Object>> employeeGet(@ApiParam(value = "Employee Id", required = true) @PathVariable String idx) {
        return responseService.getResult_Single_Map(employeeService.employeeGet(idx));
    }

    @ApiOperation(value = "Employee Get 수정", notes = "Employee 단건 수정")
    @PatchMapping(value = "/employee/{idx}")
    public Common employeeGetUpdate(
        @ApiParam(value = "Employee 번호", required = true) @PathVariable String idx,
        @ApiParam(value = "Employee 수정 객체", required = true) @RequestBody @Valid ReqEmployeeUpdateDto dto
        ) {
        employeeService.employeeGetUpdate(idx, dto);
        return responseService.getResultSuccess();
    }

    @ApiOperation(value = "Email Employee Password 수정", notes = "이메일 인증 후 비밀 번호 수정")
    @PatchMapping(value = "/employee/email/{idx}")
    public Common employeePasswordUpdate(
        @ApiParam(value = "Employee 번호", required = true) @PathVariable String idx,
        @ApiParam(value = "Employee 수정 Password", required = true) @RequestBody Map<String, String> password
        ) {
        employeeService.employeePasswordUpdate(idx, password.get("password"));
        return responseService.getResultSuccess();
    }

    @ApiOperation(value = "Employee List", notes = "Employee 리스트")
    @GetMapping(value = "/employee")
    public _List<Map<String, Object>> employeeList() {
        return responseService.getResult_List_Map(employeeService.findEmployeeList());
    }
    
    @ApiOperation(value = "Employee List data", notes = "Employee 사용자 정보만 리스트")
    @GetMapping(value = "/employee/data")
    public _List<Map<String, Object>> employeeDataList() {
        return responseService.getResult_List(employeeService.employeeDataList());
    }

    @ApiOperation(value = "Employee Columns Config", notes = "Employee 컬럼 설정")
    @PatchMapping(value = "/columnsconfig/{idx}")
    public Common columnsconfig(
        @ApiParam(value = "Employee 아이디", required = true) @PathVariable String idx,
        @ApiParam(value = "Columns Config 설정 내용", required = true) @RequestBody Map<String, Object> config
        ) {
        employeeService.ColumnsConfigSave(idx, config);
        return responseService.getResultSuccess();
    }

    @ApiOperation(value = "Check Auth", notes = "회원 등급 체크")
    @PostMapping(value = "/check")
    public _Single<String> Chcek(@ApiParam(value = "로그인 한 아이디", required = true) @RequestBody Map<String, String> id) {
        return responseService.getResult_Single(employeeService.Check(id.get("id")));
    }

    
    @ApiOperation(value = "Self Certification", notes = "본인 확인")
    @PostMapping(value = "/self/certification")
    public Common SelfCertification(@ApiParam(value = "Id, Password", required = true) @Valid @RequestBody ReqCertificationDto dto) {
        employeeService.SelfCertification(dto);
        return responseService.getResultSuccess();
    }

    @ApiOperation(value = "First Login Email Auth", notes = "첫 로그인 시 이메일 인증")
    @PostMapping(value = "/firstlogin/email")
    public Common mail(
        @ApiParam(value = "Id", required = true) @RequestBody ReqCertificationDto dto,
        @ApiParam(value = "Token", required = true) @RequestHeader("X-AUTH-TOKEN") String token
        ) {
        try {
            emailService.FirstLoginEmail(dto, token);
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
        return responseService.getResultSuccess();
    }

    @ApiOperation(value = "Employee Err, Delay Info Count", notes = "Employee 담당 API의 Err, Delay Info 개수 ")
    @GetMapping(value = "/employee/infocount/{idx}")
    public _Single<Map<String, Object>> employeeInfoCount(@ApiParam(value = "Employee Id", required = true) @PathVariable String idx) {
        return responseService.getResult_Single_Map(employeeService.employeeInfoCount(idx));
    }

    @ApiOperation(value = "Employee Err, Delay Info List", notes = "Employee 담당 API의 Err, Delay Info 리스트")
    @GetMapping(value = "/employee/info/{idx}")
    public _Single<Map<String, List<Map<String, Object>>>> employeeInfo(@ApiParam(value = "Employee Id", required = true) @PathVariable String idx) {
        return responseService.getResult_Single_MapMap(employeeService.employeeInfo(idx));
    }
}