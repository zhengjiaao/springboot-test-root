package com.zja.entity;

import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

import java.io.Serializable;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-08-25 9:10
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：Neo4j 默认存在
 */
@Data
@NodeEntity
public class Person implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
}
