package com.zja.thread.pools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @swagger3: <a href="http://localhost:8080/swagger-ui/index.html">...</a>
 * @author: zhengja
 * @since: 2024/01/22 17:31
 */
@EnableAsync
@SpringBootApplication
public class ThreadPoolsApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ThreadPoolsApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ThreadPoolsApplication.class);
    }
}