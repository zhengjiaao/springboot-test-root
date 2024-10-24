package com.zja.pdf.itext8;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 启动类
 *
 * @swagger: <a href="http://localhost:8080/swagger-ui/index.html">...</a>
 * @author: zhengja
 * @since: 2024/10/23 10:52
 */
@SpringBootApplication
public class PdfItext8Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(PdfItext8Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PdfItext8Application.class);
    }
}