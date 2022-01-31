package com.duzon.dbp.apimonitoring.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * EmailDto
 */
@Getter
@Setter
@ToString
public class ReqEmailDto {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_.-]{3,13}[a-zA-Z0-9]$")
    private String id;

    @NotBlank
    @Email
    private String mail;

    @NotBlank
    private String name;
}