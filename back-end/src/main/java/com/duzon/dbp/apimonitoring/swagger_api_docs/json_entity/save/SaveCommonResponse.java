package com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.save;

import lombok.Data;

@Data
public class SaveCommonResponse {

    private boolean saveSuccess = false;
    private String properGroupName;
    private String properServiceCategoryName;
    
}