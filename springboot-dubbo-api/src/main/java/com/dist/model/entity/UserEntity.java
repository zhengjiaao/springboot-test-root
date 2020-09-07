package com.dist.model.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Component;

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
@Component
@Table(name="t_user")
@Data
public class UserEntity implements Serializable {
    /*@Id
    @Column(name = "ID",nullable = true)
    @GeneratedValue*/

    @Id
    @Column(name = "ID",nullable = true)
    @GenericGenerator(name = "MyId", strategy = "com.dist.utils.MyIdGenerator" )
    @GeneratedValue(generator = "MyId")
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
