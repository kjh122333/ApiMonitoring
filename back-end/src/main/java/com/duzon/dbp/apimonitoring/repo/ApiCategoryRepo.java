package com.duzon.dbp.apimonitoring.repo;

import java.util.List;
import java.util.Map;

import com.duzon.dbp.apimonitoring.dto.ApiCategoryDto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * ApiCategoryRepo
 */
public interface ApiCategoryRepo extends JpaRepository<ApiCategoryDto, Long>{

    // ApiCategory 리스트 ( isDeleted = 'T' / 'F')
    public static final String FIND_ALL_LIST = "SELECT a.api_category_name_kr, a.api_category_no, a.api_category_state, DATE_FORMAT(a.insert_timestamp, '%Y-%m-%d %H:%i:%s') as insert_timestamp, a.is_deleted, a.service_no, DATE_FORMAT(a.updated_timestamp, '%Y-%m-%d %H:%i:%s') as updated_timestamp, b.service_name_kr, b.group_name, b.group_no, b.category_name_kr, b.service_category_no FROM t_api_category a JOIN ( SELECT c.*, d.group_name FROM ( SELECT e.*, f.category_name_kr FROM t_service e JOIN t_service_category f ON e.service_category_no = f.service_category_no) c JOIN e_group d ON c.group_no = d.group_no ) b ON a.service_no = b.service_no ORDER BY a.api_category_no DESC";
    @Query(value = FIND_ALL_LIST, nativeQuery = true)
    List<Map<String, Object>> findAllList();
    
    // ApiCategory 리스트 ( isDeleted = 'F')
    public static final String FIND_ALL_LIST_F = "SELECT * FROM t_api_category a WHERE a.is_deleted = 'F'";
    @Query(value = FIND_ALL_LIST_F, nativeQuery = true)
    List<ApiCategoryDto> findListF();
    
    // Name 중복 확인 (생성 때)
    public static final String FIND_NAME_CHECK = "SELECT a.api_category_name_kr FROM ( SELECT * FROM t_api_category c WHERE c.is_deleted = 'F' AND c.api_category_name_kr = :api_category_name_kr) a JOIN t_service b ON a.service_no = b.service_no AND b.service_no = :service_no";
    @Query(value = FIND_NAME_CHECK, nativeQuery = true)
    String NameCheck(String api_category_name_kr, long service_no);

    // Name 중복 확인 (업데이트 때)
    public static final String FIND_NAME_CHECK_UP = "SELECT a.api_category_name_kr FROM ( SELECT * FROM t_api_category c WHERE c.is_deleted = 'F' AND c.api_category_name_kr = :api_category_name_kr AND NOT c.api_category_no = :api_category_no) a JOIN t_service b ON a.service_no = b.service_no AND b.service_no = :service_no";
    @Query(value = FIND_NAME_CHECK_UP, nativeQuery = true)
    String NameCheckUp(String api_category_name_kr, long api_category_no, long service_no);

    // Api 테이블까지 isDelete 'T'
    public static final String ISDELETE_ALL_T = "UPDATE t_api_category T1 INNER JOIN t_api T2 ON T1.api_category_no = T2.api_category_no SET T1.is_deleted = 'T', T2.is_deleted = 'T' WHERE T1.api_category_no = :api_category_no";
    @Query(value = ISDELETE_ALL_T, nativeQuery = true)
    void isDeleteAllT(long api_category_no);
    
    // Api Category 테이블까지 isDelete 'T'
    public static final String ISDELETE_AL_T = "UPDATE t_api_category T1 SET T1.is_deleted = 'T' WHERE T1.api_category_no = :api_category_no";
    @Query(value = ISDELETE_AL_T, nativeQuery = true)
    void isDeleteAlT(long api_category_no);

    // Api Category의 이름
    public static final String FIND_NAME = "SELECT a.api_category_name_kr FROM t_api_category a WHERE a.api_category_no = :api_category_no";
    @Query(value = FIND_NAME, nativeQuery = true)
    String findName(long api_category_no);
    
    // Api Category의 자식 Api
    public static final String CHILD_API = "SELECT d.api_category_no, d.api_no, d.api_url, d.delay_status, d.description, d.employee_no, d.employee_sub_no, d.err_status, DATE_FORMAT(d.insert_timestamp, '%Y-%m-%d %H:%i:%s') as insert_timestamp, d.is_deleted, d.`method`, d.param, d.parameter_type, d.response_list, d.response_type, d.update_employee_no, DATE_FORMAT(d.updated_timestamp, '%Y-%m-%d %H:%i:%s') as updated_timestamp FROM ( SELECT * FROM t_api_category WHERE t_api_category.api_category_no = :api_category_no ) a JOIN t_api d ON a.api_category_no = d.api_category_no AND d.is_deleted = 'F'";
    @Query(value = CHILD_API, nativeQuery = true)
    List<Map<String, Object>> ChildApi(long api_category_no);
}