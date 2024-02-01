package com.zja.webexception;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @swagger3: <a href="http://localhost:8080/swagger-ui/index.html">...</a>
 * @author: zhengja
 * @since: 2024/01/31 17:16
 */
@SpringBootApplication
public class WebExceptionApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(WebExceptionApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WebExceptionApplication.class);
    }
}