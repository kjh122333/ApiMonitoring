package com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.swagger.paths.httpmethod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Reponses
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetailReponses {
    
    private String description;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private JsonNode schema;
}