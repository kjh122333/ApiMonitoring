package com.duzon.dbp.apimonitoring.dto;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * T_serviceDto
 */
@ToString
@Getter
@Setter
@Entity
@Table(name = "t_service_category")
public class ServiceCategoryDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long service_category_no;

    @NotBlank
    private String category_name_kr;

    private String is_deleted = "F";

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @CreationTimestamp
    private LocalDateTime insert_timestamp;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @UpdateTimestamp
    private LocalDateTime updated_timestamp;
}