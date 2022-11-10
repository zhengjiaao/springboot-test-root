package com.zja.config;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 对象之间属性值拷贝
 */
@Configuration
public class DozerConfig {
    @Bean(name = "org.dozer.Mapper")
    public Mapper mapper(){
        //如果要读取多个或者一个xml文件，则将写进下面地址,如果不需要xml配置则不用。
        // mapper.setMappingFiles(Arrays.asList("dozer/dozer-mapping.xml","dozer/xxx.xml"));
        return new DozerBeanMapper();
    }
}
