# starter-jta-atomikos

**多数据源+jta分布式事务管理**

## 依赖引入

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jta-atomikos</artifactId>
        </dependency>
        <!--可选的，jpa+mysql/pg库-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.29</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>42.3.4</version>
        </dependency>
```

## 配置 jta-atomikos

JtaTransactionManagerConfig.java

```java
/**
 * 统一事务管理
 */
@Configuration
public class JtaTransactionManagerConfig {
    /**
     * 配置spring的JtaTransactionManager，底层委派给atomikos进行处理
     */
    @Primary
    @Bean(name = "jtaTransactionManager")
    public JtaTransactionManager jtaTransactionManager() {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        UserTransaction userTransaction = new UserTransactionImp();
        return new JtaTransactionManager(userTransaction, userTransactionManager);
    }
}
```

## 配置jpa多数据源

### jpa数据源一

```java
/**
 * 配置JPA的数据持久层，需要配置如下内容：
 *
 * DataSource
 * EntityManager
 * EntityManagerFactoryBean
 * PlatformTransactionManager
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "primaryEntityManagerFactory",
        transactionManagerRef = "primaryTransactionManager",
        basePackages = PrimaryDataSourceConfig.REPOSITORY_PACKAGE)
public class PrimaryDataSourceConfig {

    static final String REPOSITORY_PACKAGE = "com.zja.jta.primary.repository";
    private static final String ENTITY_PACKAGE = "com.zja.jta.primary.entity";

    @Primary
    @Bean(name = "primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "primaryJpaProperties")
    @ConfigurationProperties(prefix = "spring.jpa.primary")
    public JpaProperties jpaProperties() {
        return new JpaProperties();
    }

    /**
     * 实体管理工厂对象
     *
     * 将数据源、连接池、以及其他配置策略进行封装返回给事务管理器
     * 自动装配时当出现多个Bean候选者时，被注解为@Primary的Bean将作为首选者，否则将抛出异常
     */
    @Primary
    @Bean(name = "primaryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("primaryDataSource") DataSource dataSource,
                                                                       @Qualifier("primaryJpaProperties") JpaProperties jpaProperties,
                                                                       EntityManagerFactoryBuilder builder) {
        return builder
                // 设置数据源
                .dataSource(dataSource)
                // 设置jpa配置
                .properties(jpaProperties.getProperties())
                //设置实体类所在位置：类或包 entity
                .packages(ENTITY_PACKAGE)
                //持久化单元名称 用于@PersistenceContext注解获取EntityManager时指定数据源
                .persistenceUnit("primaryPersistenceUnit")
                .build();
    }

    /**
     * 实体对象管理器
     */
    @Primary
    @Bean(name = "primaryEntityManager")
    public EntityManager entityManager(@Qualifier("primaryEntityManagerFactory") EntityManagerFactory factory) {
        return factory.createEntityManager();
    }

    /**
     * 数据源的事务管理器
     */
    @Primary
    @Bean(name = "primaryTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("primaryEntityManagerFactory") EntityManagerFactory factory) {
        return new JpaTransactionManager(factory);
    }

}

```

### jpa数据源二

```java
/**
 * 配置JPA的数据持久层，需要配置如下内容：
 *
 * DataSource
 * EntityManager
 * EntityManagerFactoryBean
 * PlatformTransactionManager
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "secondaryEntityManagerFactory",
        transactionManagerRef = "secondaryTransactionManager",
        basePackages = SecondaryDataSourceConfig.REPOSITORY_PACKAGE)
public class SecondaryDataSourceConfig {

    static final String REPOSITORY_PACKAGE = "com.zja.jta.secondary.repository";
    private static final String ENTITY_PACKAGE = "com.zja.jta.secondary.entity";

    @Bean(name = "secondaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.secondary")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "secondaryJpaProperties")
    @ConfigurationProperties(prefix = "spring.jpa.secondary")
    public JpaProperties jpaProperties() {
        return new JpaProperties();
    }

    /**
     * 实体管理工厂对象
     *
     * 将数据源、连接池、以及其他配置策略进行封装返回给事务管理器
     * 自动装配时当出现多个Bean候选者时，被注解为@Primary的Bean将作为首选者，否则将抛出异常
     */
    @Bean(name = "secondaryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("secondaryDataSource") DataSource dataSource,
                                                                       @Qualifier("secondaryJpaProperties") JpaProperties jpaProperties,
                                                                       EntityManagerFactoryBuilder builder) {
        return builder
                // 设置数据源
                .dataSource(dataSource)
                // 设置jpa配置
                .properties(jpaProperties.getProperties())
                //设置实体类所在位置：类或包 entity
                .packages(ENTITY_PACKAGE)
                //持久化单元名称 用于@PersistenceContext注解获取EntityManager时指定数据源
                .persistenceUnit("secondaryPersistenceUnit")
                .build();
    }

    /**
     * 实体对象管理器
     */
    @Bean(name = "secondaryEntityManager")
    public EntityManager entityManager(@Qualifier("secondaryEntityManagerFactory") EntityManagerFactory factory) {
        return factory.createEntityManager();
    }

    /**
     * 数据源的事务管理器
     */
    @Bean(name = "secondaryTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("secondaryEntityManagerFactory") EntityManagerFactory factory) {
        return new JpaTransactionManager(factory);
    }

}
```

## yml 配置参考

```yaml
spring:
  main:
    allow-bean-definition-overriding: true
  datasource:
    primary:
      jdbc-url: jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=true&serverTimezone=UTC #&rewriteBatchedStatements=true
      username: test
      password: pass
      driver-class-name: com.mysql.cj.jdbc.Driver
    secondary:
      jdbc-url: jdbc:postgresql://localhost:5433/test
      username: test
      password: pass
      driver-class-name: org.postgresql.Driver
  jpa:
    primary:
      show-sql: true
      properties:
        hibernate:
          hbm2ddl:
            auto: update
            naming:
              physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
    secondary:
      show-sql: true
      properties:
        hibernate:
          hbm2ddl:
            auto: update
            naming:
              physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl

```

## 测试示例

```java
@Service
public class JtaService {

    //无注解时       抛异常，全部存储
//    @Transactional(rollbackFor = Exception.class)     //使用jta-atomikos时，抛异常，全部不会存储；不使用jta-atomikos时,抛异常,user不会存储，goods会存储；
//    @Transactional(value = "primaryTransactionManager", rollbackFor = Exception.class)  //抛异常，user不会存储，goods会存储
//    @Transactional(value = "secondaryTransactionManager", rollbackFor = Exception.class)  //抛异常，user会存储，goods不会存储
//    @Transactional(value = "jtaTransactionManager", rollbackFor = Exception.class)      //抛异常，全部存储
    public void add_rollback_example() {

        PrimaryEntity primaryEntity = new PrimaryEntity();
        primaryEntity.setName("数据源1-实体类");
        primaryEntity.setCreateTime(new Date());
        primaryRepository.save(primaryEntity);

        SecondaryEntity secondaryEntity = new SecondaryEntity();
        secondaryEntity.setName("数据源2-实体类");
        secondaryEntity.setCreateTime(new Date());
        secondaryRepository.save(secondaryEntity);

        //故意抛出异常测试
        int i = 10 / 0;
    }

}
```
