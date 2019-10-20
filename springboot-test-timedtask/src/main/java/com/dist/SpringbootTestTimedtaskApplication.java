package com.dist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling //AsyncTaskConfig->SchedulingConfigurer
public class SpringbootTestTimedtaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootTestTimedtaskApplication.class, args);
	}

}
