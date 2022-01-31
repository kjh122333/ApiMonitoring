package com.duzon.dbp.apimonitoring.swagger_api_docs.service;

import com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.ServiceDtoSwagger;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MapperService {

	@Select("select EXISTS (select service_url from t_service where service_url = #{service_url}  AND is_deleted = 'F'  ) as success")
	boolean checkIsOverlab(String service_url);

	@Insert("insert into t_service (service_name_kr, service_category_no, insert_timestamp, updated_timestamp, is_deleted, service_state, group_no, service_url, definitions, service_code) values (#{service_name_kr}, #{service_category_no}, #{insert_timestamp}, #{updated_timestamp}, #{is_deleted}, #{service_state}, #{group_no}, #{service_url}, #{definitions}, #{service_code})")
	@Options(useGeneratedKeys = true, keyProperty = "service_no")
	int save(ServiceDtoSwagger serviceDto);


	@Select("select group_name from e_group where group_no =#{group_no};")
	String getGroupName(Long group_no);
	
	@Select("select category_name_kr from t_service_category where service_category_no =#{service_category_no}")
	String getServiceCategoryName(Long service_category_no);

	@Select("select group_no from t_service where is_deleted = 'F' AND service_url =#{service_url};")
	Long getGroupNoFromService(String service_url);

	@Select("select service_category_no from t_service where is_deleted = 'F' AND service_url =#{service_url};")
	Long getServiceCategoryNoFromService(String service_url);

}