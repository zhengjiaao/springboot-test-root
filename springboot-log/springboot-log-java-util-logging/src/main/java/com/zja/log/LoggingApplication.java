package com.zja.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @swagger3: <a href="http://localhost:8080/swagger-ui/index.html">...</a>
 * @author: zhengja
 * @since: 2024/01/23 17:31
 */
@SpringBootApplication
public class LoggingApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(LoggingApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(LoggingApplication.class);
    }
}