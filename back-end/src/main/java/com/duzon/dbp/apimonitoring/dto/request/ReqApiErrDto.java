package com.duzon.dbp.apimonitoring.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ReqApiErrDto
 */
@ToString
@Getter
@Setter
public class ReqApiErrDto {

    private Long api_no;

    @NotNull
    private Long api_err_code;

    @NotBlank
    private String api_err_msg;
    
    private String url;
    
    @Pattern(regexp = "get|post|patch|put|delete|GET|POST|PATCH|PUT|DELETE")
    private String method;

    private String jira_url;
    private String kibana_url;
    private String ref;
}