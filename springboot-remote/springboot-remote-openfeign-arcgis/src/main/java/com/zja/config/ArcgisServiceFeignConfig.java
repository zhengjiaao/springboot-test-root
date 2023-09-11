/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-09-06 17:03
 * @Since:
 */
package com.zja.config;

import com.zja.remote.arcgis.rest.ArcgisServiceFeign;
import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: zhengja
 * @since: 2023/09/06 17:03
 */
@Configuration
public class ArcgisServiceFeignConfig {

    @Value("${feign.arcgis}")
    public String arcgis;

    /**
     * Arcgis 服务调用
     */
    @Bean
    ArcgisServiceFeign arcgisServiceFeign(){
        ArcgisServiceFeign service = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .options(new Request.Options(60000, 60000))
                .retryer(new Retryer.Default(60000, 60000, 1))
                .target(ArcgisServiceFeign.class, arcgis);
        return service;
    }
}
