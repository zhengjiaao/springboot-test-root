/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-12-05 15:21
 * @Since:
 */
package com.zja.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_address")
public class AddressEntity {
    @Id
    @GeneratedValue//(strategy = GenerationType.IDENTITY)
    private Long addressId;
    private String addressName;
}
