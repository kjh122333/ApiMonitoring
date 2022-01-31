package com.duzon.dbp.apimonitoring.repo;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.duzon.dbp.apimonitoring.dto.Employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
/**
 * EmployeeRepo
 */
@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long> {
    
    //! employee 단체 아이디 중복 확인
    public static final String EMPLOYEE_LIST_ID_CHECK = "SELECT a.id FROM employee a WHERE a.id IN (:idx)";
    @Query(value = EMPLOYEE_LIST_ID_CHECK, nativeQuery = true)
    List<String> employeeListIdCheck(List<String> idx);

    Optional<Employee> findById(String id);

    public static final String FIND_BY_IDX = "SELECT * From employee e WHERE e.employee_no = :idx";
	@Query(value = FIND_BY_IDX, nativeQuery = true)
    Optional<Employee> findByIdx(Long idx);

    public static final String EMPLOYEE_DATA_LIST = "SELECT e.employee_no, e.name, e.roles, e.group_no, g.group_name FROM ( SELECT a.*, b.roles FROM employee a JOIN employee_roles b ON a.employee_no = b.employee_employee_no AND b.roles = 'ROLE_USER') e JOIN e_group g ON e.group_no = g.group_no ORDER BY e.name";
    @Query(value = EMPLOYEE_DATA_LIST, nativeQuery = true)
    List<Map<String, Object>> employeeDataList();

    public static final String FIND_EMPLOYEE_LIST = "SELECT a.*, b.group_name FROM ( SELECT e.certification, e.columns_config, e.employee_contact, e.employee_no, e.group_no, e.id, DATE_FORMAT(e.insert_timestamp, '%Y-%m-%d %H:%i:%s') as insert_timestamp, e.is_deleted, e.mail, e.name, DATE_FORMAT(e.updated_timestamp, '%Y-%m-%d %H:%i:%s') as updated_timestamp FROM employee e where e.is_deleted = 'F' ) a JOIN e_group b ON a.group_no = b.group_no ORDER BY a.insert_timestamp DESC";
	@Query(value = FIND_EMPLOYEE_LIST, nativeQuery = true)
    List<Map<String, Object>> findEmployeeList();
    
    public static final String FIND_EMAIL_AUTH = "SELECT * FROM employee a WHERE a.id = :id and a.mail = :mail and a.name = :name";
	@Query(value = FIND_EMAIL_AUTH, nativeQuery = true)
    Optional<Employee> findEmailAuth(String id, String  mail, String  name);

    public static final String FIND_EMPLOYEE_GET = "SELECT a.*, b.group_name, c.roles FROM employee a JOIN e_group b ON a.group_no = b.group_no JOIN employee_roles c ON a.employee_no = c.employee_employee_no WHERE a.employee_no = :employee_no";
    @Query(value = FIND_EMPLOYEE_GET, nativeQuery = true)
	Map<String, Object> employeeGet(long employee_no);

    public static final String FIND_BY_EMPLOYEE_NO = "SELECT a.* FROM employee a WHERE a.id = :update_employee_id";
    @Query(value = FIND_BY_EMPLOYEE_NO, nativeQuery = true)
    Optional<Employee> findByEmployeeNo(String update_employee_id);
    
    // 로그인 시 그룹 이름 줄때
    public static final String EMPLOYEE_GROUP_NAME = "SELECT b.group_name FROM ( SELECT * FROM employee WHERE employee.employee_no = :employee_no ) a JOIN e_group b ON a.group_no = b.group_no";
    @Query(value = EMPLOYEE_GROUP_NAME, nativeQuery = true)
    String employeeGroupName(Long employee_no);
    
    // Total Count의 Err 정 부
    public static final String EMPLOYEE_INFO_COUNT_ERR = "SELECT COUNT(*) as err_count FROM ( SELECT a.*, b.name, b.id FROM (SELECT * FROM t_api WHERE t_api.is_deleted = 'F') a JOIN employee b ON a.employee_no = b.employee_no) c JOIN employee e ON c.employee_sub_no = e.employee_no JOIN t_api_err d ON c.api_no = d.api_no WHERE c.id = :id OR e.id = :id";
    @Query(value = EMPLOYEE_INFO_COUNT_ERR, nativeQuery = true)
    int employeeInfoCountErr(String id);
    
    // Total Count의 Delay 정 부
    public static final String EMPLOYEE_INFO_COUNT_DELAY = "SELECT COUNT(*) as delay_count FROM ( SELECT a.*, b.name, b.id FROM (SELECT * FROM t_api WHERE t_api.is_deleted = 'F') a JOIN employee b ON a.employee_no = b.employee_no) c JOIN employee e ON c.employee_sub_no = e.employee_no JOIN t_api_delay d ON c.api_no = d.api_no WHERE c.id = :id OR e.id = :id";
    @Query(value = EMPLOYEE_INFO_COUNT_DELAY, nativeQuery = true)
    int employeeInfoCountDelay(String id);
    
    // mainCount는 Err + Delay 정 status 'T'
    public static final String EMPLOYEE_INFO_COUNT_ERR_DELAY_MAIN = "SELECT SUM(s.count) as count FROM( SELECT COUNT(*) as count FROM ( SELECT a.*, b.name, b.id FROM ( SELECT * FROM t_api WHERE t_api.is_deleted = 'F') a JOIN employee b ON a.employee_no = b.employee_no) c JOIN t_api_err d ON c.api_no = d.api_no AND d.api_err_status = 'T' WHERE c.id = :id UNION ALL SELECT COUNT(*) as count FROM ( SELECT e.*, f.name, f.id FROM ( SELECT * FROM t_api WHERE t_api.is_deleted = 'F') e JOIN employee f ON e.employee_no = f.employee_no) g JOIN t_api_delay h ON g.api_no = h.api_no AND h.api_delay_status = 'T' WHERE g.id = :id ) s";
    @Query(value = EMPLOYEE_INFO_COUNT_ERR_DELAY_MAIN, nativeQuery = true)
	int employeeInfoCountErrDelayMain(String id);
    
    // subCount는 Err + Delay 부 status 'T'
    public static final String EMPLOYEE_INFO_COUNT_ERR_DELAY_SUB = "SELECT SUM(s.count) as count FROM ( SELECT COUNT(*) as count FROM ( SELECT a.*, b.name, b.id FROM ( SELECT * FROM t_api WHERE t_api.is_deleted = 'F') a JOIN employee b ON a.employee_sub_no = b.employee_no) c JOIN t_api_err d ON c.api_no = d.api_no AND d.api_err_status = 'T' WHERE c.id = :id UNION ALL SELECT COUNT(*) as count FROM ( SELECT e.*, f.name, f.id FROM ( SELECT * FROM t_api WHERE t_api.is_deleted = 'F') e JOIN employee f ON e.employee_sub_no = f.employee_no) g JOIN t_api_delay h ON g.api_no = h.api_no AND h.api_delay_status = 'T' WHERE g.id = :id ) s";
    @Query(value = EMPLOYEE_INFO_COUNT_ERR_DELAY_SUB, nativeQuery = true)
    int employeeInfoCountErrDelaySub(String id);
    
    // NotiCount는 해당 id의 noti 개수
    public static final String EMPLOYEE_INFO_NOTI_COUNT = "SELECT COUNT(*) FROM notification a JOIN ( SELECT c.id, c.employee_no FROM employee c where c.id = :id) b ON a.employee_no = b.employee_no";
    @Query(value = EMPLOYEE_INFO_NOTI_COUNT, nativeQuery = true)
    int employeeInfoNotiCount(String id);
    
    // total의 Err 정 부 리스트
    public static final String EMPLOYEE_INFO_ERR = "SELECT d.api_err_no, d.api_err_code, d.api_err_msg, d.api_err_status, DATE_FORMAT(d.insert_timestamp, '%Y-%m-%d %H:%i:%s') as insert_timestamp, c.api_no, c.api_url, c.method, c.api_category_name_kr, c.service_name_kr, c.service_url, c.category_name_kr FROM ( SELECT a.*, b.name, b.id FROM ( SELECT t_api.*, z.service_no, z.api_category_name_kr, z.service_name_kr, z.service_url, z.category_name_kr FROM ( SELECT * FROM t_api WHERE t_api.is_deleted = 'F') t_api JOIN ( SELECT t_api_category.*, y.service_name_kr, y.service_url, y.category_name_kr FROM t_api_category JOIN ( SELECT t_service.*, t_service_category.category_name_kr FROM t_service JOIN t_service_category ON t_service.service_category_no = t_service_category.service_category_no) y ON t_api_category.service_no = y.service_no ) z ON t_api.api_category_no = z.api_category_no) a JOIN employee b ON a.employee_no = b.employee_no) c JOIN employee e ON c.employee_sub_no = e.employee_no JOIN t_api_err d ON c.api_no = d.api_no WHERE c.id = :id OR e.id = :id";
    @Query(value = EMPLOYEE_INFO_ERR, nativeQuery = true)
	List<Map<String, Object>> employeeInfoErr(String id);

    // total의 Delay 정 부 리스트
    public static final String EMPLOYEE_INFO_DELAY = "SELECT d.api_delay_code, d.api_delay_msg, d.api_delay_no, d.api_delay_status, ROUND(d.api_delay_time, 4) AS api_delay_time, DATE_FORMAT(d.insert_timestamp, '%Y-%m-%d %H:%i:%s') as insert_timestamp, c.api_no, c.api_url, c.method, c.api_category_name_kr, c.service_name_kr, c.service_url, c.category_name_kr FROM ( SELECT a.*, b.name, b.id FROM ( SELECT t_api.*, z.service_no, z.api_category_name_kr, z.service_name_kr, z.service_url, z.category_name_kr FROM ( SELECT * FROM t_api WHERE t_api.is_deleted = 'F') t_api JOIN ( SELECT t_api_category.*, y.service_name_kr, y.service_url, y.category_name_kr FROM t_api_category JOIN ( SELECT t_service.*, t_service_category.category_name_kr FROM t_service JOIN t_service_category ON t_service.service_category_no = t_service_category.service_category_no) y ON t_api_category.service_no = y.service_no ) z ON t_api.api_category_no = z.api_category_no) a JOIN employee b ON a.employee_no = b.employee_no) c JOIN employee e ON c.employee_sub_no = e.employee_no JOIN t_api_delay d ON c.api_no = d.api_no WHERE c.id = :id OR e.id = :id";
    @Query(value = EMPLOYEE_INFO_DELAY, nativeQuery = true)
    List<Map<String, Object>> employeeInfoDelay(String id);

    // mainList의 Err 정 status 'T'
    public static final String EMPLOYEE_INFO_ERR_MAIN = "SELECT d.api_err_no, d.api_err_code, d.api_err_msg, d.api_err_status, DATE_FORMAT(d.insert_timestamp, '%Y-%m-%d %H:%i:%s') as insert_timestamp, c.api_no, c.api_url, c.method, c.api_category_name_kr, c.service_name_kr, c.service_url, c.category_name_kr FROM ( SELECT a.*, b.name, b.id FROM ( SELECT t_api.*, z.service_no, z.api_category_name_kr, z.service_name_kr, z.service_url, z.category_name_kr FROM ( SELECT * FROM t_api WHERE t_api.is_deleted = 'F') t_api JOIN ( SELECT t_api_category.*, y.service_name_kr, y.service_url, y.category_name_kr FROM t_api_category JOIN ( SELECT t_service.*, t_service_category.category_name_kr FROM t_service JOIN t_service_category ON t_service.service_category_no = t_service_category.service_category_no) y ON t_api_category.service_no = y.service_no ) z ON t_api.api_category_no = z.api_category_no) a JOIN employee b ON a.employee_no = b.employee_no) c JOIN t_api_err d ON c.api_no = d.api_no AND d.api_err_status = 'T' WHERE c.id = :id";
    @Query(value = EMPLOYEE_INFO_ERR_MAIN, nativeQuery = true)
    List<Map<String, Object>> employeeInfoErrMain(String id);

    // mainList의 Delay 정 status 'T'
    public static final String EMPLOYEE_INFO_DELAY_MAIN = "SELECT d.api_delay_code, d.api_delay_msg, d.api_delay_no, d.api_delay_status, ROUND(d.api_delay_time, 4) AS api_delay_time, DATE_FORMAT(d.insert_timestamp, '%Y-%m-%d %H:%i:%s') as insert_timestamp, c.api_no, c.api_url, c.method, c.api_category_name_kr, c.service_name_kr, c.service_url, c.category_name_kr FROM ( SELECT a.*, b.name, b.id FROM ( SELECT t_api.*, z.service_no, z.api_category_name_kr, z.service_name_kr, z.service_url, z.category_name_kr FROM ( SELECT * FROM t_api WHERE t_api.is_deleted = 'F') t_api JOIN ( SELECT t_api_category.*, y.service_name_kr, y.service_url, y.category_name_kr FROM t_api_category JOIN ( SELECT t_service.*, t_service_category.category_name_kr FROM t_service JOIN t_service_category ON t_service.service_category_no = t_service_category.service_category_no) y ON t_api_category.service_no = y.service_no ) z ON t_api.api_category_no = z.api_category_no) a JOIN employee b ON a.employee_no = b.employee_no) c JOIN t_api_delay d ON c.api_no = d.api_no AND d.api_delay_status = 'T' WHERE c.id = :id";
    @Query(value = EMPLOYEE_INFO_DELAY_MAIN, nativeQuery = true)
    List<Map<String, Object>> employeeInfoDelayMain(String id);

    // subList의 Err 부 status 'T' 리스트
    public static final String EMPLOYEE_INFO_ERR_SUB = "SELECT d.api_err_code, d.api_err_msg, d.api_err_no, d.api_err_status, DATE_FORMAT(d.insert_timestamp, '%Y-%m-%d %H:%i:%s') as insert_timestamp, c.api_no, c.api_url, c.method, c.api_category_name_kr, c.service_name_kr, c.service_url, c.category_name_kr FROM ( SELECT a.*, b.name, b.id FROM ( SELECT t_api.*, z.service_no, z.api_category_name_kr, z.service_name_kr, z.service_url, z.category_name_kr FROM ( SELECT * FROM t_api WHERE t_api.is_deleted = 'F') t_api JOIN ( SELECT t_api_category.*, y.service_name_kr, y.service_url, y.category_name_kr FROM t_api_category JOIN ( SELECT t_service.*, t_service_category.category_name_kr FROM t_service JOIN t_service_category ON t_service.service_category_no = t_service_category.service_category_no) y ON t_api_category.service_no = y.service_no ) z ON t_api.api_category_no = z.api_category_no) a JOIN employee b ON a.employee_sub_no = b.employee_no) c JOIN t_api_err d ON c.api_no = d.api_no AND d.api_err_status = 'T' WHERE c.id = :id";
    @Query(value = EMPLOYEE_INFO_ERR_SUB, nativeQuery = true)
    List<Map<String, Object>> employeeInfoErrSub(String id);

    // subList의 Delay 부 status 'T' 리스트
    public static final String EMPLOYEE_INFO_DELAY_SUB = "SELECT d.api_delay_code, d.api_delay_msg, d.api_delay_no, ROUND(d.api_delay_time, 4) AS api_delay_time, DATE_FORMAT(d.insert_timestamp, '%Y-%m-%d %H:%i:%s') as insert_timestamp, d.api_delay_status, c.api_no, c.api_url, c.method, c.api_category_name_kr, c.service_name_kr, c.service_url, c.category_name_kr FROM ( SELECT a.*, b.name, b.id FROM ( SELECT t_api.*, z.service_no, z.api_category_name_kr, z.service_name_kr, z.service_url, z.category_name_kr FROM ( SELECT * FROM t_api WHERE t_api.is_deleted = 'F') t_api JOIN ( SELECT t_api_category.*, y.service_name_kr, y.service_url, y.category_name_kr FROM t_api_category JOIN ( SELECT t_service.*, t_service_category.category_name_kr FROM t_service JOIN t_service_category ON t_service.service_category_no = t_service_category.service_category_no) y ON t_api_category.service_no = y.service_no ) z ON t_api.api_category_no = z.api_category_no) a JOIN employee b ON a.employee_sub_no = b.employee_no) c JOIN t_api_delay d ON c.api_no = d.api_no AND d.api_delay_status = 'T' WHERE c.id = :id";
    @Query(value = EMPLOYEE_INFO_DELAY_SUB, nativeQuery = true)
    List<Map<String, Object>> employeeInfoDelaySub(String id);

    // 모든 관리자들에게 알림을 보내기 위해서 : 관리자 아이디 찾기
    public static final String FIND_ADMIN_ID = "SELECT a.* FROM employee a JOIN employee_roles b ON a.employee_no = b.employee_employee_no AND b.roles = 'ROLE_ADMIN'";
    @Query(value = FIND_ADMIN_ID, nativeQuery = true)
	List<Employee> findAdminId();

    // 모든 관리자들에게 알림을 보내기 위해서 : api의 담당 유저 이름 찾기
    public static final String FIND_NAME = "SELECT a.name FROM employee a WHERE a.id = :id";
    @Query(value = FIND_NAME, nativeQuery = true)
	String findName(String id);
}