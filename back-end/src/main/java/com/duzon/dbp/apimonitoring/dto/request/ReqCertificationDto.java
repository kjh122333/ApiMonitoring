package com.duzon.dbp.apimonitoring.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * SelfCertificationDto
 */
@Getter
@Setter
@ToString
public class ReqCertificationDto {

    @NotBlank(message = "Id를 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_.-]{3,13}[a-zA-Z0-9]$")
    private String id;

    @NotBlank(message = "Password를 입력해주세요.")
    @Pattern(regexp = "^.*(?=^.{8,20}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$")
    private String password;
}