/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-03-22 16:54
 * @Since:
 */
package com.zja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * swagger: http://localhost:8083/openfeign/swagger-ui.html
 */
@SpringBootApplication
public class RemoteConsumerOpenfeignApplication {

    public static void main(String[] args) {
        SpringApplication.run(RemoteConsumerOpenfeignApplication.class, args);
    }

}
