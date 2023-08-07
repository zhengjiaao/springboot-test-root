package com.zja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @swagger3: <a href="http://localhost:18080/swagger-ui/index.html">...</a>
 * @author: zhengja
 * @since: 2023/08/03 11:11
 */
@SpringBootApplication
public class PreviewKkviewApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(PreviewKkviewApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PreviewKkviewApplication.class);
    }
}