package com.duzon.dbp.apimonitoring.repo;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.duzon.dbp.apimonitoring.dto.ApiDto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * ServiceRepo
 */
@Repository
public interface ApiRepo extends JpaRepository<ApiDto, Long> {

    // Name 중복 확인 (생성 때)
    public static final String FIND_NAME_CHECK = "SELECT * FROM t_api WHERE t_api.is_deleted = 'F' AND t_api.api_url = :api_url AND t_api.`method` = :method AND t_api.api_category_no IN ( SELECT t_api_category.api_category_no FROM t_api_category WHERE t_api_category.service_no = (SELECT t_api_category.service_no FROM t_api_category WHERE t_api_category.api_category_no = :api_category_no))";
    @Query(value = FIND_NAME_CHECK, nativeQuery = true)
    Map<String, String> NameCheck(String api_url, String method, long api_category_no);

    // Name 중복 확인 (업데이트 때)
    public static final String FIND_NAME_CHECK_UP = "SELECT * FROM t_api WHERE t_api.is_deleted = 'F' AND t_api.api_url = :api_url AND t_api.`method` = :method AND t_api.api_category_no IN ( SELECT t_api_category.api_category_no FROM t_api_category WHERE t_api_category.service_no = ( SELECT t_api_category.service_no FROM t_api_category WHERE t_api_category.api_category_no = :api_category_no)) AND NOT t_api.api_no IN (:api_no)";
    @Query(value = FIND_NAME_CHECK_UP, nativeQuery = true)
    String NameCheckUp(String api_url, String method, long api_category_no, long api_no);

    // isDelete가 'F'인 리스트
    public static final String FIND_ISDELETE_LIST = "SELECT a.api_no, a.api_url, concat(TRIM(TRAILING '/' FROM c.service_code), a.api_url) AS url, a.parameter_type, a.delay_status, a.employee_no, a.err_status, DATE_FORMAT(a.insert_timestamp, '%Y-%m-%d %H:%i:%s') as insert_timestamp, a.is_deleted, a.method, a.param, a.response_list, a.description, a.api_category_no, DATE_FORMAT(a.updated_timestamp, '%Y-%m-%d %H:%i:%s') as updated_timestamp, a.employee_sub_no, a.response_type, a.employee_name, b.employee_sub_name, a.group_name as employee_group_name, b.group_name as employee_sub_group_name, c.api_category_name_kr, c.service_name_kr, c.service_no, c.service_url, c.service_group_name from ( select t_api.*, employee.name as employee_name, employee.group_no, e_group.group_name from t_api JOIN employee on t_api.employee_no = employee.employee_no join e_group on employee.group_no = e_group.group_no) a join ( SELECT t_api.api_no, t_api.employee_sub_no, employee.name as employee_sub_name, e_group.group_name from employee JOIN t_api on employee.employee_no = t_api.employee_sub_no join e_group on employee.group_no = e_group.group_no) b on a.api_no = b.api_no JOIN ( SELECT t_api_category.api_category_no, t_api_category.api_category_name_kr, z.service_name_kr, z.service_no, z.service_url, z.service_code, z.service_group_name from t_api_category join ( SELECT x.*, y.group_name as service_group_name FROM t_service x JOIN e_group y ON x.group_no = y.group_no) z on t_api_category.service_no = z.service_no ) c on a.api_category_no = c.api_category_no WHERE a.is_deleted = 'F' ORDER By a.api_no DESC";
    @Query(value = FIND_ISDELETE_LIST, nativeQuery = true)
    List<Map<String, Object>> findByIsDeleteList();

    public static final String FIND_HEAD_URL = "SELECT c.service_url FROM t_api a JOIN t_api_category b ON a.api_category_no = b.api_category_no JOIN t_service c ON b.service_no = c.service_no WHERE a.api_no = :api_no";
    @Query(value = FIND_HEAD_URL, nativeQuery = true)
    String findByHeadUrl(Long api_no);
    
