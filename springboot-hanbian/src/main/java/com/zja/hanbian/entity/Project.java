package com.zja.hanbian.entity;

import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * Project 实体类
 *
 * @author: zhengja
 * @since: 2024/11/11 10:12
 */
@Getter
@Setter
@Entity
@Table(name = "t_project_1")
@EntityListeners(value = AuditingEntityListener.class)
public class Project {

    @Id
    private String id = String.valueOf(System.currentTimeMillis());

    /**
     * 名称
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 创建日期
     */
    @Column(name = "create_time", nullable = false)
    @CreatedDate
    private LocalDateTime createTime;

    /**
     * 最后一次修改时间
     */
    @Column(name = "last_modified_date")
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
}