package com.duzon.dbp.apimonitoring.repo;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.duzon.dbp.apimonitoring.dto.socket.NotificationDto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * NotificationRepo
 */
@Repository
public interface NotificationRepo extends JpaRepository<NotificationDto, Long> {
    
    public static final String NOTIFICATION_ERR_COUNT_READ_F = "SELECT COUNT(*) FROM ( SELECT * FROM notification c WHERE c.is_confirm = 'F') a JOIN employee b ON a.employee_no = b.employee_no AND b.id = :id";
    @Query(value = NOTIFICATION_ERR_COUNT_READ_F, nativeQuery = true)
    int countReadF(String id);

    public static final String FIND_API_ID = "SELECT a.id FROM employee a WHERE a.employee_no = :employee_no";
    @Query(value = FIND_API_ID, nativeQuery = true)
    String findApiid(Long employee_no);

    public static final String FIND_ERR_ID = "SELECT b.id FROM ( SELECT * FROM t_api_err WHERE t_api_err.api_err_no = :api_err_no) a JOIN ( SELECT c.*, d.id FROM t_api c JOIN employee d ON c.employee_no = d.employee_no) b ON a.api_no = b.api_no";
    @Query(value = FIND_ERR_ID, nativeQuery = true)
    String findErrid(Long api_err_no);
    
    public static final String FIND_ERR_SUB_ID = "SELECT b.id FROM ( SELECT * FROM t_api_err WHERE t_api_err.api_err_no = :api_err_no) a JOIN ( SELECT c.*, d.id FROM t_api c JOIN employee d ON c.employee_sub_no = d.employee_no) b ON a.api_no = b.api_no";
    @Query(value = FIND_ERR_SUB_ID, nativeQuery = true)
    String findErrSubid(Long api_err_no);
    
    public static final String FIND_DELAY_ID = "SELECT b.id FROM ( SELECT * FROM t_api_delay WHERE t_api_delay.api_delay_no = :api_delay_no) a JOIN ( SELECT c.*, d.id FROM t_api c JOIN employee d ON c.employee_no = d.employee_no) b ON a.api_no = b.api_no";
    @Query(value = FIND_DELAY_ID, nativeQuery = true)
    String findDelayid(Long api_delay_no);

    public static final String FIND_DELAY_SUB_ID = "SELECT b.id FROM ( SELECT * FROM t_api_delay WHERE t_api_delay.api_delay_no = :api_delay_no) a JOIN ( SELECT c.*, d.id FROM t_api c JOIN employee d ON c.employee_sub_no = d.employee_no) b ON a.api_no = b.api_no";
    @Query(value = FIND_DELAY_SUB_ID, nativeQuery = true)
    String findDelaySubid(Long api_delay_no);
    
    public static final String FIND_EMPLOYEE_NO = "SELECT d.employee_no FROM (SELECT * FROM t_api a WHERE a.api_no = :api_no) c JOIN employee d ON c.employee_no = d.employee_no";
    @Query(value = FIND_EMPLOYEE_NO, nativeQuery = true)
	long findEmployee_no(Long api_no);
    
    public static final String FIND_EMPLOYEE_SUB_NO = "SELECT d.employee_no FROM ( SELECT * FROM t_api a WHERE a.api_no = :api_no) c JOIN employee d ON c.employee_sub_no = d.employee_no";
    @Query(value = FIND_EMPLOYEE_SUB_NO, nativeQuery = true)
    long findEmployee_Sub_no(long api_no);
    
    public static final String FIND_API_CATEGORY_NO = "SELECT b.api_category_no FROM ( SELECT * FROM t_api c WHERE c.api_no = :api_no) a JOIN t_api_category b ON a.api_category_no = b.api_category_no";
    @Query(value = FIND_API_CATEGORY_NO, nativeQuery = true)
    long findApi_category_no(Long api_no);
    
    public static final String FIND_SERVICE_NO = "SELECT b.service_no FROM ( SELECT * FROM t_api_category c WHERE c.api_category_no = :api_category_no) a JOIN t_service b ON a.service_no = b.service_no";
    @Query(value = FIND_SERVICE_NO, nativeQuery = true)
    long findService_no(Long api_category_no);
    
    public static final String FIND_URL = "SELECT concat(TRIM(TRAILING '/' FROM d.service_url), c.api_url) AS url FROM ( SELECT a.*, b.service_no FROM ( SELECT * FROM t_api WHERE t_api.is_deleted = 'F' AND t_api.api_no = :api_no) a JOIN t_api_category b ON a.api_category_no = b.api_category_no ) c JOIN t_service d ON c.service_no = d.service_no";
    @Query(value = FIND_URL, nativeQuery = true)
    String findurl(long api_no);

    // employee_no에 해당하는 리스트 ( 조인 )
    public static final String NOTIFICATION_GET_LIST = "SELECT * FROM ( SELECT b.*, c.api_url, d.service_url, concat(TRIM(TRAILING '/' FROM d.service_code), c.api_url) AS url, d.service_code, concat(TRIM(TRAILING '/' FROM d.service_code), c.api_url) AS code_url FROM ( SELECT a.api_category_no, a.api_no, a.delay_no, a.employee_no, a.err_no, DATE_FORMAT(a.insert_timestamp, '%Y-%m-%d %H:%i:%s') as insert_timestamp, a.is_confirm, a.is_read, a.notify_no, a.service_no, a.type FROM notification a WHERE a.employee_no = :employee_no) b JOIN t_api c ON b.api_no = c.api_no JOIN t_service d ON b.service_no = d.service_no) e ORDER BY e.insert_timestamp DESC";
    @Query(value = NOTIFICATION_GET_LIST, nativeQuery = true)
    List<Map<String, Object>> NotificationGetList(long employee_no);
    
    // employee_no에 해당하는 리스트
    public static final String FIND_LIST = "SELECT * FROM notification a WHERE a.employee_no = :employee_no";
    @Query(value = FIND_LIST, nativeQuery = true)
    List<NotificationDto> findList(long employee_no);
    
    // notify_no에 해당하는 단건
    public static final String NOTIFICATION_GET = "SELECT * FROM notification a WHERE a.notify_no = :notify_no";
    @Query(value = NOTIFICATION_GET, nativeQuery = true)
    Optional<NotificationDto> NotificationGet(long notify_no);

    // id로 회원 번호 찾기
    public static final String FIND_EM_ID = "SELECT a.employee_no FROM employee a WHERE a.id = :id";
    @Query(value = FIND_EM_ID, nativeQuery = true)
    long findEmID(String id);
    
    // 초기에 로그인 시 is_confirm = 'F' 개수
    public static final String NOTIFICATION_GET_LIST_COUNT = "SELECT COUNT(*) FROM notification a WHERE a.employee_no = :employee_no AND a.is_confirm = 'F'";
    @Query(value = NOTIFICATION_GET_LIST_COUNT, nativeQuery = true)
	int NotificationGetListCount(long employee_no);
}