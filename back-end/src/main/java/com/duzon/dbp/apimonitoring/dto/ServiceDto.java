package com.duzon.dbp.apimonitoring.dto;

import java.time.LocalDateTime;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
 * ServiceDto
 */
@ToString
@Getter
@Setter
@Entity
@Table(name = "t_service")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class ServiceDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long service_no;

    @NotNull
    private Long service_category_no;

    @NotBlank
    private String service_name_kr;

    @NotBlank
    private String service_url;

    @NotNull
    private Long group_no;

    @NotNull
    private Long service_state = (long) 1;

    private String is_deleted = "F";

    private String service_code;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @CreationTimestamp
    private LocalDateTime insert_timestamp;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @UpdateTimestamp
    private LocalDateTime updated_timestamp;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private Map<String, Object> definitions;
}