package com.zja.ai.langchain4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @swagger: <a href="http://localhost:8080/swagger-ui/index.html">...</a>
 * @author: zhengja
 * @since: 2024/05/15 15:02
 */
@SpringBootApplication
public class LangChain4jApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(LangChain4jApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(LangChain4jApplication.class);
    }
}