package com.zja.preview.libreoffice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @swagger3: <a href="http://localhost:8080/swagger-ui/index.html">...</a>
 * @author: zhengja
 * @since: 2024/03/05 10:48
 */
@SpringBootApplication
public class LibreOfficeApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(LibreOfficeApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(LibreOfficeApplication.class);
    }
}