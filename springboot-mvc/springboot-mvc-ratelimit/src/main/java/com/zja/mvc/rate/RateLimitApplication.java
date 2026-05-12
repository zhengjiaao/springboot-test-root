package com.zja.mvc.rate;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 启动类
 *
 * @swagger: <a href="http://localhost:8080/swagger-ui/index.html">...</a>
 * @author: zhengja
 * @since: 2026/03/24 17:48
 */
@SpringBootApplication
public class RateLimitApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(RateLimitApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(RateLimitApplication.class);
    }
}