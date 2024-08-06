package com.zja.mdb.jackcess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 启动类
 * @swagger: <a href="http://localhost:8080/swagger-ui/index.html">...</a>
 * @author: zhengja
 * @since: 2024/08/06 10:05
 */
@SpringBootApplication
public class JackcessApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(JackcessApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(JackcessApplication.class);
    }
}