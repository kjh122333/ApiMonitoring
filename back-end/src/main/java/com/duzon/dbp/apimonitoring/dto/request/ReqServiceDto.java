package com.duzon.dbp.apimonitoring.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.vladmihalcea.hibernate.type.json.JsonStringType;

import org.hibernate.annotations.TypeDef;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ReqServiceDto
 */
@Getter
@Setter
@ToString
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class ReqServiceDto {

    @NotNull
    private Long service_category_no;

    @NotBlank
    private String service_name_kr;

    @NotBlank
    private String service_url;

    @NotNull
    private Long group_no;

    private Long service_state = (long) 1;

    private String service_code;
}