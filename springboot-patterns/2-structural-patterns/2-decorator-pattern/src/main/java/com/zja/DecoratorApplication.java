package com.zja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @swagger3: <a href="http://localhost:8080/swagger-ui/index.html">...</a>
 * @author: zhengja
 * @since: 2023/10/08 13:56
 */
@SpringBootApplication
public class DecoratorApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(DecoratorApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DecoratorApplication.class);
    }
}