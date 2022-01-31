package com.duzon.dbp.apimonitoring.repo;

import java.util.List;
import java.util.Map;

import com.duzon.dbp.apimonitoring.dto.ServiceDto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * ServiceRepo
 */
@Repository
public interface ServiceRepo extends JpaRepository<ServiceDto, Long> {

    // Service 모두 포함 리스트 ( isDelete = 'T' / 'F' )
    public static final String FIND_ALL_LIST = "SELECT b.*, c.category_name_kr, d.group_name FROM ( SELECT a.definitions, a.group_no, DATE_FORMAT(a.insert_timestamp, '%Y-%m-%d %H:%i:%s') as insert_timestamp, a.is_deleted, a.service_category_no, a.service_name_kr, a.service_no, a.service_state, a.service_url, DATE_FORMAT(a.updated_timestamp, '%Y-%m-%d %H:%i:%s') as updated_timestamp, a.service_code FROM t_service a ) b JOIN t_service_category c ON b.service_category_no = c.service_category_no JOIN e_group d ON b.group_no = d.group_no ORDER BY b.service_no DESC";
    @Query(value = FIND_ALL_LIST, nativeQuery = true)
    List<Map<String, Object>> FindAllList();
    
    // Service 리스트 ( isDelete = 'F' )
    public static final String FIND_LIST = "SELECT a.definitions, a.group_no, DATE_FORMAT(a.insert_timestamp, '%Y-%m-%d %H:%i:%s') as insert_timestamp, a.is_deleted, a.service_category_no, a.service_name_kr, a.service_no, a.service_state, a.service_url, DATE_FORMAT(a.updated_timestamp, '%Y-%m-%d %H:%i:%s') as updated_timestamp FROM t_service a WHERE a.is_deleted = 'F'";
    @Query(value = FIND_LIST, nativeQuery = true)
    List<Map<String, Object>> FindList();

    // service_name_kr 중복 확인 (생성 때)
    public static final String FIND_NAME_CHECK = "SELECT a.service_name_kr FROM ( SELECT b.* FROM t_service b JOIN t_service_category c ON b.service_category_no = c.service_category_no AND c.service_category_no = :service_category_no) a WHERE a.is_deleted = 'F' AND a.service_name_kr = :service_name_kr";
    @Query(value = FIND_NAME_CHECK, nativeQuery = true)
    String NameCheck(String service_name_kr, long service_category_no);

    // service_url 중복 확인 (생성 때)
    public static final String FIND_URL_CHECK = "SELECT a.service_url FROM t_service a WHERE a.service_url = :service_url AND a.is_deleted = 'F'";
    @Query(value = FIND_URL_CHECK, nativeQuery = true)
    String UrlCheck(String service_url);

    // Name 중복 확인 (업데이트 때)
    public static final String FIND_NAME_CHECK_UP = "SELECT a.service_name_kr FROM ( SELECT b.* FROM t_service b JOIN t_service_category c ON b.service_category_no = c.service_category_no AND c.service_category_no = :service_category_no) a WHERE a.is_deleted = 'F' AND a.service_name_kr = :service_name_kr AND NOT a.service_no = :service_no";
    @Query(value = FIND_NAME_CHECK_UP, nativeQuery = true)
    String NameCheckUp(String service_name_kr, long service_no, long service_category_no);

    // service_url 중복 확인 (생성 때)
    public static final String FIND_URL_CHECK_UP = "SELECT a.service_url FROM t_service a WHERE a.service_url = :service_url AND a.is_deleted = 'F' AND NOT a.service_no = :service_no";
    @Query(value = FIND_URL_CHECK_UP, nativeQuery = true)
    String UrlCheckUp(String service_url, Long service_no);

    // Api 테이블까지 isDelete 'T'
    public static final String ISDELETE_ALL_T = "UPDATE t_service T1 INNER JOIN t_api_category T2 ON T1.service_no = T2.service_no INNER JOIN t_api T3 ON T2.api_category_no = T3.api_category_no SET T1.is_deleted = 'T', T2.is_deleted = 'T', T3.is_deleted = 'T' WHERE T1.service_no = :service_no";
    @Query(value = ISDELETE_ALL_T, nativeQuery = true)
    void isDeleteAllT(long service_no);

    // Api Category 테이블까지 isDelete 'T'
    public static final String ISDELETE_AL_T = "UPDATE t_service T1 INNER JOIN t_api_category T2 ON T1.service_no = T2.service_no SET T1.is_deleted = 'T', T2.is_deleted = 'T' WHERE T1.service_no = :service_no";
    @Query(value = ISDELETE_AL_T, nativeQuery = true)
    void isDeleteAlT(long service_no);

    // Service 테이블까지 isDelete 'T'
    public static final String ISDELETE_A_T = "UPDATE t_service T1 SET T1.is_deleted = 'T' WHERE T1.service_no = :service_no";
    @Query(value = ISDELETE_A_T, nativeQuery = true)
    void isDeleteAT(long service_no);

    // Service의 Service Category Name
    public static final String FIND_SERVICE_CATEGORY_NAME = "SELECT b.category_name_kr FROM (SELECT * FROM t_service WHERE t_service.service_no = :service_no) a JOIN t_service_category b ON a.service_category_no = b.service_category_no";
    @Query(value = FIND_SERVICE_CATEGORY_NAME, nativeQuery = true)
    String findServiceCategoryName(long service_no);

    // Service의 Service List
    public static final String FIND_SERVICE_LIST = "SELECT * FROM t_service a WHERE a.service_no = :service_no";
    @Query(value = FIND_SERVICE_LIST, nativeQuery = true)
    Map<String, Object> findServiceGet(long service_no);

	// Service의 자식 Api Category
    public static final String CHILD_API_CATEGORY = "SELECT c.api_category_name_kr, c.api_category_no, c.api_category_state, DATE_FORMAT(c.insert_timestamp, '%Y-%m-%d %H:%i:%s') as insert_timestamp, c.is_deleted, c.service_no, DATE_FORMAT(c.updated_timestamp, '%Y-%m-%d %H:%i:%s') as updated_timestamp FROM ( SELECT * FROM t_service WHERE t_service.service_no = :service_no) a JOIN t_api_category c ON a.service_no = c.service_no AND c.is_deleted = 'F'";
    @Query(value = CHILD_API_CATEGORY, nativeQuery = true)
    List<Map<String, Object>> ChildApiCategory(long service_no);

    // Service의 자식 Api
    public static final String CHILD_API = "SELECT d.api_category_no, d.api_no, d.api_url, d.delay_status, d.description, d.employee_no, d.employee_sub_no, d.err_status, DATE_FORMAT(d.insert_timestamp, '%Y-%m-%d %H:%i:%s') as insert_timestamp, d.is_deleted, d.`method`, d.param, d.parameter_type, d.response_list, d.response_type, d.update_employee_no, DATE_FORMAT(d.updated_timestamp, '%Y-%m-%d %H:%i:%s') as updated_timestamp FROM ( SELECT * FROM t_service WHERE t_service.service_no = :service_no ) a JOIN t_api_category c ON a.service_no = c.service_no AND c.is_deleted = 'F' JOIN t_api d ON c.api_category_no = d.api_category_no AND d.is_deleted = 'F'";
    @Query(value = CHILD_API, nativeQuery = true)
    List<Map<String, Object>> ChildApi(long service_no);
}