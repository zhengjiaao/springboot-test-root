package com.dist;

import com.dist.server.EmbeddedZooKeeper;
import com.dist.utils.CacheFile;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @date 2018/8/15
 * @desc
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
@ImportResource({"classpath:config/spring-dubbo-consumer.xml"})
@EnableAsync
public class ConsumerApplication {
    private final static String ZK_PORT_PATTERN = "[0-9]+";

    public static void main(String[] args) {
        //搭建-zk配置连接
        if(args.length > 0 && args[0].matches(ZK_PORT_PATTERN)){
             new EmbeddedZooKeeper(2181, false).start();
        }
        SpringApplication.run(ConsumerApplication.class, args);
    }

    @Bean
    public CacheFile cacheFile(){
        return new CacheFile();
    }
}
