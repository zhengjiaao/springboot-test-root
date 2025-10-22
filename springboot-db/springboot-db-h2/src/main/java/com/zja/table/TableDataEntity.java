package com.zja.table;

import lombok.Data;

import javax.persistence.*;

/**
 * @Author: zhengja
 * @Date: 2025-10-21 13:51
 */
@Data
@Entity
@Table(name = "table_data")
public class TableDataEntity {
    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private Integer age;
    private String email;

    @Version
    private Long version; // 乐观锁版本号
}
