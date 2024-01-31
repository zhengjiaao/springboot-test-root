package com.zja.obfuscated.proguard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @swagger3: <a href="http://localhost:8080/swagger-ui/index.html">...</a>
 * @author: zhengja
 * @since: 2024/01/26 15:15
 */
@EnableJpaAuditing
@SpringBootApplication
public class ProguardApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ProguardApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ProguardApplication.class);
    }

}