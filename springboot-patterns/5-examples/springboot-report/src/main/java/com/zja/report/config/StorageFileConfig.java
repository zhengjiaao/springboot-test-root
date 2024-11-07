package com.zja.report.config;

import com.zja.report.service.StorageFileService;
import com.zja.report.service.StorageFileServiceMongoImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

/**
 * 存储服务配置
 *
 * @Author: zhengja
 * @Date: 2024-09-04 11:40
 */
@Slf4j
@Configuration
public class StorageFileConfig {

    public static final String STORAGE_TYPE_MONGO = "mongo";
    public static final String STORAGE_TYPE_GUO_TU = "guo_tu";

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Value("${storage.file.type:mongo}")
    private String storageType;

    @Bean
    public StorageFileService storageFileService() {
        if (STORAGE_TYPE_MONGO.equalsIgnoreCase(storageType)) {
            log.info("存储服务类型：使用Mongo存储服务");
            return new StorageFileServiceMongoImpl(gridFsTemplate, mongoTemplate);
        }

        log.error("未配置正确的存储服务类型，storage.type=[mongo or guo_tu]");
        throw new RuntimeException("未配置正确的存储服务类型，storage.type=[mongo or guo_tu].");
    }
}

