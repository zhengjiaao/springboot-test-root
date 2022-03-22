/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-03-22 17:01
 * @Since:
 */
package com.zja.config;

import com.zja.remote.RemoteInterfaceFeignClient;
import feign.Feign;
import feign.Logger;
import feign.Request;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class OpenfeignConfig {

    @Value("${remote.feign.url}")
    public String urlRemote;

    @Bean
    RemoteInterfaceFeignClient remoteInterfaceFeignClient() {
        final Decoder decoder = new GsonDecoder();
        final Encoder encoder = new GsonEncoder();
        return Feign.builder()
                .encoder(encoder)
                .decoder(decoder)
                .logger(new Logger.ErrorLogger())
                .logLevel(Logger.Level.BASIC)
//                .requestInterceptor(template -> {
//                    template.header(
//                            // not available when building PRs...
//                            // https://docs.travis-ci.com/user/environment-variables/#defining-encrypted-variables-in-travisyml
//                            "Authorization",
//                            "token 383f1c1b474d8f05a21e7964976ab0d403fee071");
//                })
                .options(new Request.Options(10, TimeUnit.SECONDS, 60, TimeUnit.SECONDS, true))
                .target(RemoteInterfaceFeignClient.class, urlRemote);
    }

}
