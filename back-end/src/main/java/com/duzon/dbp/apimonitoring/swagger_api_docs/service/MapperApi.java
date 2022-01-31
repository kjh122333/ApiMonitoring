package com.duzon.dbp.apimonitoring.swagger_api_docs.service;

import com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.ApiDtoSwagger;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MapperApi {

    @Select("SELECT EXISTS (SELECT api_url, method FROM t_api WHERE api_url = #{api_url} AND method = #{method} AND is_deleted = 'F') AS success")
    boolean checkIsOverlab(String api_url, String method);

    @Select(" select api_category_no from t_api_category WHERE api_category_name_kr = #{api_category_name_kr} AND is_deleted = 'F' AND service_no = (select service_no from t_service where is_deleted = 'F' AND service_url=#{api_url})")
    Long getApiCategoryNo(String api_category_name_kr, String api_url);

    @Select(" select api_category_name_kr from t_api_category WHERE api_category_no = #{api_category_no} AND is_deleted = 'F' ")
    String getApiCategoryName(Long api_category_no);

    @Select(" SELECT EXISTS (SELECT api_category_name_kr  FROM t_api_category WHERE api_category_name_kr = #{api_category_name_kr} AND is_deleted = 'F' ) AS success ")
    boolean isSameApiCategoryNameKr(String api_category_name_kr);

    @Insert("insert into t_api (api_url,parameter_type,response_type,delay_status,employee_no,err_status,insert_timestamp,is_deleted,method,param,response_list,description,api_category_no,updated_timestamp,employee_sub_no, update_employee_no) values (#{api_url},#{parameter_type},#{response_type},#{delay_status},#{employee_no},#{err_status},#{insert_timestamp},#{is_deleted},#{method},#{param},#{response_list},#{description},#{api_category_no},#{updated_timestamp},#{employee_sub_no}, #{update_employee_no})")
    int save(ApiDtoSwagger apiDtoSwagger);


    @Select("SELECT EXISTS (select api_url, method from t_api where  api_url = #{api_url} AND method =#{method} AND is_deleted = 'F' ) as  success;") 
    boolean doUpdate(String api_url, String method);
}
