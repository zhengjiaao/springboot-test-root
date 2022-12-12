package com.zja.entity;

import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-08-25 11:14
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：基础节点类型
 */
@Data
public class BaseNode {
    @Id
    @GeneratedValue
    private Long id;
}
