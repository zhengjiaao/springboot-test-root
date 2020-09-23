package com.zja;

import com.slyak.spring.jpa.GenericJpaRepositoryFactoryBean;
import com.slyak.spring.jpa.GenericJpaRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableAutoConfiguration
@EnableJpaRepositories(
		basePackages = {"com.dist.dao"},
		repositoryFactoryBeanClass = GenericJpaRepositoryFactoryBean.class,
		repositoryBaseClass = GenericJpaRepositoryImpl.class)
public class SpringbootTestJpaextentionApplication {

	//不要用tomcat启动，目前会报错，找不到sqlMapper类找不到
	//启动方式： main
	public static void main(String[] args) {
		SpringApplication.run(SpringbootTestJpaextentionApplication.class, args);
	}

}
