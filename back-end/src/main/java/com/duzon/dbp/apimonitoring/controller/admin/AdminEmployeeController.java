package com.duzon.dbp.apimonitoring.controller.admin;

import java.util.List;

import com.duzon.dbp.apimonitoring.dto.Employee;
import com.duzon.dbp.apimonitoring.dto.message.Common;
import com.duzon.dbp.apimonitoring.dto.message._List;
import com.duzon.dbp.apimonitoring.service.EmployeeService;
import com.duzon.dbp.apimonitoring.service.response.ResponseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * EmployeeController
 */
@Api(tags = { "0.2 Admin Employee" })
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin")
public class AdminEmployeeController {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    ResponseService responseService;
    
    // employee 엑셀 회원가입
    @ApiOperation(value = "Signup", notes = "회원가입")
    @PostMapping(value = "/signup/import")
    public Common Signupimport(@ApiParam(value = "아이디", required = true) @RequestBody List<Employee> employee) {
        employeeService.SaveAll(employee);
        return responseService.getResultSuccess();
    }

    // employee 단체 아이디 중복 확인
    @ApiOperation(value = "Employee List Id Check", notes = "Employee 리스트 Id 중복 확인")
    @PostMapping(value = "/employee/listcheck")
    public _List<String> employeeListIdCheck(@ApiParam(value = "Employee 번호", required = true) @RequestBody List<String> idx
        ) {
        return responseService.getResult_List_String(employeeService.employeeListIdCheck(idx));
    }
    
    // employee 단체 삭제
    @ApiOperation(value = "Employee isDelete All 'T'", notes = "Employee isDelete 모두 'T'")
    @PatchMapping(value = "/employee")
    public Common employeeisDeleteAllT(@ApiParam(value = "Employee 번호", required = true) @RequestBody List<Long> idx
        ) {
        employeeService.isDeleteAllT(idx);
        return responseService.getResultSuccess();
    }
}