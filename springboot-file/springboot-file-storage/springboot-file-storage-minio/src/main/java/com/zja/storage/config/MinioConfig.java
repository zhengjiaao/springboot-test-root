package com.zja.storage.config;

import com.zja.storage.minio.MinioMultipartClient;
import com.zja.storage.minio.MinioObjectClient;
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
     * 配置 Minio客户端
     */
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioProperties.getEndpoint())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
    }

    /**
     * 配置 Minio异步客户端
     */
    @Bean
    public MinioAsyncClient minioAsyncClient() {
        return MinioAsyncClient.builder()
                .endpoint(minioProperties.getEndpoint())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
    }

    /**
     * 配置Minio对象自定义客户端：文件上传、下载、删除等
     */
    @Bean
    public MinioObjectClient minioObjectClient() {
        return new MinioObjectClient(minioClient());
    }

    /**
     * 配置Minio多部件自定义客户端：分片上传、合并分片等，支持同步和异步
     */
    @Bean
    public MinioMultipartClient minioMultipartClient() {
        return new MinioMultipartClient(minioAsyncClient());
    }
}
