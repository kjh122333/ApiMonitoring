package com.duzon.dbp.apimonitoring.config.mapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * Mapper
 */
@Mapper
public interface ListMapper {
    // @Select("SELECT a.service_api_no,a.api_url, a.content_type, a.delay_status, a.employee_no, a.err_status, a.insert_timestamp, a.is_deleted, a.method, a.param, a.response_list, a.description, a.service_no, a.updated_timestamp, a.employee_sub_no, a.employee_name, b.employee_sub_name, a.group_name, t_service.service_name_kr as service_name from ( select t_service_api.*, employee.name as employee_name, employee.group_name from t_service_api JOIN employee on t_service_api.employee_no = employee.employee_no ) a join ( SELECT t_service_api.service_api_no, t_service_api.employee_sub_no, employee.name as employee_sub_name from employee JOIN t_service_api on employee.employee_no = t_service_api.employee_sub_no ) b on a.service_api_no = b.service_api_no JOIN t_service on a.service_no = t_service.service_no")
    // public List<dto> findList();
}