    // API Detail Check
    public static final String API_CATEGORY_SERVICE_CHECK = "SELECT a.* FROM t_api a JOIN ( SELECT * FROM t_api_category b WHERE b.api_category_no = :api_category_no AND b.service_no = :service_no) c ON a.api_category_no = c.api_category_no";
    @Query(value = API_CATEGORY_SERVICE_CHECK, nativeQuery = true)
    Optional<List<ApiDto>> ApiCategoryServicecheck(long api_category_no, long service_no);
    // API Detail service
    public static final String FIND_API_DETAIL_SERVICE = "SELECT a.*, b.group_name FROM t_service a JOIN e_group b ON a.group_no = b.group_no WHERE a.service_no = :service_no";
    @Query(value = FIND_API_DETAIL_SERVICE, nativeQuery = true)
    Map<String, Object> findApiDetailservice(long service_no);
    // API Detail Api_category
    public static final String FIND_API_DETAIL_API_CATEGORY = "SELECT * FROM t_api_category a WHERE a.api_category_no = :api_no";
    @Query(value = FIND_API_DETAIL_API_CATEGORY, nativeQuery = true)
    Map<String, Object> findApiDetailApiCategory(long api_no);
    // API Detail Api (isDelete = 'F')
    public static final String FIND_API_DETAIL_API = "SELECT a.*, b.name as employee_name, b.group_name as employee_group_name, e.name as employee_sub_name, e.group_name as employee_sub_group_name, j.name as update_employee_name, j.group_name as update_employee_group_name FROM ( Select t_api.api_category_no, t_api.api_no, t_api.api_url, t_api.delay_status, t_api.description, t_api.employee_no, t_api.employee_sub_no, t_api.err_status, DATE_FORMAT(t_api.insert_timestamp, '%Y-%m-%d %H:%i:%s') as insert_timestamp, t_api.is_deleted, t_api.`method`, t_api.param, t_api.parameter_type, t_api.response_list, t_api.response_type, t_api.update_employee_no, DATE_FORMAT(t_api.updated_timestamp, '%Y-%m-%d %H:%i:%s') as updated_timestamp from t_api where t_api.is_deleted = 'F') a JOIN ( SELECT c.employee_no, c.name, d.group_name FROM employee c JOIN e_group d ON c.group_no = d.group_no ) b on a.employee_no = b.employee_no JOIN ( SELECT f.employee_no, f.name, g.group_name FROM employee f JOIN e_group g ON f.group_no = g.group_no ) e ON a.employee_sub_no = e.employee_no JOIN ( SELECT h.employee_no, h.name, i.group_name FROM employee h JOIN e_group i ON h.group_no = i.group_no ) j ON a.update_employee_no = j.employee_no where a.api_category_no = :api_category_no and a.is_deleted = 'F' ORDER BY a.api_no";
    @Query(value = FIND_API_DETAIL_API, nativeQuery = true)
    List<Map<String, Object>> findApiDetailApi(long api_category_no);

    public static final String FIND_API_ERROR_LIST = "SELECT COUNT(*) FROM t_api a JOIN t_api_err b ON a.api_no = b.api_no where a.api_no = :api_no AND b.api_err_status = 'T'";
	@Query(value = FIND_API_ERROR_LIST, nativeQuery = true)
    int findApiErrorList(long api_no);

    public static final String FIND_API_DELAY_LIST = "SELECT COUNT(*) FROM t_api a JOIN t_api_delay b ON a.api_no = b.api_no where a.api_no = :api_no AND b.api_delay_status = 'T'";
	@Query(value = FIND_API_DELAY_LIST, nativeQuery = true)
    int findApiDelayList(long api_no);

    // Err 및 Delay 등록 시 api_url, method로 api 찾기
    public static final String FIND_API_NO = "SELECT e.api_no, e.api_url, e.parameter_type, e.delay_status, e.employee_no, e.err_status, DATE_FORMAT(e.insert_timestamp, '%Y-%m-%d %H:%i:%s') as insert_timestamp, e.is_deleted, e.method, e.param, e.response_list, e.description, e.api_category_no, e.updated_timestamp, e.employee_sub_no, e.response_type, e.update_employee_no FROM ( SELECT c.*, d.service_url, concat(TRIM(TRAILING '/' FROM d.service_url), c.api_url) AS url FROM ( SELECT a.*, b.service_no FROM ( SELECT * FROM t_api WHERE t_api.is_deleted = 'F' ) a JOIN t_api_category b ON a.api_category_no = b.api_category_no ) c JOIN t_service d ON c.service_no = d.service_no ) e WHERE e.url = :url AND e.method = :method";
    @Query(value = FIND_API_NO, nativeQuery = true)
    Optional<ApiDto> findApino(String url, String method);
    // Err 및 Delay 등록 시 api_no로 api 찾기
    public static final String FIND_BY_API_NO = "SELECT * FROM t_api a WHERE a.api_no = :api_no";
    @Query(value = FIND_BY_API_NO, nativeQuery = true)
    Optional<ApiDto> findByApi_no(Long api_no);
    
    // Err, Delay 생성 시 API Full URL, Method 리스트
    public static final String API_FULL_URL_AND_METHOD_LIST = "SELECT concat(TRIM(TRAILING '/' FROM d.service_url), c.api_url) AS url, c.method FROM ( SELECT a.*, b.service_no FROM ( SELECT * FROM t_api WHERE t_api.is_deleted = 'F' ) a JOIN t_api_category b ON a.api_category_no = b.api_category_no ) c JOIN t_service d ON c.service_no = d.service_no";
    @Query(value = API_FULL_URL_AND_METHOD_LIST, nativeQuery = true)
    List<Map<String, Object>> apiFullURLAndMethodList();
}