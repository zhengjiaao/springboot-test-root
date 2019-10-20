package com.dist.config;

/**
 * @company: DIST
 * @date：2017/5/17
 * @author: ChenYanping
 * desc
 */
import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
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

    /**
     * //双数据源需要配置一个为默认的datasource
     * @return
     */
    @ConfigurationProperties(prefix = "ds")
    @Bean(name = "dataSource")
    @Primary
    public DataSource druid() {
        return new DruidDataSource();
    }
}
