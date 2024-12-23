package com.zja.file.minio.upload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 启动类
 *
 * @swagger: <a href="http://localhost:8080/swagger-ui/index.html">...</a>
 * @author: zhengja
 * @since: 2024/12/18 10:00
 */
@SpringBootApplication
public class MinioUploadApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MinioUploadApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MinioUploadApplication.class);
    }
}