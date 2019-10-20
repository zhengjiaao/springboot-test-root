package com.dist.config;

import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yangmin
 * @date 2018/4/11
 * 返回一个dozer实例bean，目的是做对象间属性值的赋值操作
 */
@Configuration
public class DozerConfig {
    @Bean
    public DozerBeanMapperFactoryBean mapper() {
        return new DozerBeanMapperFactoryBean();
    }
}
