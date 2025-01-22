package com.zja.onlyoffice.config;

import com.zja.onlyoffice.fegin.OnlyOfficeAdditionalFeign;
import com.zja.onlyoffice.fegin.OnlyOfficeFeign;
import feign.Feign;
import feign.Logger;
import feign.Request;
import feign.Retryer;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.spring.SpringContract;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @Author: zhengja
 * @Date: 2025-01-17 13:49
 */
@Configuration
public class FeignBeanConfig {

    @Value("${feign.onlyoffice-documentserver-url:https://documentserver}")
    public String onlyofficeDocumentserverUrl;

    /**
     * Arcgis 服务调用
     */
    @Bean
    OnlyOfficeFeign onlyOfficeFeign() {
        return Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .options(new Request.Options(60_000, TimeUnit.MILLISECONDS, 60_000, TimeUnit.MILLISECONDS, true))
                .retryer(new Retryer.Default(60000, 60000, 1))
                .target(OnlyOfficeFeign.class, onlyofficeDocumentserverUrl);
    }

    @Bean
    OnlyOfficeAdditionalFeign onlyOfficeAdditionalFeign() {
        final Decoder decoder = new MyJacksonDecoder();
        final Encoder encoder = new JacksonEncoder();
        return Feign.builder()
                .contract(new SpringContract()) // @RequestLine("GET /get") 转为 @GetMapping("/get")
                .encoder(encoder)
                .decoder(decoder)
                .logger(new Logger.ErrorLogger())
                .logLevel(Logger.Level.BASIC)
                .options(new Request.Options(10, TimeUnit.SECONDS, 60, TimeUnit.SECONDS, true))
                .target(OnlyOfficeAdditionalFeign.class, onlyofficeDocumentserverUrl);
    }

}
