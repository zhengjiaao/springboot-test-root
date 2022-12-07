/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-25 15:58
 * @Since:
 */
package com.zja.jta.secondary.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "secondary_entity")
public class SecondaryEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Date createTime;
}
