package com.zja.hanbian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 启动类
 *
 * @swagger: <a href="http://localhost:8080/swagger-ui/index.html">...</a>
 * @author: zhengja
 * @since: 2024/09/27 9:17
 */
@SpringBootApplication
public class 汉编应用程序 extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(汉编应用程序.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(汉编应用程序.class);
    }
}