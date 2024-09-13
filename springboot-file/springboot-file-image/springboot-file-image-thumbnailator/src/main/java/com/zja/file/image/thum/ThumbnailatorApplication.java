package com.zja.file.image.thum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 启动类
 *
 * @swagger: <a href="http://localhost:8080/swagger-ui/index.html">...</a>
 * @author: zhengja
 * @since: 2024/09/12 17:05
 */
@SpringBootApplication
public class ThumbnailatorApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ThumbnailatorApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ThumbnailatorApplication.class);
    }
}