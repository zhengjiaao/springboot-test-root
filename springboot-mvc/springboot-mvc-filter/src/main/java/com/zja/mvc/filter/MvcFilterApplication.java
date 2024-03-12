package com.zja.mvc.filter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @swagger3: <a href="http://localhost:8080/swagger-ui/index.html">...</a>
 * @author: zhengja
 * @since: 2024/03/11 15:09
 */
@ServletComponentScan // 自动注册过滤器，需配合 @WebFilter 使用
@SpringBootApplication
public class MvcFilterApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MvcFilterApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MvcFilterApplication.class);
    }
}