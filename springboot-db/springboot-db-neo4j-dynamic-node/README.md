# springboot-db-neo4j-dynamic-node

**neo4j动态节点实现**

* [neo4j 官网文档](https://neo4j.com/docs/)
* [spring-boot-starter-data-neo4j](https://docs.spring.io/spring-boot/docs/2.5.5/reference/htmlsingle/#boot-features-neo4j)
* [spring-data-neo4j](https://docs.spring.io/spring-data/neo4j/docs/6.1.5/reference/html/#getting-started)
* [使用 Neo4j 访问数据](https://spring.io/guides/gs/accessing-data-neo4j/)
* [Neo4j Spring动态起始节点类型实现 示例](https://blog.csdn.net/temotemo/article/details/80627284)
* [springboot 连接neo4j认证报错解决方案](https://blog.csdn.net/qq_17351077/article/details/107312945)


## 依赖引入

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-neo4j</artifactId>
        </dependency>
```

## 简单示例

设计可被DomainInfoSchemaBuilder.getNodeByTypeDescriptor解析的基础类型；
基础类型作为所有动态节点类型的基类；
最后代码改写如下：

```java
/**
* 基础节点类型
*/
@Data
public class BaseNode {
    @Id
    @GeneratedValue
    private Long id;
}

/**
* 基础关系类型
*/
@Data
public class BaseRel {
    @Id
    @GeneratedValue
    private Long id;
}

@Data
@EqualsAndHashCode(callSuper = false)
public class Customer extends BaseNode {
    private String name;
}

@Data
@EqualsAndHashCode(callSuper = false)
public class Car extends BaseNode {
    private String brand;
}

@Data
@EqualsAndHashCode(callSuper = false)
@RelationshipEntity(type = RelsType.HAVE)
public class HaveDynamic<S extends BaseNode, E extends BaseNode> extends BaseRel {
    @Property
    private String createTime;

    @StartNode
    private S startNode;

    @EndNode
    private E endNode;
}
```

## 单元测试

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class HaveDynamicTest {
    
    @Autowired
    private HaveDynamicRepository haveDynamicRepository;

    @Test
    public void dynamicTest() throws Exception {
        Customer customerNode = new Customer();
        customerNode.setName("John");
        Car carNode = new Car();
        carNode.setBrand("Benz");

        HaveDynamic<Customer, Car> haveRelationship = new HaveDynamic<>();
        haveRelationship.setCreateTime(LocalDateTime.now().toString());
        haveRelationship.setStartNode(customerNode);
        haveRelationship.setEndNode(carNode);
        haveDynamicRepository.save(haveRelationship);
    }
}
```

HaveDynamic实现支持任何基于BaseNode的开始和结束节点类型，实现动态的构建节点和关系模型，对于同一关系类型，只需要编写一类关系构建的代码即可。

> 提示： 以上动态起始节点类型的实现比较适合OLTP的系统上存在复杂的节点和关系需要做CURD的时候，能够改善整个模型工程的复杂度。
> 但是，在做类似ETL大批量数据插入的场景是不合适的，如果存在大批量数据插入来完成建模的情况，建议优先使用cypher schema来处理，并通过cypher-shell来完成导入，性能会更佳。
