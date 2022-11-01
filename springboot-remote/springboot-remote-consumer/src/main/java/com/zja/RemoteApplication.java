package com.zja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * http://127.0.0.1:8082/springboot-test-remote/swagger-ui.html
 */
@SpringBootApplication(exclude = {
		MongoAutoConfiguration.class,
		MongoDataAutoConfiguration.class
})
@EnableAsync
@EnableHystrix  // 开启服务降级/服务熔断
public class RemoteApplication {

	public static void main(String[] args) {
		SpringApplication.run(RemoteApplication.class, args);
	}

}
