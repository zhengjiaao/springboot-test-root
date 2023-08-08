/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-05-17 16:11
 * @Since:
 */
package com.zja.properties3;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: zhengja
 * @since: 2023/05/17 16:11
 */
@Data
@ConfigurationProperties(prefix = MinioProperties.PREFIX)
public class MinioProperties {
    public static final String PREFIX = "dist.ai.minio";

    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
}
