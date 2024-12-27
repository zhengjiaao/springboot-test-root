package com.zja.storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 *
 * @swagger: <a href="http://localhost:8080/swagger-ui/index.html">...</a>
 * @分片上传页面：http://localhost:8080/parts/ChunkUploadProgressVue4.html
 * @author: zhengja
 * @since: 2024/12/18 10:00
 */
@SpringBootApplication
public class MinioApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinioApplication.class, args);
    }

}
