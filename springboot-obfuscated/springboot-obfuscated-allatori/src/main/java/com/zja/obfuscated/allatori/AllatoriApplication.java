package com.zja.obfuscated.allatori;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @swagger3: <a href="http://localhost:8080/swagger-ui/index.html">...</a>
 * @author: zhengja
 * @since: 2024/01/31 9:12
 */
@SpringBootApplication
public class AllatoriApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(AllatoriApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AllatoriApplication.class);
    }
}