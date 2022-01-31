package com.duzon.dbp.apimonitoring.dto.request;

import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.vladmihalcea.hibernate.type.json.JsonStringType;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ApiUpdateDto
 */
@Getter
@Setter
@ToString
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class ReqApiDto {

    private Long api_category_no;

    @NotBlank
    private String api_url;

    @NotBlank
    private String method;

    @NotBlank
    private String description;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_.-]{3,13}[a-zA-Z0-9]$")
    private String update_employee_id;
    @NotNull
    private Long employee_no;
    @NotNull
    private Long employee_sub_no;

    private String is_deleted = "F";
    private String err_status = "F";
    private String delay_status = "F";

    private String parameter_type;
    private String response_type;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private List<Map<String, Object>> param;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private Map<String, Object> response_list;
}