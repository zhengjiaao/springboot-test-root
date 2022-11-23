package com.zja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * http://localhost:8080/swagger-ui/index.html#/
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class TaskSpringTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskSpringTaskApplication.class, args);
	}

}
