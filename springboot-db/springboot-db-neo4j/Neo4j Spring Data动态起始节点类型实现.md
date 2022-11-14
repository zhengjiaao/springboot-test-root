# Neo4j Spring Data动态起始节点类型实现

[toc]

在spring-boot里我们可以基于neo4j-spring-data，编写类似关系数据库风格的JPA操作，实现对neo4j数据的CURD。研究neo4j-spring-data底层API的实现发现，本质上还是将JPA的操作翻译为cypher的语句，并基于neo4j的bolt的协议将操作语句发送至远程的数据库服务器来执行，anyway，这块我们不进一步展开了，我们来看看怎么利用neo4j-spring-data的API高效地实现工程化模型。



## neo4j-spring-data创建节点和关系

我们利用Neo4j建模的时候，经常会发现，同样的Relationship里，可能连接的节点是不一样的，比如OWN（拥有）这样的关系，比如：
用户拥有手机：User-[OWN]->Phone
用户拥有银行卡：User-[OWN]->Card

在neo4j-spring-data中，主要有2种形式来创建节点和节点之间的关系。



#### 基于NodeEntity

这里我们简单举一个简单例子：User-[OWN]->Phone

1. 定义好@NodeEntity，任何被@NodeEntity的POJO将会被作为neo4j的一个节点类型处理；
2. 定义好对应的Repository；
模型定义：

```java
//所有关系标签常量
//这里使用了interface成员变量是常量的特性，比class使用static final String XX="xx"简洁 
public interface RelsType {
    String OWN  = "OWN";
    String HAVE = "HAVE";
}

@Data
@NodeEntity
public class User {
    @Id
    @GeneratedValue
    private Long id;
	private String name;

    /**
     * 一对多的关系
     */
    @Relationship(type = RelsType.OWN, direction = Relationship.OUTGOING)
    private List<Phone> phones;

}

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
```


定义好对应的Repository：

```java
public interface UserRepository extends Neo4jRepository：<User, Long> {
}
```

注意：从4.2版本之后，API做了修改GraphRepository改为Neo4jRepository，参数也不一样，如果看到之前的博客的代码，请注意修改。
Base class for repositories GraphRepository has been renamed Neo4jRepository and parameter types change from <T> to<T, ID>

总结要点：

POJO中的属性将作为Node的properties
@Relationship注解指定与其他节点的关系，指定关系的direction方向，可以是一对一，一对多（List<>）
@Id注解的id是对应Neo4j数据的<id>值
测试代码：

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class Neo4jSpringDataTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void userOwnPhoneTest() throws Exception {
        Phone phone = new Phone();
        phone.setPhoneNo("13800123456");

        User user = new User();
        user.setName("Jack");
        user.setPhones(Lists.newArrayList(phone));

        userRepository.save(user);
    }
}
```


运行一切正常的话，数据库将新建User-[OWN]->Phone：2个节点和1个关系。

思考：
走到这里我们可能觉得一切都正常，按照neo4j-spring-data的API来完成节点和关系的创建也比较简洁。等等，让我们思考一下可能遇到的问题：

如果User不止和Phone有OWN连接关系，还跟其他节点有其他关系，那么就需要在User的POJO里新加其他节点关系的代码，整个POJO就会越来越膨胀，这样代码将越来越难维护和理解；

比如，可能会出现下面类似臃肿的代码：

```java
@Data
@NodeEntity
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @Relationship(type = RelsType.OWN, direction = Relationship.OUTGOING)
    private List<Phone> phones;

    @Relationship(type = RelsType.HAVE, direction = Relationship.OUTGOING)
    private List<Car> cars;

    @Relationship(type = RelsType.BUY, direction = Relationship.OUTGOING)
    private List<House> houses;

    //其他关系
...
}
```
也就是说，基于@NodeEntity及内置@Relationship的实现方式，在遇到可能存在多关系的场景，工程实现可能会不那么优雅和易维护。



#### 基于RelationshipEntity

为了测试代码，我们新举Customer-[HAVE]->Car这个例子，neo4j-spring-data为了支持多个角度构建关系的灵活性出发，即从关系的角度出发，也能完成节点和关系的构建，这个机制就是RelationshipEntity。
模型定义：

```java
@Data
public class Customer {
    @Id
    @GeneratedValue
    private Long id;
	private String name;
}

@Data
public class Car {
    @Id
    @GeneratedValue
    private Long id;
    private String brand;
}
```


```java
@Data
@RelationshipEntity(type = RelsType.HAVE)
public class Have {
    @Id
    @GeneratedValue
    private Long id;
    @Property
    private String createTime;
    @StartNode
    private Customer customer;
    @EndNode
    private Car car;
}
```
定义好对应的Repository:

```java
public interface HaveRepository extends Neo4jRepository<Have, Long> {
}
```

测试代码：

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class HaveTest {

    @Autowired
    private HaveRepository haveRepository;

    @Test
    public void customerHaveCarTest() throws Exception {
        Customer customer = new Customer();
        customer.setName("Tom");

        Car car = new Car();
        car.setBrand("BMW");

        Have have = new Have();
        have.setCustomer(customer);
        have.setCar(car);
        have.setCreateTime(LocalDateTime.now().toString());

        haveRepository.save(have);
	}
}
```
总结要点：

@RelationshipEntity注解的POJO里有2个关键的内部field的注解：@StartNode和表示关系的起始节点，@EndNode表示关系的结束节点，即起始节点、结束节点和关系类型构建完整构建一个关系，这里只支持一对一的情况；
@Property注解的成员变量会被作为关系的属性处理；
@StartNode和@EndNode注解的成员变量可以是没有被@NodeEntity注解的POJO，但是一定要有指定@Id，否则会报错；
很明显，如果要完成一对多关系的构建，需要多次执行上面关系构建的操作（不过这个不是大问题）；
思考：

