package com.duzon.dbp.apimonitoring.dto;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ServiceApiErrorDto
 */
@ToString
@Getter
@Setter
@Entity
@Table(name = "t_api_err")
public class ApiErrDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long api_err_no;
    
    private Long api_no;

    private Long api_err_code;
    private String api_err_msg;
    private String api_err_comment;
    
    private String api_err_status = "T";

    private String jira_url;
    private String kibana_url;
    private String ref;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @CreationTimestamp
    private LocalDateTime insert_timestamp;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updated_timestamp;

    public ApiErrDto() {
    }
}