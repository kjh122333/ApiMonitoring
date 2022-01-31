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
 * ServiceApiResDto
 */
@ToString
@Getter
@Setter
@Entity
@Table(name = "t_api_delay")
public class ApiDelayDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long api_delay_no;

    private Long api_no;

    private Long api_delay_code;
    private Double api_delay_time;
    private String api_delay_comment;

    private String api_delay_status = "T";
    private String api_delay_msg = "응답요청이 지연되었습니다.";

    private String jira_url;
    private String kibana_url;
    private String ref;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @CreationTimestamp
    private LocalDateTime insert_timestamp;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updated_timestamp;

    public ApiDelayDto() {
    }
}