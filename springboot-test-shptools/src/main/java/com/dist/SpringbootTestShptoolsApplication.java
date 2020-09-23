package com.zja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(value = "com.dist.entity")
public class SpringbootTestShptoolsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootTestShptoolsApplication.class, args);
	}

}
