package com.zja.mq.rocketmq5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 启动类
 *
 * @swagger: <a href="http://localhost:8080/swagger-ui/index.html">...</a>
 * @author: zhengja
 * @since: 2025/01/13 10:46
 */
@SpringBootApplication
public class Rocketmq5Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Rocketmq5Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Rocketmq5Application.class);
    }
}