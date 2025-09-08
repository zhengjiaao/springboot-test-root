package com.zja.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author: zhengja
 * @Date: 2025-09-08 20:07
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "ftp")
public class FtpServerProperties {
    // 使用 Map 存储多个服务器配置，key 为服务器标识（如 "ftp"）
    private Map<String, ServerConfig> servers;

    @Setter
    @Getter
    public static class ServerConfig {
        private String host;
        private int port = 21; // 默认端口
        private String username;
        private String password;
        private boolean passiveMode = true; // 默认使用被动模式，通常更安全，尤其在有防火墙/NAT时 [[27]]
    }
}