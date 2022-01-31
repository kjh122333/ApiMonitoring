package com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.swagger.paths.httpmethod;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DynamicResponses {
 
    private Map<String, Object> dynamicResponses = new LinkedHashMap<String, Object>();
    
    @JsonAnySetter
    void setDynamicResponses(String key, Object value) {
        dynamicResponses.put(key, value);
    }

}