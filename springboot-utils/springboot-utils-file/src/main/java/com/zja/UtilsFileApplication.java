package com.zja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @swagger3: <a href="http://localhost:8080/file/swagger-ui/index.html">...</a>
 * @author: zhengja
 * @since: 2023/08/02 10:43
 */
@SpringBootApplication
public class UtilsFileApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(UtilsFileApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(UtilsFileApplication.class);
    }
}