package com.zja.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 产品 实体类
 *
 * @author: zhengja
 * @since: 2026/02/06 10:30
 */
@Getter
@Setter
@Entity
@Table(name = "t_product_info")
@EntityListeners(value = AuditingEntityListener.class)
public class Product {

    @Id
    // @GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "VARCHAR(36)")
    private String id;

    /**
     * 产品名称
     */
    @Column(name = "product_name", nullable = false)
    private String productName;

    /**
     * 产品编码
     */
    @Column(name = "product_code", nullable = false, unique = true)
    private String productCode;

    /**
     * 产品价格
     */
    @Column(name = "price")
    private Double price;

    /**
     * 产品描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 产品分类
     */
    @Column(name = "category")
    private String category;

    /**
     * 产品状态(0-禁用,1-启用)
     */
    @Column(name = "status")
    private Integer status;

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