package com.zja.mvc.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @swagger3: <a href="http://localhost:8080/web/swagger-ui/index.html">...</a>
 * @author: zhengja
 * @since: 2024/03/11 15:11
 */
@SpringBootApplication
public class MvcWebApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MvcWebApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MvcWebApplication.class);
    }
}