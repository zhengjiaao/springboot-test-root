package com.zja.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * 数据源配置类
 * @author yinxp@dist.com.cn
 * @date 2018/12/17
 */
@Configuration
@ConfigurationProperties(prefix = "ds")
@Data
public class DBConfig {

    Logger logger = LoggerFactory.getLogger(DBConfig.class);

    public boolean monitor;
    public String url;
    public String username;
    public String password;
    public String driverClass;
    public int maxActive;
    public int initSize;
    public int minIdle;
    public int maxWait;
    public boolean testWhileIdle;
    public boolean removeAbandoned;
    public int removeAbandonedTimeout;
    public boolean logAbandoned;
    public int minEvictableIdleTimeMillis;
    public int timeBetweenEvictionRunsMillis;
    public boolean testOnBorrow;
    public String validationQuery;
    public int maxPoolPreparedStatementPerConnectionSize;
    public boolean poolPreparedStatements;


    @Bean(initMethod = "init",destroyMethod = "close")
    public DataSource dataSource(){
        DruidDataSource ds = new DruidDataSource();
        ds.setUrl(this.url);
        ds.setUsername(this.username);
        ds.setPassword(this.password);
        ds.setDriverClassName(this.driverClass);
        ds.setInitialSize(this.initSize);
        ds.setMaxActive(this.maxActive);
        ds.setMinIdle(this.minIdle);
        ds.setMaxWait(this.maxWait);
        ds.setTestWhileIdle(this.testWhileIdle);
        ds.setRemoveAbandoned(this.removeAbandoned);
        ds.setRemoveAbandonedTimeout(this.removeAbandonedTimeout);
        ds.setLogAbandoned(this.logAbandoned);
        ds.setMinEvictableIdleTimeMillis(this.minEvictableIdleTimeMillis);
        ds.setTimeBetweenEvictionRunsMillis(this.timeBetweenEvictionRunsMillis);
        ds.setTestOnBorrow(this.testOnBorrow);
        ds.setValidationQuery(this.validationQuery);
        ds.setPoolPreparedStatements(this.poolPreparedStatements);
        ds.setMaxPoolPreparedStatementPerConnectionSize(this.maxPoolPreparedStatementPerConnectionSize);
        if(this.monitor){
            try {
                ds.setFilters("stat");
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        }
        return ds;
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean(){
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
        servletRegistrationBean.setServlet(new StatViewServlet());
        servletRegistrationBean.setEnabled(true);
        servletRegistrationBean.addUrlMappings("/druid/*");
        return servletRegistrationBean;
    }

}
