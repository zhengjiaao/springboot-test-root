package com.zja.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyGroup;
import org.hibernate.engine.spi.PersistentAttributeInterceptable;
import org.hibernate.engine.spi.PersistentAttributeInterceptor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Blob;
import java.time.LocalDateTime;

/**
 * Project 实体类
 *
 * @author: zhengja
 * @since: 2024/07/01 15:23
 */
@Getter
@Setter
@Entity
@Table(name = "t_project")
@EntityListeners(value = AuditingEntityListener.class)
public class Project {

    @Id
    private String id = String.valueOf(System.currentTimeMillis());

    /**
     * 名称
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * 项目周期
     */
    private int cycle;

    /**
     * json 字符串存储字段
     * BLOB类型：BLOB、MEDIUMBLOB、LONGBLOB
     */
    @Lob
    // @Lazy
    @Basic(fetch = FetchType.LAZY) // FetchType.LAZY 似乎未生效
    @Column(name = "config_json") // 默认：longblob，jpa默认使用MyISAM 引擎，无法自动创建表结构，jpa改为innodb引擎
    private JSONObject configJson;

    /**
     * 文本 or json字符串存储字段
     * TEXT类型：TEXT、MEDIUMTEXT、LONGTEXT
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "config_text") // 默认：longtext，jpa默认使用MyISAM 引擎，无法自动创建表结构，jpa改为innodb引擎
    private String configText;

    /**
     * 当前状态 正常 1，已删除 -1
     */
    @Column(name = "state")
    private Integer state = 1;

    /**
     * 是否为内置（true 系统自动创建）
     */
    @Column(name = "internal")
    private Boolean internal = false;

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