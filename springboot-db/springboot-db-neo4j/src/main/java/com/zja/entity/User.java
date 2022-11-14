package com.zja.entity;

import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-08-25 10:09
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：基于NodeEntity 使用 @Relationship
 */
@Data
@NodeEntity
public class User {

    //@Id注解的id是对应Neo4j数据的<id>值
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    /**
     * 一对多的关系
     * @Relationship 注解指定与其他节点的关系，指定关系的direction方向，可以是一对一，一对多（List<>）
     */
    @Relationship(type = RelsType.OWN, direction = Relationship.OUTGOING)
    private List<Phone> phones;
}
