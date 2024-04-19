package com.zja.file.resources;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @swagger3: <a href="http://localhost:8080/swagger-ui/index.html">...</a>
 * @author: zhengja
 * @since: 2024/04/18 16:40
 */
@SpringBootApplication
public class ResourcesApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ResourcesApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ResourcesApplication.class);
    }
}