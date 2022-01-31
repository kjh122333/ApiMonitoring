package com.duzon.dbp.apimonitoring.swagger_api_docs.service;

import java.time.LocalDateTime;
import java.util.Vector;

import com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.overlap.CheckOverlapApi;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MapperOverlap {

    @Select("SELECT service_no FROM t_service WHERE service_url = #{service_url}  AND is_deleted = 'F' ")
    Long getServicNo(String service_url);

    

    @Select("SELECT EXISTS (select * from t_service where service_url=#{service_url} and is_deleted = 'F') as success")
    boolean isOverlapService(String service_url);

    @Select("SELECT EXISTS (SELECT * FROM t_api_category WHERE api_category_name_kr = #{api_category_name_kr} and is_deleted = 'F') AS SUCCESS")
    boolean isOverlapApiCategory(String api_category_name_kr);

    @Select("SELECT api_category_name_kr  FROM t_api_category WHERE api_category_name_kr = #{api_category_name_kr} AND is_deleted = 'F' AND service_no = (select service_no from t_service where is_deleted = 'F' AND service_url=#{service_url})")
    String getExpectedUpdate(String api_category_name_kr, String service_url);

    @Select("select t_api_category.api_category_name_kr from t_api_category where (t_api_category.service_no ="
            + "(SELECT service_no FROM t_service WHERE service_name_kr = #{service_name}  AND is_deleted = 'F' ))"
            + "and (api_category_name_kr not in (#{api_category_name_kr}))  AND is_deleted = 'F' ")
    String getExpectedDelete(String service_name, String api_category_name_kr);

    @Select("select api_category_name_kr from t_api_category where t_api_category.service_no = (select t_service.service_no from t_service where t_service.service_url = #{service_url} AND is_deleted = 'F' ) AND is_deleted = 'F' ")
    Vector<String> getExpectedUpdateAll(String service_url);

    // API 중복검사
    @Select("SELECT t_api_category.api_category_no FROM t_api_category WHERE t_api_category.service_no = (SELECT t_service.service_no FROM t_service WHERE t_service.service_url = #{serviceIsOverlap} AND is_deleted = 'F' ) AND is_deleted = 'F' ")
    Vector<Long> getApiCategoryNo(String serviceIsOverlap);
    

    @Select("select t_api.api_url , t_api.method from t_api where t_api.api_category_no = #{apiCategoryNo}  AND is_deleted = 'F' ")
    Vector<CheckOverlapApi> getApiUrlMethod(Long apiCategoryNo);

    @Select("select t_api.api_url , t_api.method from t_api where t_api.api_category_no = (select t_api_category.api_category_no from t_api_category where t_api_category.api_category_name_kr = #{api_category_name_kr} AND is_deleted = 'F' ) AND is_deleted = 'F' ")
    Vector<CheckOverlapApi> getApiUrlMethod2(String api_category_name_kr);

    /* -------------------------------- Api단위별 요청 ------------------------------- */
    @Select("SELECT EXISTS (select t_api.api_url, t_api.method, t_api.api_category_no from t_api where  t_api.api_url = #{apiUrl} AND t_api.method = #{method} AND t_api.api_category_no = (select t_api_category.api_category_no from t_api_category where t_api_category.api_category_name_kr= #{apiCategoryNameKr})  AND is_deleted = 'F' ) AS SUCCESS")
    boolean isOverlapApi(String apiUrl, String method, String apiCategoryNameKr);


    @Update("UPDATE t_api_category SET updated_timestamp=#{updated_timestamp} WHERE api_category_name_kr =#{apiCategoryUpdate} AND is_deleted='F'")
    void updateApiCategory(LocalDateTime updated_timestamp, String apiCategoryUpdate);

    @Update("UPDATE t_api_category SET is_deleted=#{is_deleted} WHERE api_category_name_kr = #{apiCategoryUpdate}  AND is_deleted = 'F' ")
    void deleteApiCategory(String is_deleted, String apiCategoryUpdate);

    @Update("UPDATE t_api SET update_employee_no=#{update_employee_no}, parameter_type=#{parameter_type},employee_no=#{employee_no},employee_sub_no=#{employee_sub_no},response_type=#{response_type},param=#{param},response_list=#{response_list},description=#{description},api_category_no=#{api_category_no},updated_timestamp=#{updated_timestamp} WHERE api_url=#{api_url} AND method=#{method} AND is_deleted='F'")
    void updateApi(Long update_employee_no, String parameter_type, Long employee_no, Long employee_sub_no, String response_type, String param, String response_list, String description, Long api_category_no, LocalDateTime updated_timestamp, String api_url, String method);
    
    @Update("UPDATE t_api SET is_deleted=#{is_deleted} WHERE api_url=#{api_url} AND method=#{method}  AND is_deleted = 'F' ")
    void deleteApi(String is_deleted, String api_url, String method);

}



 /*abstract
sssss

@Update("

UPDATE t_api_category 
SET 

    is_deleted=#{is_deleted} 
    
WHERE api_category_name_kr = #{apiCategoryUpdate}  AND is_deleted = 'F'

")


void deleteApiCategory(String is_deleted, String apiCategoryUpdate);

 */