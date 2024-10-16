package com.zja.apache.lang3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 启动类
 *
 * @swagger: <a href="http://localhost:8080/swagger-ui/index.html">...</a>
 * @author: zhengja
 * @since: 2024/10/16 9:33
 */
@SpringBootApplication
public class ApacheLang3Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ApacheLang3Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ApacheLang3Application.class);
    }
}