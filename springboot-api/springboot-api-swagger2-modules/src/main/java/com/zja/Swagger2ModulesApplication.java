package com.zja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @swagger2: <a href="http://localhost:8080/swagger-ui.html">...</a>
 * @author: zhengja
 * @since: 2023/10/31 15:13
 */
@SpringBootApplication
public class Swagger2ModulesApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Swagger2ModulesApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Swagger2ModulesApplication.class);
    }
}