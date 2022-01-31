package com.duzon.dbp.apimonitoring.repo;

import java.util.List;
import java.util.Map;

import com.duzon.dbp.apimonitoring.dto.ApiErrDto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * ServiceApiErrRepo
 */
@Repository
public interface ApiErrRepo extends JpaRepository<ApiErrDto, Long>{

    // Api Err 리스트
    public static final String FIND_LIST = "SELECT t_api_err.api_err_code, t_api_err.api_err_comment, DATE_FORMAT(t_api_err.insert_timestamp, '%Y-%m-%d %H:%i:%s') as insert_timestamp, DATE_FORMAT(t_api_err.updated_timestamp, '%Y-%m-%d %H:%i:%s') as updated_timestamp, t_api_err.api_err_msg, t_api_err.api_err_no, t_api_err.api_no, t_api_err.api_err_status, t_api_err.jira_url, t_api_err.kibana_url, t_api_err.`ref`, a.api_url, concat(TRIM(TRAILING '/' FROM d.service_code), a.api_url) AS url, a.employee_no, a.method, a.api_category_no, a.employee_sub_no, a.employee_name, b.employee_sub_name, e_group.group_name as employee_group_name, b.employee_sub_group_name, d.api_category_name_kr, d.group_name as service_group_name, d.service_name_kr, d.service_no, d.service_url from ( select z.*, employee.name as employee_name, employee.group_no from (SELECT * FROM t_api WHERE t_api.is_deleted = 'F') z JOIN employee on z.employee_no = employee.employee_no ) a join ( SELECT t_api.api_no, t_api.employee_sub_no, employee.name as employee_sub_name, e_group.group_name as employee_sub_group_name from employee JOIN t_api on employee.employee_no = t_api.employee_sub_no JOIN e_group on employee.group_no = e_group.group_no) b on a.api_no = b.api_no JOIN ( SELECT t_api_category.api_category_name_kr, t_api_category.api_category_no, c.group_name, c.service_name_kr, c.service_no, c.service_url, c.service_code from t_api_category JOIN ( SELECT e_group.group_name, t_service.service_no, t_service.service_name_kr, t_service.service_url, t_service.service_code from t_service join e_group on t_service.group_no = e_group.group_no ) c on c.service_no = t_api_category.service_no ) d on a.api_category_no = d.api_category_no join t_api_err on t_api_err.api_no = a.api_no join e_group on e_group.group_no = a.group_no ORDER By t_api_err.insert_timestamp DESC";
    @Query(value = FIND_LIST, nativeQuery = true)
    List<Map<String, Object>> findByList();

    // Api Err 리스트 api_err_status 'T'
    public static final String FIND_LIST_STATUS = "SELECT t_api_err.api_err_code, t_api_err.api_err_comment, DATE_FORMAT(t_api_err.insert_timestamp, '%Y-%m-%d %H:%i:%s') as insert_timestamp, DATE_FORMAT(t_api_err.updated_timestamp, '%Y-%m-%d %H:%i:%s') as updated_timestamp, t_api_err.api_err_msg, t_api_err.api_err_no, t_api_err.api_no, t_api_err.api_err_status, t_api_err.jira_url, t_api_err.kibana_url, t_api_err.`ref`, a.api_url, concat(TRIM(TRAILING '/' FROM d.service_code), a.api_url) AS url, a.employee_no, a.method, a.api_category_no, a.employee_sub_no, a.employee_name, b.employee_sub_name, e_group.group_name as employee_group_name, b.employee_sub_group_name, d.api_category_name_kr, d.group_name as service_group_name, d.service_name_kr, d.service_no, d.service_url from ( select z.*, employee.name as employee_name, employee.group_no from (SELECT * FROM t_api WHERE t_api.is_deleted = 'F') z JOIN employee on z.employee_no = employee.employee_no ) a join ( SELECT t_api.api_no, t_api.employee_sub_no, employee.name as employee_sub_name, e_group.group_name as employee_sub_group_name from employee JOIN t_api on employee.employee_no = t_api.employee_sub_no JOIN e_group on employee.group_no = e_group.group_no) b on a.api_no = b.api_no JOIN ( SELECT t_api_category.api_category_name_kr, t_api_category.api_category_no, c.group_name, c.service_name_kr, c.service_no, c.service_url, c.service_code from t_api_category JOIN ( SELECT e_group.group_name, t_service.service_no, t_service.service_name_kr, t_service.service_url, t_service.service_code from t_service join e_group on t_service.group_no = e_group.group_no ) c on c.service_no = t_api_category.service_no ) d on a.api_category_no = d.api_category_no join t_api_err on t_api_err.api_no = a.api_no join e_group on e_group.group_no = a.group_no WHERE t_api_err.api_err_status = 'T' ORDER By t_api_err.insert_timestamp DESC";
    @Query(value = FIND_LIST_STATUS, nativeQuery = true)
    List<Map<String, Object>> findByStatusList();

    public static final String FIND_API_ERR_GET = "SELECT a.api_err_no, a.api_err_code, a.api_err_comment, a.api_err_msg, a.api_err_status, a.api_no, DATE_FORMAT(a.insert_timestamp, '%Y-%m-%d %H:%i:%s') as insert_timestamp, DATE_FORMAT(a.updated_timestamp, '%Y-%m-%d %H:%i:%s') as updated_timestamp, a.jira_url, a.kibana_url, a.`ref`, b.api_url, concat(TRIM(TRAILING '/' FROM d.service_code), b.api_url) AS url, b.api_category_no, b.description, b.employee_no, b.employee_sub_no, b.err_status, b.is_deleted, b.`method`, b.param, b.parameter_type, b.response_list, b.employee_group_name, b.employee_sub_group_name, b.employee_name, b.employee_sub_name, c.api_category_name_kr, c.service_no, d.group_no, d.service_category_no, d.service_name_kr, d.service_url, d.service_group_name, e.category_name_kr FROM t_api_err a JOIN ( SELECT k.api_no, k.api_url, k.api_category_no, k.description, k.employee_no, k.employee_sub_no, k.err_status, k.is_deleted, k.`method`, k.param, k.parameter_type, k.response_list, k.employee_group_name, k.name as employee_name, l.name as employee_sub_name, m.group_name as employee_sub_group_name FROM ( SELECT h.*, i.name, j.group_name as employee_group_name FROM t_api h JOIN employee i ON h.employee_no = i.employee_no JOIN e_group j ON i.group_no = j.group_no ) k JOIN employee l ON k.employee_sub_no = l.employee_no JOIN e_group m ON l.group_no = m.group_no ) b ON a.api_no = b.api_no JOIN t_api_category c ON b.api_category_no = c.api_category_no JOIN ( SELECT f.group_no, f.service_no, f.service_name_kr, f.service_category_no, f.service_url, f.service_code, g.group_name as service_group_name FROM t_service f JOIN e_group g ON f.group_no = g.group_no ) d ON c.service_no = d.service_no JOIN t_service_category e ON d.service_category_no = e.service_category_no WHERE a.api_err_no = :api_err_no";
    @Query(value = FIND_API_ERR_GET, nativeQuery = true)
    Map<String, Object> findApiErrGet(long api_err_no);
}