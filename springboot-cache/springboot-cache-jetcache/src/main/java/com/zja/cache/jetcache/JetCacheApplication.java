package com.zja.cache.jetcache;

// import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
// import com.alicp.jetcache.anno.config.EnableMethodCache;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @swagger: <a href="http://localhost:8080/swagger-ui/index.html">...</a>
 * @author: zhengja
 * @since: 2024/05/31 9:53
 */
@SpringBootApplication
@EnableMethodCache(basePackages = "com.zja.cache.jetcache.service")
// @EnableCreateCacheAnnotation // jetcache 2.7 已弃用
public class JetCacheApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(JetCacheApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(JetCacheApplication.class);
    }
}