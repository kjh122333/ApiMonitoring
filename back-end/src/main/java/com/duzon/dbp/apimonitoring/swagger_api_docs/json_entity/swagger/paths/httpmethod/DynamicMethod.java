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
public class DynamicMethod {

    private Map<String, Object> dynamicMethod = new LinkedHashMap<>();

    @JsonAnySetter
    void setDynamicMethod(String key, Object value) {
        dynamicMethod.put(key, value);
    }

}