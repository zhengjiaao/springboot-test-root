/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-03-10 16:30
 * @Since:
 */
package com.zja.flywaydb.entity;

import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;

@Data
public class Person implements Serializable {
    @Id
    private Long id;
    private String firstname;
    private String lastname;
}
