/*
 * @Author: junghwan.kong 
 * @Date: 2019-12-03 17:15:12 
 * @Last Modified by: junghwan.kong
 * @Last Modified time: 2019-12-10 15:12:58
 */
package com.duzon.dbp.apimonitoring.swagger_api_docs.service;

import java.time.LocalDateTime;
import java.util.Vector;

import com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.overlap.CheckOverlapApi;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MapperSave {
  // SECTION COMMON
  @Select("SELECT EXISTS (select * from t_service where service_url=#{service_url} and group_no =#{group_no}) as success")
  boolean isSameServiceByGroupNo(String service_url, Long group_no);

  @Select("SELECT EXISTS (select * from t_service where service_url=#{service_url} and service_category_no =#{service_category_no}) as success")
  boolean isSameServiceByServiceCategoryNo(String service_url, Long service_category_no);

  // SECTION SERVICE
  @Select("SELECT EXISTS (select * from t_service where service_url=#{service_url} and is_deleted = 'F') as success")
  boolean isExistService(String service_url);

  // SECTION API_CATEGORY
  @Select("SELECT EXISTS (select * from t_api_category where  t_api_category.service_no = (select t_service.service_no from t_service where t_service.service_url =#{service_url} and t_service.is_deleted = 'F') and t_api_category.api_category_name_kr =#{api_category_name_kr} t_api_category.is_deleted = 'F') as success")
  boolean isExistApiCategory(String service_url, String api_category_name_kr);

  // SECTION API
  @Select("SELECT EXISTS (select * from t_api where api_category_no =  (select api_category_no from t_api_category where api_category_name_kr =#{api_category_name_kr} and service_no = (select service_no from t_service where service_url =#{service_url})) and method =#{method} and api_url=#{api_url} and is_deleted = 'F')as success")
  boolean isExistApi(String service_url, String api_category_name_kr, String api_url, String method);


  // 요청에대해 URL이 저장되어있으면 (+ 삭제된게 아니면 ) 입력된 그룹넘버, 서비스카테고리넘버가 일치하는지 체크후 FALSE이면
  // group_no 반환
  @Select("SELECT group_no FROM t_service WHERE is_deleted = 'F' AND service_url =#{service_url} AND (( IF(group_no =#{group_no}, 't','f') = 'f') OR (IF(service_category_no =#{service_category_no}, 't','f')='f'))")
  Long isProperGroupNo(String service_url, Long group_no, Long service_category_no);

  // 요청에대해 URL이 저장되어있으면 (+ 삭제된게 아니면 ) 입력된 그룹넘버, 서비스카테고리넘버가 일치하는지 체크후 FALSE이면
  // service_category_no 반환
  @Select("SELECT service_category_no FROM t_service WHERE is_deleted = 'F' AND service_url =#{service_url} AND (( IF(group_no =#{group_no}, 't','f') = 'f') OR (IF(service_category_no =#{service_category_no}, 't','f')='f'))")
  Long isProperServiceCategoryNo(String service_url, Long group_no, Long service_category_no);

  @Select("select exists ( select service_url from t_service where service_url =#{service_url} and is_deleted = 'F' ) as success")
  boolean isAlreadySavedService(String service_url);

  @Select("select exists (select * from t_api_category where t_api_category.api_category_name_kr =#{api_category_name_kr} and t_api_category.is_deleted = 'F' and t_api_category.service_no = (select t_service.service_no from t_service where t_service.service_url =#{service_url} and t_service.is_deleted='F')) as success")
  boolean isAlreadySavedApiCategory(String service_url, String api_category_name_kr);

  @Select("select api_category_name_kr from t_api_category where t_api_category.is_deleted = 'F' and t_api_category.service_no = (select t_service.service_no from t_service where t_service.service_url =#{service_url} and t_service.is_deleted='F')")
  Vector<String> totalApiCategory(String service_url);

  @Update("UPDATE t_service SET t_service.definitions=#{definitions}, t_service.updated_timestamp=#{updated_timestamp}, t_service.service_name_kr=#{service_name_kr}, service_code=#{service_code} WHERE service_url=#{service_url} AND is_deleted='F'")
  void updateService(LocalDateTime updated_timestamp, String service_name_kr, String service_url, String definitions, String service_code);

  @Update(" UPDATE t_api_category SET is_deleted='T', updated_timestamp =#{updated_timestamp} WHERE api_category_name_kr=#{api_category_name_kr} AND is_deleted='F'")
  void deleteApiCategory(String api_category_name_kr, LocalDateTime updated_timestamp);

  @Select("select exists( select * from t_api where  t_api.api_category_no = (select api_category_no from t_api_category where api_category_name_kr=#{api_category_name_kr} and is_deleted = 'T')) as success")
  boolean checkApiOfDeletedCategory(String api_category_name_kr);

  @Update("UPDATE t_api SET is_deleted='T', updated_timestamp =#{updated_timestamp} , update_employee_no= (select employee_no from employee  where id =#{updateEmployeeName}) WHERE api_category_no = (select api_category_no from t_api_category where api_category_name_kr=#{api_category_name_kr} and is_deleted = 'T') AND is_deleted='F'")
  void deleteApi(String api_category_name_kr, LocalDateTime updated_timestamp, String updateEmployeeName);

  @Select("select t_api.api_url, t_api.method, t_api.api_category_no, t_api.is_deleted from t_api where is_deleted = 'F' AND t_api.api_category_no in (select t_api_category.api_category_no from t_api_category where is_deleted = 'F' AND t_api_category.service_no = (select t_service.service_no from t_service where is_deleted = 'F' AND t_service.service_url =#{service_url}))")
  Vector<CheckOverlapApi> totalApi(String service_url);

  @Select("select t_api.api_url, t_api.method, t_api.api_category_no, t_api.is_deleted from t_api where is_deleted = 'F' AND t_api.api_category_no = (select t_api_category.api_category_no from t_api_category where api_category_name_kr =#{api_category_name_kr} AND is_deleted = 'F' AND t_api_category.service_no = (select t_service.service_no from t_service where is_deleted='F' AND t_service.service_url =#{service_url}))")
  Vector<CheckOverlapApi> onlyApiOfCategory(String service_url, String api_category_name_kr);

  @Select("select exists ( select * from t_api where api_url =#{api_url} AND method =#{method} AND is_deleted = 'F' AND api_category_no = (select api_category_no from t_api_category where is_deleted = 'F' AND api_category_name_kr =#{api_category_name_kr})) as success")
  boolean checkMethodAndPath(String api_url, String method, String api_category_name_kr);

  @Select("select employee_no from employee where id =#{name}")
  Long getEmployeeNo(String name);

  @Update("UPDATE t_api SET is_deleted = 'T', updated_timestamp=#{updated_timestamp}, update_employee_no=#{update_employee_no} WHERE api_url =#{api_url} AND method =#{method} AND api_category_no =#{api_category_no} AND is_deleted ='F'")
  void deleteOnlyApi(LocalDateTime updated_timestamp, Long update_employee_no, String api_url, String method, Long api_category_no);

}


/*
select t_api.api_url, t_api.method, t_api.api_category_no, t_api.is_deleted 
from t_api 
where is_deleted = 'F' 
AND t_api.api_category_no in (
  
select t_api_category.api_category_no 
from t_api_category 
where is_deleted = 'F' 
AND t_api_category.service_no = 301
  
select t_service.service_no 
from t_service 
where is_deleted = 'F' AND t_service.service_url ="http://15.165.25.145:9500/"))
 */