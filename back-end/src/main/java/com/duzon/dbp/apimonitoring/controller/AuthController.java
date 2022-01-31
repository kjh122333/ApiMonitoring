package com.duzon.dbp.apimonitoring.controller;

import java.io.IOException;
import java.util.Map;

import javax.mail.MessagingException;
import javax.validation.Valid;

import com.duzon.dbp.apimonitoring.dto.message.Common;
import com.duzon.dbp.apimonitoring.dto.message._Single;
import com.duzon.dbp.apimonitoring.dto.request.ReqCertificationDto;
import com.duzon.dbp.apimonitoring.dto.request.ReqEmailDto;
import com.duzon.dbp.apimonitoring.service.EmployeeService;
import com.duzon.dbp.apimonitoring.service.mail.EmailService;
import com.duzon.dbp.apimonitoring.service.response.ResponseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * AuthController
 */
@Api(tags = { "9. Auth" })
@CrossOrigin(origins = "*")
@Validated
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    EmailService emailService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    ResponseService responseService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @ApiOperation(value = "Signin", notes = "로그인")
    @PostMapping(value = "/signin")
    public _Single<Map<String, String>> Signin(@ApiParam(value = "ID, Password", required = true) @RequestBody @Valid ReqCertificationDto dto) {
        return responseService.getResult_Single(employeeService.Signin(dto));
    }

    @ApiOperation(value = "Email Auth", notes = "이메일 인증 후 비밀번호 수정")
    @PostMapping(value = "/email")
    public Common mail(@ApiParam(value = "Id, Mail, Name", required = true) @RequestBody(required = true) @Valid ReqEmailDto dto) {
        try {
            emailService.sendEmail(dto);
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
        return responseService.getResultSuccess();
    }
}