/*
 * @Author: junghwan.kong 
 * @Date: 2019-12-12 20:29:20 
 * @Last Modified by: junghwan.kong
 * @Last Modified time: 2019-12-14 22:26:43
 */
package com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.swagger.definitions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

/**
 * SpecOfObject
 */
@Data
public class SpecOfObject_dynamic {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String type;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private JsonNode properties;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private JsonNode additionalProperties;

}
// @JsonInclude(JsonInclude.Include.NON_NULL)
// private String type;

// @JsonInclude(JsonInclude.Include.NON_NULL)
// private Map<String, JsonNode> properties = new LinkedHashMap<String, JsonNode>();

// @JsonInclude(JsonInclude.Include.NON_NULL)
// private Map<String, JsonNode> additionalProperties = new LinkedHashMap<String, JsonNode>();

// @JsonAnySetter
// @JsonInclude(JsonInclude.Include.NON_NULL)
// void setProperties(String key, JsonNode value) {
//     properties.put(key, value);
// }

// @JsonAnySetter
// @JsonInclude(JsonInclude.Include.NON_NULL)
// void setAdditionalProperties(String key, JsonNode value) {
//     additionalProperties.put(key, value);
// }
