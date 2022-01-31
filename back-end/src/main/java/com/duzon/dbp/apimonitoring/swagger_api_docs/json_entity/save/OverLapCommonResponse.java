package com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.save;

import java.util.Vector;

import com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.overlap.CheckOverlapApi;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class OverLapCommonResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Vector<CheckOverlapApi> isExpectedCreate;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Vector<CheckOverlapApi> isExpectedDelete;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Vector<CheckOverlapApi> isExpectedUpdate;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean isOverlap = false;

}