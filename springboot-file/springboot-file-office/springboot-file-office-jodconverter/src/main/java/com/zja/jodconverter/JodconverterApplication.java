package com.zja.jodconverter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 启动类
 *
 * @swagger: <a href="http://localhost:8080/swagger-ui/index.html">...</a>
 * @author: zhengja
 * @since: 2025/04/18 15:59
 */
@SpringBootApplication
public class JodconverterApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(JodconverterApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(JodconverterApplication.class);
    }
}