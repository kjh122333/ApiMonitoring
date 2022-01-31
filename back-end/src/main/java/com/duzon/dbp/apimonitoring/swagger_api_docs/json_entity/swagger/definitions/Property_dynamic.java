/*
 * @Author: junghwan.kong 
 * @Date: 2019-12-12 20:19:55 
 * @Last Modified by: junghwan.kong
 * @Last Modified time: 2019-12-14 22:21:18
 */
package com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.swagger.definitions;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

/**
 * Property
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Property_dynamic {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, JsonNode> dynamicProperty = new LinkedHashMap<String, JsonNode>();

    @JsonAnySetter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    void setDynamicProperty(String key, JsonNode value) {
        dynamicProperty.put(key, value);
    }


    /**
     * Map<String, Property> PropertyMap = new LinkedHashMap<String, Property>();
     */
}