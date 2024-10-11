# springboot-db-dm

- [达梦数据库](https://eco.dameng.com/)
- [达梦数据库-技术文档](https://eco.dameng.com/document/dm/zh-cn/start/index.html)
- [达梦数据库-试用下载](https://eco.dameng.com/download/?_blank)

## 安装

## springboot jpa 集成达梦数据库

依赖：

```xml

<dependencies>
    <dependency>
        <groupId>com.dameng</groupId>
        <artifactId>DmJdbcDriver18</artifactId>
        <version>8.1.3.140</version>
    </dependency>
    <dependency>
        <groupId>com.dameng</groupId>
        <artifactId>DmDialect-for-hibernate5.6</artifactId>
        <version>8.1.3.140</version>
    </dependency>
</dependencies>
```

配置：

```yaml
spring:
  datasource:
    username: test
    password: pass
    url: jdbc:dm://192.168.1.*:5236
    driver-class-name: dm.jdbc.driver.DmDriver
  jpa:
    database-platform: org.hibernate.dialect.DmDialect
    hibernate:
      ddl-auto: update
    # sql 日志
    show-sql: true # 这将使Hibernate显示它生成的SQL语句到标准输出，默认是false
```

示例：

```java

@Getter
@Setter
@Entity
@Table(name = "t_project")
@EntityListeners(value = AuditingEntityListener.class)
public class Project implements Serializable/*, PersistentAttributeInterceptable */ {

    @Id
    private String id = String.valueOf(System.currentTimeMillis());

    /**
     * 名称
     */
    @Column(nullable = false, length = 100)
    private String name;
}
```

测试：

```java

@SpringBootTest
public class ProjectDaoTest {

    @Autowired
    ProjectRepo repo;

    @Test
    public void add_test() {
        // repo.deleteAll(); // 感到疑惑是添加清理数据时，会导致下放插入数据保存失败，创建时间是NULL异常

        for (int i = 1; i < 10; i++) {
            Project p = new Project();
            p.setName("名称-" + i);
            repo.save(p);
        }
    }
}
```

