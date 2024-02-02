package com.zja.hadoop.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @swagger3: <a href="http://localhost:8080/swagger-ui/index.html">...</a>
 * @author: zhengja
 * @since: 2024/02/02 14:54
 */
@SpringBootApplication
public class HadoopCommonApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(HadoopCommonApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(HadoopCommonApplication.class);
    }
}