/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-07-17 13:43
 * @Since:
 */
package com.zja.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: zhengja
 * @since: 2023/07/17 13:43
 */
@Getter
@Setter
//@Entity
//@Table(name = "**_Role", uniqueConstraints = {
//        @UniqueConstraint(name = "uk_role_sort", columnNames = {"sort"})
//})
//@EntityListeners(value = AuditingEntityListener.class)
public class Role {

    /**
     * 唯一主键id
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String remarks;
}