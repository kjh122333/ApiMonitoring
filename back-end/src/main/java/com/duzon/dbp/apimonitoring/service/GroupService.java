package com.duzon.dbp.apimonitoring.service;

import java.util.Iterator;
import java.util.List;

import com.duzon.dbp.apimonitoring.advice.exception.GroupNotFoundWithNumberError;
import com.duzon.dbp.apimonitoring.advice.exception.SameNameError;
import com.duzon.dbp.apimonitoring.dto.GroupDto;
import com.duzon.dbp.apimonitoring.repo.GroupRepo;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

/**
 * GroupService
 */
@Service
public class GroupService {

    @Autowired
    GroupRepo groupRepo;

    public List<GroupDto> groupNameList() {
        List<GroupDto> list = groupRepo.findAll();
        Iterator<GroupDto> iter = list.iterator();
        while( iter.hasNext() ){
            GroupDto dto = iter.next();
            if (dto.getGroup_no() == 0) {
                iter.remove();
            }
        }
        return list;
    }

    public void save(GroupDto dto) {
        if (groupRepo.NameCheck(dto.getGroup_name()) == null) {
            groupRepo.save(dto);
        } else {
            throw new SameNameError();
        }
    }

	public void groupUpdate(long group_no, GroupDto dto) {
        GroupDto groupDto = groupRepo.findById(group_no).orElseThrow(GroupNotFoundWithNumberError::new);
        groupDto.setGroup_name(dto.getGroup_name());
        groupDto.setIs_deleted(dto.getIs_deleted());
        
        if (groupRepo.NameCheckUp(dto.getGroup_name(), dto.getGroup_no()) == null) {
            groupRepo.save(groupDto);
        } else {
            throw new SameNameError();
        }
	}
}