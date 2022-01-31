package com.duzon.dbp.apimonitoring.repo;

import com.duzon.dbp.apimonitoring.dto.GroupDto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * GroupRepo
 */
@Repository
public interface GroupRepo extends JpaRepository<GroupDto, Long> {

	// Name 중복 확인 (생성 때)
    public static final String FIND_NAME_CHECK = "SELECT a.group_name FROM e_group a WHERE a.is_deleted = 'F' AND a.group_name = :group_name";
    @Query(value = FIND_NAME_CHECK, nativeQuery = true)
    String NameCheck(String group_name);

    // Name 중복 확인 (업데이트 때)
    public static final String FIND_NAME_CHECK_UP = "SELECT a.group_name FROM e_group a WHERE a.is_deleted = 'F' AND a.group_name = :group_name AND NOT a.group_no = :group_no";
    @Query(value = FIND_NAME_CHECK_UP, nativeQuery = true)
    String NameCheckUp(String group_name, long group_no);
}