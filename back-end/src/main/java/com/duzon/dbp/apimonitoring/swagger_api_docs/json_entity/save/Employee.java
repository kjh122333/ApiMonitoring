package com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.save;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Employee {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long employee_no;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long group_no;
    
    private String name;
    private String group_name;

}