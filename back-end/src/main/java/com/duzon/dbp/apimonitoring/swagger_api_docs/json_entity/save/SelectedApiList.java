package com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.save;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class SelectedApiList {
    private String path;
    private String method;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String employee;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String employeeSub;

}