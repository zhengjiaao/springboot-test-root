package com.dist.entity.gxtz;

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
@Table(name="gxtz_user")
@Data
@SequenceGenerator(name = "ID_SEQ_DOC", sequenceName = "SEQUENCE_GXTZ", allocationSize = 1)
public class GxtzUserEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ID_SEQ_DOC")
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
