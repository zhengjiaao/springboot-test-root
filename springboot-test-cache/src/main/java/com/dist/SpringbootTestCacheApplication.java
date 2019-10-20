package com.dist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringbootTestCacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootTestCacheApplication.class, args);
	}

}
