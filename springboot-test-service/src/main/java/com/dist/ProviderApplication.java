package com.dist;

import com.dist.server.EmbeddedZooKeeper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

/**
 * @author yinxp@dist.com.cn
 * @date 2018/12/13
 */
@SpringBootApplication(exclude = {
                                         MongoAutoConfiguration.class,
                                         MongoDataAutoConfiguration.class
})
@ImportResource({"classpath:config/spring-dubbo-provider.xml"})
@EntityScan(basePackages = {"com.dist.model"})
@EnableCaching
@EnableAsync
public class ProviderApplication {

    private final static String ZK_PORT_PATTERN = "[0-9]+";

    public static void main(String[] args) {
        //搭建-zk配置连接
        if (args.length > 0 && args[0].matches(ZK_PORT_PATTERN)) {
            new EmbeddedZooKeeper(2181, false).start();
        }
        SpringApplication.run(ProviderApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
