package com.duzon.dbp.apimonitoring.dto;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ApiCategory
 */
@ToString
@Getter
@Setter
@Entity
@Table(name = "t_api_category")
public class ApiCategoryDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long api_category_no;

    private Long service_no;

    private String api_category_name_kr;
    
    private String is_deleted = "F";

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @CreationTimestamp
    private LocalDateTime insert_timestamp;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @UpdateTimestamp
    private LocalDateTime updated_timestamp;
}