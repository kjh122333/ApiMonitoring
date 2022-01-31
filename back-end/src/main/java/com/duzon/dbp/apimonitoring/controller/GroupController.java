package com.duzon.dbp.apimonitoring.controller;

import javax.validation.Valid;

import com.duzon.dbp.apimonitoring.dto.GroupDto;
import com.duzon.dbp.apimonitoring.dto.message.Common;
import com.duzon.dbp.apimonitoring.dto.message._List;
import com.duzon.dbp.apimonitoring.service.GroupService;
import com.duzon.dbp.apimonitoring.service.response.ResponseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * GroupConreoller
 */
@Api(tags = { "8. Group" })
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
public class GroupController {

    @Autowired
    GroupService groupService;

    @Autowired
    ResponseService responseService;

    @ApiOperation(value = "Group Name List", notes = "Group Name 리스트")
    @GetMapping("/groupname")
    public _List<GroupDto> groupNameList(){
        return responseService.getResult_List(groupService.groupNameList());
    }

    @ApiOperation(value = "Group Create", notes = "Group 생성")
    @PostMapping("/group")
    public Common groupCreate(@ApiParam(value = "Group 생성 객체", required = true) @RequestBody @Valid GroupDto dto){
        groupService.save(dto);
        return responseService.getResultSuccess();
    }

    @ApiOperation(value = "Group Update", notes = "Group 수정")
    @PutMapping("/group/{idx}")
    public Common groupUpdate(
        @ApiParam(value = "그룹 번호", required = true) @PathVariable long idx,
        @ApiParam(value = "그룹 수정 객체", required = true)@RequestBody @Valid GroupDto dto
        ){
            groupService.groupUpdate(idx, dto);
        return responseService.getResultSuccess();
    }
}