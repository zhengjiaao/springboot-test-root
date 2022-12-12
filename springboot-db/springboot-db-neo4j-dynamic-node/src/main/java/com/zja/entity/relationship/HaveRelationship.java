package com.zja.entity.relationship;

import com.zja.entity.BaseNode;
import com.zja.entity.BaseRelationship;
import com.zja.entity.RelsType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.*;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-08-25 11:15
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：[拥有关系=HAVE]
 * 实现动态的构建节点和关系模型
 * 优点：对于同一关系类型，只需要编写一类关系构建的代码即可
 */
@Data
@EqualsAndHashCode(callSuper = false)
@RelationshipEntity(type = RelsType.HAVE)
//@RelationshipEntity(type = HaveDynamicRel.REL_TYPE)
//@RelationshipEntity(type = "HaveDynamic")
public class HaveRelationship<S extends BaseNode, E extends BaseNode> extends BaseRelationship {

    /**
     * 关系类型(关系属性) 拥有=OWN
     */
    static final String REL_TYPE = "OWN";

    /**
     * 关系名称(关系属性)
     */
    @Property
    private String relName;

    /**
     * 关系类型(空间 1、属性 2、业务 3)
     */
    @Index
    private Integer relType = 3;

    /**
     * 创建时间(关系属性)
     */
    @Property
    private String createTime;

    /**
     * 起始节点
     */
    @StartNode
    private S startNode;

    /**
     * 结束节点
     */
    @EndNode
    private E endNode;

}