基于@RelationshipEntity节点关系构建需要显示指明对应的起始节点的类型，如果需要构建的模型中存在大量不同的关系，那么将要实现大量类似Have和HaveRepository的代码。这在工程上也是不现实的实现，那么我们是否可以使用动态节点类型来作为起始节点和结束节点呢？答案是可以的！需要我们动手封装一下实现。



#### 基于RelationshipEntity的动态节点类型

从上面2种实现节点关系构建的DEMO中我们可以发现，当我们的关系类型和节点类型越来越多时，需要编写和维护的代码也越来越多，我们需要控制这种复杂性。



#### 一切只有Relationship

我们将图构建的时候从关系类型的角度去抽象，因为关系类型是可以枚举的，不管开始节点或者结束节点是什么类型的节点，他们之间只通过关系类型建立关系，比如：

用户拥有手机：User-[OWN]->Phone
用户拥有银行卡：User-[OWN]->Card
用户拥有车子：User-[OWN]->Car
那么这里只需要对OWN建模即可，开始节点和结束节点的类型可动态变化，那么我们的建模系统数量就可以维持在关系类型的水平上，节点类型可以灵活扩展。

*思考

建模的思路有了，那怎么实现动态节点类型呢？用Java泛型啊！思路是对的，让我们试试吧！

测试代码：

```java
//起始节点和结束节点都为动态类型的泛型实现
@Data
@RelationshipEntity(type = RelsType.HAVE)
public class HaveDynamic<S, E> {
    @Id
    @GeneratedValue
    private Long id;
    @Property
    private String createTime;
    @StartNode
    private S startNode;
    @EndNode
    private E endNode;
}

//实现对应的Repository
public interface HaveDynamicRepository extends Neo4jRepository<HaveDynamic, Long> {
}

//测试代码：
@RunWith(SpringRunner.class)
@SpringBootTest
public class HaveDynamicTest {

    @Autowired
    private HaveDynamicRepository haveDynamicRepository;

    @Test
    public void dynamicTest() throws Exception {
        Customer customer = new Customer();
        customer.setName("John");
        Car car = new Car();
        car.setBrand("Benz");

        HaveDynamic<Customer, Car> haveDynamic = new HaveDynamic<>();
        haveDynamic.setCreateTime(LocalDateTime.now().toString());
        haveDynamic.setStartNode(customer);
        haveDynamic.setEndNode(car);

        haveDynamicRepository.save(haveDynamic);
    }
}
```
很不幸的，Spring-Boot启动注入bean的阶段就失败了，报错信息如下：

...省略
Caused by: org.springframework.beans.BeanInstantiationException: Failed to instantiate [org.neo4j.ogm.session.SessionFactory]: Factory method 'sessionFactory' threw exception; nested exception is java.lang.NullPointerException
    at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:185)
    at org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(ConstructorResolver.java:579)
    ... 55 more
Caused by: java.lang.NullPointerException
    at org.neo4j.ogm.metadata.schema.DomainInfoSchemaBuilder.getNodeByTypeDescriptor(DomainInfoSchemaBuilder.java:145)
    at org.neo4j.ogm.metadata.schema.DomainInfoSchemaBuilder.getNodeByFieldAndContainingClass(DomainInfoSchemaBuilder.java:139)
...省略

很明显的是，因为DomainInfoSchemaBuilder.getNodeByTypeDescriptor找不到HaveDynamic的节点类型，因此在初始化org.neo4j.ogm.session.SessionFactory失败了，也就是说@RelationshipEntity里指定@StartNode和@EndNode的节点类型需要是明确的类型，否则无法解析。

深入分析：

（1）在neo4j-spring-data里，java class定义的类型会被解析为对应Neo4j里节点的label，而Neo4j的Node是支持多层label的；
（2）如果java class定义的节点类型存在多重继承，通过neo4j-spring-data保存在数据库时会被解析为存在多个label的node；
（3）泛型的实现在Java编译的时候会发生类型的擦除（默认会擦除为Object），我们可以通过< Y extends X>通配符限制的方式来控制类型擦除后为X类型的泛型保证；



#### 最终解决方案：

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


```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class HaveDynamicTest {
    
    @Autowired
    private HaveDynamicRepository haveDynamicRepository;

    @Test
    public void dynamicTest() throws Exception {
        Customer customer = new Customer();
        customer.setName("John");
        Car car = new Car();
        car.setBrand("Benz");

        HaveDynamic<Customer, Car> haveDynamic = new HaveDynamic<>();
        haveDynamic.setCreateTime(LocalDateTime.now().toString());
        haveDynamic.setStartNode(customer);
        haveDynamic.setEndNode(car);
        haveDynamicRepository.save(haveDynamic);
    }
}
```
HaveDynamic实现支持任何基于BaseNode的开始和结束节点类型，实现动态的构建节点和关系模型，对于同一关系类型，只需要编写一类关系构建的代码即可。

提示： 以上动态起始节点类型的实现比较适合OLTP的系统上存在复杂的节点和关系需要做CURD的时候，能够改善整个模型工程的复杂度。但是，在做类似ETL大批量数据插入的场景是不合适的，如果存在大批量数据插入来完成建模的情况，建议优先使用cypher schema来处理，并通过cypher-shell来完成导入，性能会更佳。

以上实现的项目源码地址：https://github.com/zengxijin/JavaHub/tree/master/neo4j-spring-data-tuning
提示：

代码使用了lombok插件来完成代码的getter和setter，需要在IDE安装对应的lombok插件和开启Annotation处理才能跑测试；
测试时，请记得修改application.properties里的neo4j远程服务器上的ip，用户名和密码；