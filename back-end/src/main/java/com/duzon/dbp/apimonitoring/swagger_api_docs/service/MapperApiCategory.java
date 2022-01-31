package com.duzon.dbp.apimonitoring.swagger_api_docs.service;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.duzon.dbp.apimonitoring.dto.ApiCategoryDto;

@Mapper
public interface MapperApiCategory {
    
    @Select(" select EXISTS (select * from t_service where service_url=#{service_url}  AND is_deleted = 'F' ) ) as success ")
    boolean IsServiceSaved(String service_url);
    
    @Select("select service_no from t_service where service_url= #{service_url} AND is_deleted = 'F'")
    Long getServiceNo(String service_url);
    
    @Select("select service_name_kr from t_service where service_url LIKE CONCAT('%', #{service_url} AND is_deleted = 'F' )")
    String getServiceNameKr(String service_url);

    @Select(" SELECT EXISTS (SELECT api_category_name_kr  FROM t_api_category WHERE api_category_name_kr = #{api_category_name_kr}  AND is_deleted = 'F' ) AS success ")
    boolean isSameApiCategoryNameKr(String api_category_name_kr);

    @Select("SELECT EXISTS (SELECT api_category_name_kr FROM t_api_category WHERE api_category_name_kr = #{api_category_name_kr}  AND is_deleted = 'F' ) AS success")
    boolean checkIsOverlab(String api_category_name_kr);
    
    @Insert("insert into t_api_category (insert_timestamp, is_deleted, api_category_name_kr, updated_timestamp, service_no) values (#{insert_timestamp}, #{is_deleted}, #{api_category_name_kr}, #{updated_timestamp}, #{service_no})")
    @Options(useGeneratedKeys = true, keyProperty = "api_category_no")
    int save(ApiCategoryDto apiCategoryDto);
}