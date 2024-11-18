package com.zja.hanbian.封装;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 启动类
 *
 * @swagger: <a href="http://localhost:8080/swagger-ui/index.html">...</a>
 * @author: zhengja
 * @since: 2024/09/27 9:17
 */
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.zja.hanbian")
@EntityScan(basePackages = "com.zja.hanbian")
@SpringBootApplication(scanBasePackages = "com.zja.hanbian")
public class 启动应用程序 extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(启动应用程序.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(启动应用程序.class);
    }
}