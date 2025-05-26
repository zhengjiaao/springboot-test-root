package com.zja.detect.tika;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 启动类
 *
 * @swagger: <a href="http://localhost:8080/swagger-ui/index.html">...</a>
 * @author: zhengja
 * @since: 2025/05/26 15:25
 */
@SpringBootApplication
public class DetectTikaApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(DetectTikaApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DetectTikaApplication.class);
    }
}