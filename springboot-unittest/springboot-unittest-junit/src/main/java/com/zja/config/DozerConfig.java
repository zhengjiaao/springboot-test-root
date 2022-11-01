package com.zja.config;

import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-08-25 10:53
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：dozer实例bean 配置 目的是做对象间属性值的赋值操作
 */
@Configuration
public class DozerConfig {
    @Bean
    public DozerBeanMapperFactoryBean mapper() {
        return new DozerBeanMapperFactoryBean();
    }
}
