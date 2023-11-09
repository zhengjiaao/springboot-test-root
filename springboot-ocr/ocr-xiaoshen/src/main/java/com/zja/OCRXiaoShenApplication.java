package com.zja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @swagger3: <a href="http://localhost:19900/swagger-ui/index.html">...</a>
 * @author: zhengja
 * @since: 2023/11/08 10:25
 */
@SpringBootApplication
public class OCRXiaoShenApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(OCRXiaoShenApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(OCRXiaoShenApplication.class);
    }
}