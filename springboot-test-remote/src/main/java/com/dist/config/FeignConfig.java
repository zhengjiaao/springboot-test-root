package com.dist.config;

import com.dist.api.FeignTestService;
import com.dist.api.TelihuiService;
import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/6/27 11:34
 */
@Configuration
public class FeignConfig {

    @Value("${feign.telihui.url}")
    public String urlTelihui;

    /**
     * 配置bean，Feign-HTTP调用远程服务器接口，Feign仅与本地接口配置有关，与远程接口无关
     */
    @Bean
    FeignTestService feignTest(){
        FeignTestService service = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .options(new Request.Options(5000, 5000))
                .retryer(new Retryer.Default(5000, 5000, 1))
                .target(FeignTestService.class, "http://localhost:8080/springboot-test-remoteservice/");
        return service;
    }

    /**
     * 第三方-特力惠
     */
    @Bean
    TelihuiService telihuiService(){
        TelihuiService service = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .options(new Request.Options(10000, 100000))
                .retryer(new Retryer.Default(10000, 10000, 1))
                .target(TelihuiService.class, urlTelihui);
        return service;
    }
}
