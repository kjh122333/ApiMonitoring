package com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.vladmihalcea.hibernate.type.json.JsonStringType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Entity
@Table(name = "t_api")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class ApiDtoSwagger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long api_no;

    @NotNull
    private Long api_category_no;
    private String api_url;

    private String param;
    private Long update_employee_no;
    @NotBlank
    private String method;
    private String parameter_type;
    private String response_type;
    private String is_deleted = "F";

    @CreationTimestamp
    private LocalDateTime insert_timestamp;

    @UpdateTimestamp
    private LocalDateTime updated_timestamp;

    @NotNull
    private Long employee_no;
    @NotNull
    private Long employee_sub_no;


    private String description;
    private String err_status = "F";
    private String delay_status = "F";

    private String response_list;
    
}