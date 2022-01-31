package com.duzon.dbp.apimonitoring.swagger_api_docs.service;

import java.util.List;

import com.duzon.dbp.apimonitoring.dto.ServiceCategoryDto;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MapperServiceCategory {

	@Select("select * from t_service_category")
	List<ServiceCategoryDto> findAll();
	

	@Select("select category_name_kr from t_service_category where service_category_no = #{service_category_no}")
	String getCategoryNameKr(Long service_category_no);

	

	@Insert("insert into t_service_category(category_name_kr, insert_timestamp, is_deleted, updated_timestamp) values (#{category_name_kr}, #{insert_timestamp}, #{is_deleted}, #{updated_timestamp})")
	@Options(useGeneratedKeys=true, keyProperty="service_category_no")
	int save(ServiceCategoryDto serviceCategoryDto);
}