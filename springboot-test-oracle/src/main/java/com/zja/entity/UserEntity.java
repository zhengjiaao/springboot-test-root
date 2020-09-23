package com.zja.entity;

import lombok.Data;
import org.apache.solr.client.solrj.beans.Field;
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
@Entity(name = "UserEntity")
@Component
@Table(name="t_user")
@Data
public class UserEntity implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "ID",nullable = true)
    @Field("id")
    private Long id;
    @Column(name="GUID")
    private String guid;
    @Column(name = "NAME",nullable = true,length = 50)
    @Field("user_name")
    private String name;
    @Column(name = "AGE",nullable = true,length = 50)
    @Field("user_age")
    private String age;
    @Column(name = "CREATETIME",nullable = true)
    private Date createTime;
    @Column(name = "LASTUPDATETIME",nullable = true)
    @Field("user_lastUpdateTime")
    private Date lastUpdateTime;

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", guid='" + guid + '\'' +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", createTime=" + createTime +
                ", lastUpdateTime=" + lastUpdateTime +
                '}';
    }
}
