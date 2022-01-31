package com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.swagger.paths.httpmethod;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiJson {

    private String[] tags;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String summary;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;
    private String operationId;
    private JsonNode consumes;
    private JsonNode produces;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private JsonNode[] parameters;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private JsonNode responses;
}