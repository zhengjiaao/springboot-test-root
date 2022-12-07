/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-12-05 15:22
 * @Since:
 */
package com.zja.entity;

import lombok.Data;

import javax.persistence.*;


@Data
@Entity
@Table(name = "t_order")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)     //主键由程序控制，执行save()方法，在insert前会校验主键是否为null
//    @GeneratedValue(strategy = GenerationType.IDENTITY) //主键由数据库自动生成（主要是自动增长型），执行save()方法，在insert前不校验主键是否为null
    private Long orderId;
    private int userId;
    private long addressId;
    private String creator;
    private String updater;
}
