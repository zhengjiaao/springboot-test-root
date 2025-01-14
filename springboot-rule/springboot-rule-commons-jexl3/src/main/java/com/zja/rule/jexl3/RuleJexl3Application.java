package com.zja.rule.jexl3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 启动类
 *
 * @swagger: <a href="http://localhost:8080/swagger-ui/index.html">...</a>
 * @author: zhengja
 * @since: 2025/01/14 14:37
 */
@SpringBootApplication
public class RuleJexl3Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(RuleJexl3Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(RuleJexl3Application.class);
    }
}