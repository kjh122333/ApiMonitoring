/*
 * @Author: junghwan.kong 
 * @Date: 2019-12-04 20:27:58 
 * @Last Modified by: junghwan.kong
 * @Last Modified time: 2019-12-07 11:20:25
 */

package com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.overlap;

import lombok.Data;

@Data
public class CheckOverlapApi {

    private String api_url;
    private String method;
    private Long api_category_no;
    private String is_deleted;

}