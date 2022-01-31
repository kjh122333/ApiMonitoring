package com.duzon.dbp.apimonitoring.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * UpdateEmployeeDto
 */
@Getter
@Setter
@ToString
public class ReqEmployeeUpdateDto {

    @Pattern(regexp = "^.*(?=^.{8,20}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$")
    private String password;

    @NotBlank
    private String name;

    @NotNull
    private Long group_no;

    @NotBlank
    @Email
    private String mail;

    @NotBlank
    private String employee_contact;
}