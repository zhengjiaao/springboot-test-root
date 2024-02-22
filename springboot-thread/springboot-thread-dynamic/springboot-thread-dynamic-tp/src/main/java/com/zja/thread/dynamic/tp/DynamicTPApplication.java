package com.zja.thread.dynamic.tp;

import org.dromara.dynamictp.core.spring.EnableDynamicTp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @swagger3: <a href="http://localhost:8080/swagger-ui/index.html">...</a>
 * @author: zhengja
 * @since: 2024/02/22 11:08
 */
@EnableDynamicTp
@SpringBootApplication
public class DynamicTPApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(DynamicTPApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DynamicTPApplication.class);
    }
}