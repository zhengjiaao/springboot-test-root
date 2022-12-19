/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-12-19 10:25
 * @Since:
 */
package com.zja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * http://localhost:8080/swagger-ui/index.html#/
 */
@SpringBootApplication
@EnableKafka
public class MqKafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MqKafkaApplication.class, args);
    }

}
