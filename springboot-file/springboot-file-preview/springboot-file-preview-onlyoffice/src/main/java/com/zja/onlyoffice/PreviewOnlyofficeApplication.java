package com.zja.onlyoffice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @swagger3: <a href="http://localhost:8080/swagger-ui/index.html">...</a>
 * @swagger3: <a href="http://ip:8080/swagger-ui/index.html">...</a>
 * @author: zhengja
 * @since: 2023/08/03 11:12
 */
@SpringBootApplication
public class PreviewOnlyofficeApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(PreviewOnlyofficeApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PreviewOnlyofficeApplication.class);
    }
}