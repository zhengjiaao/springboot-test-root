package com.zja.file.minio.upload.config;

import com.zja.file.minio.upload.storage.MinioMultipartClient;
import io.minio.MinioAsyncClient;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: zhengja
 * @Date: 2024-12-23 11:23
 */
@Configuration
@RequiredArgsConstructor
public class MinioConfig {

    private final MinioProperties minioProperties;

    /**
     * 配置Minio客户端
     */
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioProperties.getEndpoint())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
    }

    /**
     * 配置Minio多部件客户端：分片上传
     */
    @Bean
    public MinioMultipartClient minioMultipartClient() {
        MinioAsyncClient asyncClient = MinioAsyncClient.builder()
                .endpoint(minioProperties.getEndpoint())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
        return new MinioMultipartClient(asyncClient);
    }
}
