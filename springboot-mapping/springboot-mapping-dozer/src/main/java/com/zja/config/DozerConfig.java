package com.zja.config;

import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 对象之间属性值拷贝
 */
@Configuration
public class DozerConfig {
    @Bean
    public DozerBeanMapperFactoryBean mapper() {
        return new DozerBeanMapperFactoryBean();
    }
}
