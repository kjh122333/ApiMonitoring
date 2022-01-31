package com.duzon.dbp.apimonitoring.repo;

import java.util.List;
import java.util.Map;

import com.duzon.dbp.apimonitoring.dto.ServiceCategoryDto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * ServiceCategoryRepo
 */
@Repository
public interface ServiceCategoryRepo extends JpaRepository<ServiceCategoryDto, Long> {

    //! Name 중복 확인 (생성 때)
    public static final String FIND_NAME_CHECK = "SELECT t_service_category.category_name_kr FROM t_service_category WHERE t_service_category.is_deleted = 'F' AND t_service_category.category_name_kr = :category_name_kr";
    @Query(value = FIND_NAME_CHECK, nativeQuery = true)
    String NameCheck(String category_name_kr);

    //! Name 중복 확인 (업데이트 때)
    public static final String FIND_NAME_CHECK_UP = "SELECT t_service_category.category_name_kr FROM t_service_category WHERE t_service_category.is_deleted = 'F' AND t_service_category.category_name_kr = :category_name_kr AND NOT t_service_category.service_category_no = :service_category_no";
    @Query(value = FIND_NAME_CHECK_UP, nativeQuery = true)
    String NameCheckUp(String category_name_kr, long service_category_no);

    //! Api 테이블까지 isDelete 'T'
    public static final String ISDELETE_ALL_T = "UPDATE t_service_category T1 INNER JOIN t_service T2 ON T1.service_category_no = T2.service_category_no INNER JOIN t_api_category T3 ON T2.service_no = T3.service_no INNER JOIN t_api T4 ON T3.api_category_no = T4.api_category_no SET T1.is_deleted = 'T', T2.is_deleted = 'T', T3.is_deleted = 'T', T4.is_deleted = 'T' WHERE T1.service_category_no = :service_category_no";
    @Query(value = ISDELETE_ALL_T, nativeQuery = true)
    void isDeleteAllT(long service_category_no);

    //! Api Category 테이블까지 isDelete 'T'
    public static final String ISDELETE_AL_T = "UPDATE t_service_category T1 INNER JOIN t_service T2 ON T1.service_category_no = T2.service_category_no INNER JOIN t_api_category T3 ON T2.service_no = T3.service_no SET T1.is_deleted = 'T', T2.is_deleted = 'T', T3.is_deleted = 'T' WHERE T1.service_category_no = :service_category_no";
    @Query(value = ISDELETE_AL_T, nativeQuery = true)
    void isDeleteAlT(long service_category_no);

    //! Service 테이블까지 isDelete 'T'
    public static final String ISDELETE_A_T = "UPDATE t_service_category T1 INNER JOIN t_service T2 ON T1.service_category_no = T2.service_category_no SET T1.is_deleted = 'T',	T2.is_deleted = 'T' WHERE T1.service_category_no = :service_category_no";
    @Query(value = ISDELETE_A_T, nativeQuery = true)
    void isDeleteAT(long service_category_no);
    
    //! Service Category의 Name
    public static final String FIND_NAME = "SELECT t_service_category.category_name_kr FROM t_service_category WHERE t_service_category.service_category_no = :service_category_no";
    @Query(value = FIND_NAME, nativeQuery = true)
    String findName(long service_category_no);
    
    //! Service Category의 자식 Service
    public static final String CHILD_SERVICE = "SELECT b.definitions, b.group_no, DATE_FORMAT(b.insert_timestamp, '%Y-%m-%d %H:%i:%s') as insert_timestamp, b.is_deleted, b.service_category_no, b.service_name_kr, b.service_no, b.service_state, b.service_url, DATE_FORMAT(b.updated_timestamp, '%Y-%m-%d %H:%i:%s') as updated_timestamp FROM ( SELECT * FROM t_service_category WHERE t_service_category.service_category_no = :service_category_no ) a JOIN t_service b ON a.service_category_no = b.service_category_no AND b.is_deleted = 'F'";
    @Query(value = CHILD_SERVICE, nativeQuery = true)
    List<Map<String, Object>> ChildSerivce(long service_category_no);

    //! Service Category의 자식 Api Category
    public static final String CHILD_API_CATEGORY = "SELECT c.api_category_name_kr, c.api_category_no, c.api_category_state, DATE_FORMAT(c.insert_timestamp, '%Y-%m-%d %H:%i:%s') as insert_timestamp, c.is_deleted, c.service_no, DATE_FORMAT(c.updated_timestamp, '%Y-%m-%d %H:%i:%s') as updated_timestamp FROM ( SELECT * FROM t_service_category WHERE t_service_category.service_category_no = :service_category_no) a JOIN t_service b ON a.service_category_no = b.service_category_no AND b.is_deleted = 'F' JOIN t_api_category c ON b.service_no = c.service_no AND c.is_deleted = 'F'";
    @Query(value = CHILD_API_CATEGORY, nativeQuery = true)
    List<Map<String, Object>> ChildApiCategory(long service_category_no);

    //! Service Category의 자식 Api
    public static final String CHILD_API = "SELECT d.api_category_no, d.api_no, d.api_url, d.delay_status, d.description, d.employee_no, d.employee_sub_no, d.err_status, DATE_FORMAT(d.insert_timestamp, '%Y-%m-%d %H:%i:%s') as insert_timestamp, d.is_deleted, d.`method`, d.param, d.parameter_type, d.response_list, d.response_type, d.update_employee_no, DATE_FORMAT(d.updated_timestamp, '%Y-%m-%d %H:%i:%s') as updated_timestamp FROM ( SELECT * FROM t_service_category WHERE t_service_category.service_category_no = :service_category_no) a JOIN t_service b ON a.service_category_no = b.service_category_no AND b.is_deleted = 'F' JOIN t_api_category c ON b.service_no = c.service_no AND c.is_deleted = 'F' JOIN t_api d ON c.api_category_no = d.api_category_no AND d.is_deleted = 'F'";
    @Query(value = CHILD_API, nativeQuery = true)
    List<Map<String, Object>> ChildApi(long service_category_no);

    // Service Category List (isDeleted 'F')
    public static final String FIND_LIST_ISDELETED_F = "SELECT * FROM t_service_category a WHERE a.is_deleted = 'F'";
    @Query(value = FIND_LIST_ISDELETED_F, nativeQuery = true)
    List<ServiceCategoryDto> findlistF();

    // Service Category List
    public static final String FIND_LIST = "SELECT * FROM t_service_category a ORDER BY a.service_category_no DESC";
    @Query(value = FIND_LIST, nativeQuery = true)
	List<ServiceCategoryDto> findlist();
}