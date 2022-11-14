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
 * Date: 2021-08-25 10:10
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@Data
@NodeEntity
public class Phone {

    @Id
    @GeneratedValue
    private Long id;
    private String phoneNo;

    /**
     * 一对多的关系
     */
    @Relationship(type = RelsType.OWN, direction = Relationship.INCOMING)
    private List<User> users;
}
