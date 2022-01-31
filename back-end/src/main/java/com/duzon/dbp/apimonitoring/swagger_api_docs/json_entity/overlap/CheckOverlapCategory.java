/*
 * @Author: junghwan.kong 
 * @Date: 2019-12-04 20:27:47 
 * @Last Modified by: junghwan.kong
 * @Last Modified time: 2019-12-04 20:28:24
 */

package com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.overlap;

import lombok.Data;

@Data
public class CheckOverlapCategory {

    private String api_category_name_kr;
    private boolean is_deleted;
    
}