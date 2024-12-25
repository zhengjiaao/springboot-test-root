package com.zja.storage.service;

import io.minio.MinioAsyncClient;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Minio 业务处理服务实现层
 *
 * @author: zhengja
 * @since: 2024/12/24 11:10
 */
@Slf4j
@Service
public class MinioClientServiceImpl implements MinioClientService {

    @Autowired
    MinioClient minioClient;

    @Autowired
    MinioAsyncClient minioAsyncClient;

    @Value("${minio.bucketName}")
    private String bucketName;

    // minioClient 上传、下载、查询、删除等操作

}