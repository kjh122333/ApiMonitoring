package com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.swagger.paths;

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
public class DynamicPath {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, Object> methods = new LinkedHashMap<>();

    @JsonAnySetter
    void setDynamicPath(String key, Object value) {
        methods.put(key, value);
    }
}