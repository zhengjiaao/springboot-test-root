package com.zja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 密码服务平台启动类
 * <a href="http://localhost:8080/swagger-ui/index.html#/">...</a>
 * <p>面向三方系统的统一密码服务：集中密钥管理、加解密、签名验签、摘要、消息认证码，
 * 支持三种接入方式：在线 SDK、HTTP 接口、离线 SDK，帮助三方系统完成国密改造（密改）。</p>
 *
 * @author: zhengja
 */
@SpringBootApplication
public class CryptoPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(CryptoPlatformApplication.class, args);
    }

}
