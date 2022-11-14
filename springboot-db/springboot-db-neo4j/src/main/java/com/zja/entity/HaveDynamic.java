package com.zja.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-08-25 11:15
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@Data
@EqualsAndHashCode(callSuper = false)
@RelationshipEntity(type = RelsType.HAVE)
public class HaveDynamic <S extends BaseNode, E extends BaseNode> extends BaseRel{
    @Property
    private String createTime;

    @StartNode
    private S startNode;

    @EndNode
    private E endNode;
}
