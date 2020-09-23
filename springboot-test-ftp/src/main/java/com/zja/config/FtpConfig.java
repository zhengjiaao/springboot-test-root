package com.zja.config;

import com.zja.dto.FtpCfgDTO;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
* 使用@value 注解需要配置文件 ，否则找不到
*/
@Configuration
public class FtpConfig {

    //最基本的ftp(默认也就是服务器本地的 )
    @Primary
    @Bean(name = "default")
    @ConfigurationProperties(prefix = "ftp.default")
    public FtpCfgDTO getFtpCfgDTO(){
        return new FtpCfgDTO();
    }

    @Bean("cadFtp")
    @ConfigurationProperties(prefix = "ftp.cad")
    @ConditionalOnExpression(value = "true")  //是否初始化
    public FtpCfgDTO getCadFtpCfgDTO(){
        return new FtpCfgDTO();
    }

    @Bean("topicFtp")
    @ConfigurationProperties(prefix = "ftp.topic")
    @ConditionalOnExpression(value = "${ftp.topic.enabled}")  //是否初始化
    public FtpCfgDTO getTopicFtpCfgDTO(){
        return new FtpCfgDTO();
    }
}
