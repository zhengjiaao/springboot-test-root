package com.zja.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * User 实体类
 *
 * @author: zhengja
 * @since: 2025/03/05 13:57
 */
@Getter
@Setter
@Entity
@Table(name = "t_user_info")
@EntityListeners(value = AuditingEntityListener.class)
public class User {

    @Id
    private String id = String.valueOf(System.currentTimeMillis());

    /**
     * 名称
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * 用户地址
     */
    private String address;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户年龄
     */
    private Integer age;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 排列顺序
     */
    @Column(name = "sort", nullable = false, unique = true)
    private Long sort = System.currentTimeMillis();

    /**
     * 创建时间
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