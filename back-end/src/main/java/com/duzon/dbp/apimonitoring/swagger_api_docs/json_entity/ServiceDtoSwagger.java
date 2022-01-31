package com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
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
public class ServiceDtoSwagger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long service_no;

    @NotNull
    private Long service_category_no;

    @NotBlank
    private String service_name_kr;

    private Long group_no;

    private String is_deleted = "F";

    private Long service_state = (long) 0;

    private String service_url;     // Host+Path
    private String service_code;    // Path

    @CreationTimestamp
    private LocalDateTime insert_timestamp;

    @UpdateTimestamp
    private LocalDateTime updated_timestamp;
    private String definitions;

}