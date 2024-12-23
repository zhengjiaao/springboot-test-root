package com.zja.file.minio.upload.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: zhengja
 * @Date: 2024-12-23 11:26
 */
@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {

    /**
     * 连接地址
     */
    private String endpoint;

    /**
     * 账号
     */
    private String accessKey;

    /**
     * 密码
     */
    private String secretKey;

    /**
     * 桶名称
     */
    private String bucketName;
}