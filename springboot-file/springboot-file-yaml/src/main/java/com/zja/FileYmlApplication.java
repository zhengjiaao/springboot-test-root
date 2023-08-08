package com.zja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @swagger3: <a href="http://localhost:8080/yaml/swagger-ui/index.html">...</a>
 * @author: zhengja
 * @since: 2023/08/08 11:21
 */
@SpringBootApplication
public class FileYmlApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(FileYmlApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(FileYmlApplication.class);
    }
}