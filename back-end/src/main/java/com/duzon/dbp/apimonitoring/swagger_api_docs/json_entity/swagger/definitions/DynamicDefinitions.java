package com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.swagger.definitions;

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
public class DynamicDefinitions {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, Object> dynamicDefinitions = new LinkedHashMap<>();

    @JsonAnySetter
    void setDynamicDefinitions(String key, Object value) {
        dynamicDefinitions.put(key, value);
    }

}
 