package com.zja.placeholder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 启动类
 *
 * @author: zhengja
 * @since: 2025/09/18 14:36
 */
@SpringBootApplication
public class PlaceholderApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(PlaceholderApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PlaceholderApplication.class);
    }
}