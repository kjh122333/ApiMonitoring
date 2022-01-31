package com.duzon.dbp.apimonitoring.dto.message;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Common
 */
@Getter
@Setter
public class Common {

    @ApiModelProperty(value = "응답 성공여부 : TRUE/FALSE")
    private boolean success;

    @ApiModelProperty(value = "응답 코드번호 : > 0 정상, < 0 비정상")
    private int code;

    @ApiModelProperty(value = "응답 메시지")
    private String message;
}