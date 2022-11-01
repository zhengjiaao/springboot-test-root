package com.zja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling //AsyncTaskConfig->SchedulingConfigurer
public class TaskTimedtaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskTimedtaskApplication.class, args);
	}

}
