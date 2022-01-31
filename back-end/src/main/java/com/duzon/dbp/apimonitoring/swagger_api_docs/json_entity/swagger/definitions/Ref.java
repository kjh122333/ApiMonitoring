/*
 * @Author: junghwan.kong 
 * @Date: 2019-12-15 12:54:21 
 * @Last Modified by: junghwan.kong
 * @Last Modified time: 2019-12-15 19:07:57
 */
package com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.swagger.definitions;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ref {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, JsonNode> ref = new LinkedHashMap<String, JsonNode>();

    @JsonAnySetter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    void setRef(String key, JsonNode value) {
        ref.put(key, value);
    }

}
 