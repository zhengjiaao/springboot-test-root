package com.zja.pdf.itext5;

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
public class PdfItext5Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(PdfItext5Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PdfItext5Application.class);
    }
}