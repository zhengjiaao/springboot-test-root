package com.zja.properties2;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 将Unions注入到spring容器,底层使用了@Import注解的方式
 */
@Configuration
@EnableConfigurationProperties(Unions.class)
public class UnionsConfig {
}
