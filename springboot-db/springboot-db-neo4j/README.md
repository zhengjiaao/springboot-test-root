# springboot-db-neo4j

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

```java
@Data
@NodeEntity
public class Person implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
}

public interface PersonRepository extends Neo4jRepository<Person, Long> {

    List<Person> findByName(String name);
}

```

测试类

```java
@SpringBootTest
public class PersonRepositoryTests {

    @Resource
    private PersonRepository personRepository;

    @Test
    void save() {
        Person person = new Person();
        person.setId(1L);
        person.setName("lisi");
        personRepository.save(person);
    }

    @Test
    void findAll() {
        List<Person> all = (List<Person>)personRepository.findAll();
        System.out.println(all);
    }
}

```
