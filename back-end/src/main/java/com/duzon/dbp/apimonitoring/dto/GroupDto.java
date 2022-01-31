package com.duzon.dbp.apimonitoring.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * GroupDto
 */
@ToString
@Getter
@Setter
@Entity
@Table(name = "e_group")
public class GroupDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long group_no;

    @NotBlank
    private String group_name;

    private String is_deleted = "F";
}