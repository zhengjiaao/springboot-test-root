/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-18 16:20
 * @Since:
 */
package com.zja.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "t_user")
public class User {

    @Id
    private String id = UUID.randomUUID().toString();

    private String username;
    private String password;
    private int age;

    private LocalDateTime createTime;
}
