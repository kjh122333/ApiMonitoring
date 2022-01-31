package com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.swagger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tags {

    private long id;
    private String name;
    private String description;
}