package com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.check;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

@Data
public class UrlRequest {

    private Long groupNo;
    
    private Long serviceCategoryNo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String swaggerUrl;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private JsonNode swaggerFile;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String importType;

}