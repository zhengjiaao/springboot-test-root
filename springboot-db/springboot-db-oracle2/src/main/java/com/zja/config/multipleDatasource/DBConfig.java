package com.zja.config.multipleDatasource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @author zhaohj
 * @date 2019/1/8 下午10:16
 * @desc 将设置参数的druid的数据源注册到IOC容器中
 */
@Configuration
public class DBConfig {

    @Primary
    @Bean(name = "primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.first")
    public DataSource druid() {
        return DataSourceBuilder.create().build();
    }

    
    @Bean(name = "secondDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.second")
    @ConditionalOnExpression("${spring.datasource.second.enabled}")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }
}
