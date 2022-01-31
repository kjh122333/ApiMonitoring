package com.duzon.dbp.apimonitoring.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vladmihalcea.hibernate.type.json.JsonStringType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ServiceApiDto
 */
@ToString
@Getter
@Setter
@Entity
@Table(name = "t_api")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class ApiDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long api_no;

    private Long api_category_no;

    private String api_url;

    private String method;

    private String description;

    private Long employee_no;
    private Long employee_sub_no;
    private Long update_employee_no;

    private String is_deleted = "F";
    private String err_status = "F";
    private String delay_status = "F";

    private String parameter_type;
    private String response_type;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private List<Map<String, Object>> param;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private Map<String, Object> response_list;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @CreationTimestamp
    private LocalDateTime insert_timestamp;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @UpdateTimestamp
    private LocalDateTime updated_timestamp;
}