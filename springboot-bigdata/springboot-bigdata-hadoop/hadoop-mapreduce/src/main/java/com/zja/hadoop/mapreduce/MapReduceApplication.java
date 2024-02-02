package com.zja.hadoop.mapreduce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @swagger3: <a href="http://localhost:8080/swagger-ui/index.html">...</a>
 * @author: zhengja
 * @since: 2024/02/02 14:49
 */
@SpringBootApplication
public class MapReduceApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MapReduceApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MapReduceApplication.class);
    }
}