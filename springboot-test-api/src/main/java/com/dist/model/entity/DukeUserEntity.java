package com.dist.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @program: springbootdemo
 * @Date: 2018/12/26 10:03
 * @Author: Mr.Zheng
 * @Description:
 */
@Entity
@Table(name="duke_user")
@Data
@SequenceGenerator(name = "ID_SEQ_DOC", sequenceName = "HIBERNATE_SEQUENCES", allocationSize = 1)
public class DukeUserEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ID_SEQ_DOC")
    //@GeneratedValue //默认序列名称 hibernate_sequences
    @Column(name = "ID",nullable = false)
    private Long id;
    @Column(name="GUID")
    private String guid;
    @Column(name = "NAME",nullable = true,length = 50)
    private String name;
    @Column(name = "AGE",nullable = true,length = 50)
    private String age;
    @Column(name = "CREATETIME",nullable = true)
    private Date createTime;
}
