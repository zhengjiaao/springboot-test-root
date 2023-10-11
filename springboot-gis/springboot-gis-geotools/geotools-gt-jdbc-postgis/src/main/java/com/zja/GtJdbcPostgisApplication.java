package com.zja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @swagger3: <a href="http://localhost:8080/swagger-ui/index.html">...</a>
 * @author: zhengja
 * @since: 2023/10/10 11:28
 */
@SpringBootApplication
public class GtJdbcPostgisApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(GtJdbcPostgisApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(GtJdbcPostgisApplication.class);
    }
}