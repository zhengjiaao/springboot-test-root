package com.dist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@EnableAutoConfiguration
public class WebsocketApplication {
	public static void main(String[] args) {
		SpringApplication.run(WebsocketApplication.class, args);
	}
}
