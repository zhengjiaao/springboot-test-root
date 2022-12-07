/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-25 16:05
 * @Since:
 */
package com.zja.jta.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

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
