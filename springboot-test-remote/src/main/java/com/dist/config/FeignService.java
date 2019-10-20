package com.dist.config;

import com.dist.api.FeignTestService;
import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/6/27 11:34
 */
@Configuration
public class FeignService {

    /**
     * 配置bean，Feign-HTTP调用远程服务器接口，Feign仅与本地接口配置有关，与远程接口无关
     * @return
     */
    @Bean
    FeignTestService feignTest(){
        FeignTestService service = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .options(new Request.Options(1000, 3500))
                .retryer(new Retryer.Default(5000, 5000, 3))
                .target(FeignTestService.class, "http://localhost:8080/springboot-test-remoteservice/");
        return service;
    }
}
