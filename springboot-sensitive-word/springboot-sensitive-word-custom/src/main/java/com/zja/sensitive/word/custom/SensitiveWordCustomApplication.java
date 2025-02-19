package com.zja.sensitive.word.custom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 启动类
 *
 * @swagger: <a href="http://localhost:8080/swagger-ui/index.html">...</a>
 * @author: zhengja
 * @since: 2024/10/15 13:31
 */
@SpringBootApplication
public class SensitiveWordCustomApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SensitiveWordCustomApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SensitiveWordCustomApplication.class);
    }
}