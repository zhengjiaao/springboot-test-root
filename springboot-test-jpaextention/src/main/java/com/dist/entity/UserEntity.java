package com.dist.entity;

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
@Table(name = "t_user")
@Data
public class UserEntity implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "ID",nullable = true)
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
