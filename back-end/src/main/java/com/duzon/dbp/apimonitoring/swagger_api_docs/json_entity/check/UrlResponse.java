package com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.check;

import com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.SwaggerData;

import lombok.Data;

@Data
public class UrlResponse {

    private SwaggerData swaggerData;
    private boolean swaggerCheck;
    private boolean sameCheck;
    private String properGroupName;
    private String properServiceCategoryName;

}