package com.duzon.dbp.apimonitoring.dto.socket;

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
 * NotificationDto
 */
@ToString
@Getter
@Setter
@Entity
@Table(name = "notification")
public class NotificationDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long notify_no;

    private long employee_no;
    private String is_read = "F";
    private String is_confirm = "F";
    private long err_no;
    private long delay_no;
    private long service_no;
    private long api_category_no;
    private long api_no;

    private String type;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @CreationTimestamp
    private LocalDateTime insert_timestamp;

}