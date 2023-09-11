package com.zja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @swagger3: <a href="http://localhost:8080/swagger-ui/index.html">...</a>
 * @author: zhengja
 * @since: 2023/09/07 14:15
 */
@SpringBootApplication
public class RemoteHttpclientApacheApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(RemoteHttpclientApacheApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(RemoteHttpclientApacheApplication.class);
    }
}