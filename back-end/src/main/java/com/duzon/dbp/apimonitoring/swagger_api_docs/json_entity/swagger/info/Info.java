package com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.swagger.info;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

@Data
public class Info {

    private String description;
    private String version;
    private String title;
    private String termsOfService;
    private JsonNode license;

}