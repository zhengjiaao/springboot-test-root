package com.zja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @swagger3: <a href="http://localhost:8080/swagger-ui/index.html">...</a>
 * @druid: <a href="http://localhost:8080/druid/">...</a>
 * @author: zhengja
 * @since: 2023/08/01 14:41
 */
@SpringBootApplication
public class DbDruidApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(DbDruidApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DbDruidApplication.class);
    }
}