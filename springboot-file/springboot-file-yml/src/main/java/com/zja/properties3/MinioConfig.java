/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-05-17 16:16
 * @Since:
 */
package com.zja.properties3;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author: zhengja
 * @since: 2023/05/17 16:16
 */
@Component
@Configuration
@EnableConfigurationProperties({MinioProperties.class})
public class MinioConfig {

    /**
     * 存储服务
     */
    @Bean
    StorageManage storageManage(MinioProperties properties) {
        return new MinioStorageImpl(properties);
    }

}
