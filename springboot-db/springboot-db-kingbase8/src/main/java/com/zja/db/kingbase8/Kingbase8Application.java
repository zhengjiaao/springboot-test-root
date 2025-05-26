package com.zja.db.kingbase8;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * 启动类
 *
 * @swagger: <a href="http://localhost:8080/swagger-ui/index.html">...</a>
 * @author: zhengja
 * @since: 2025/05/26 15:36
 */
@EnableJpaAuditing
@SpringBootApplication
public class Kingbase8Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Kingbase8Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Kingbase8Application.class);
    }
}