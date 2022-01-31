/*
 * @Author: junghwan.kong 
 * @Date: 2019-12-14 22:14:21 
 * @Last Modified by: junghwan.kong
 * @Last Modified time: 2019-12-15 13:24:18
 */
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
public class Types_dynamic {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, Object> dynamicTypes = new LinkedHashMap<String, Object>();

    @JsonAnySetter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    void setDynamicTypes(String key, Object value) {
        dynamicTypes.put(key, value);
    }

}
 