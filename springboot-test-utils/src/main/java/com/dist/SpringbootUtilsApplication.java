package com.dist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SpringbootUtilsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootUtilsApplication.class, args);
	}

}
