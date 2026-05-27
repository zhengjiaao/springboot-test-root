package com.zja.controller.parts6.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文件存储配置类
 *
 * @author: zhengja
 * @since: 2026/03/09 10:00:00
 */
@Data
@Component
@ConfigurationProperties(prefix = "file.storage")
public class FileStorageConfig {

    /**
     * 本地存储路径
     */
    private String localPath = "/data/files";

    /**
     * 分片文件临时存储路径
     */
    private String chunkTempPath = "/data/files/chunks";

    /**
     * 分片过期时间（小时），默认 24 小时
     */
    private Integer chunkExpireHours = 24;
}
