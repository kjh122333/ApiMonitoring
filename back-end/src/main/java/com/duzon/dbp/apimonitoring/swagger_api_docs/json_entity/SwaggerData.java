package com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SwaggerData {

    
    private String swagger;

    private JsonNode info;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String host;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String basePath;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private JsonNode tags;
    
    private JsonNode paths;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private JsonNode definitions;
}