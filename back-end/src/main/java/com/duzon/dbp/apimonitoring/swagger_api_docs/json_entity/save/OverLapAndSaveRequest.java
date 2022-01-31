package com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.save;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

@Data
public class OverLapAndSaveRequest {

    private Long groupNo;
    private Long serviceCategoryNo;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String swaggerUrl;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private JsonNode swaggerFile;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String importType;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String updateEmployeeId;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String apiCategoryName;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Employee employee;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Employee employeeSub;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SelectedApiList[] selectedApiList;






}
