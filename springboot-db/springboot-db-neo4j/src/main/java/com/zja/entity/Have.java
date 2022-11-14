package com.zja.entity;

import lombok.Data;
import org.neo4j.ogm.annotation.*;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-08-25 11:25
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：基于RelationshipEntity
 */
@Data
@NodeEntity
@RelationshipEntity(type = RelsType.HAVE) //只支持一对一的情况,要完成一对多关系的构建，需要多次执行上面关系构建的操作（不过这个不是大问题）
public class Have {
    @Id
    @GeneratedValue
    private Long id;

    //@Property注解的成员变量会被作为关系的属性处理
    @Property
    private String createTime;

    //@StartNode 和 @EndNode 注解的成员变量可以是没有被@NodeEntity注解的POJO，但是一定要有指定@Id，否则会报错
    //@StartNode和表示关系的起始节点
    @StartNode
    private Customer customer;

    //@EndNode表示关系的结束节点，即起始节点、结束节点和关系类型构建完整构建一个关系，这里只支持一对一的情况
    @EndNode
    private Car car;
}
