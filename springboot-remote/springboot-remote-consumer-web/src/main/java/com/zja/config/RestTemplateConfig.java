/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-11 16:10
 * @Since:
 */
package com.zja.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * 两种构造方式
 * RestTemplateBuilder  推荐，可自定义一些连接参数，如：连接超时时间，读取超时时间，还有认证信息等
 * RestTemplate         简单，一般够用了
 */
@Configuration
public class RestTemplateConfig {

    @Value("${base-url}")
    private String baseUrl;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                //自定义拦截器
                //.additionalInterceptors(new CustomClientHttpRequestInterceptor())
                //设置认证信息
                //.basicAuthentication("username", "password")
                //设置连接超时时间
                .setConnectTimeout(Duration.ofSeconds(5000))
                //设置读取超时时间
                .setReadTimeout(Duration.ofSeconds(5000))
                //设置根路径
                //.rootUri("https://api.test.com/")
                .rootUri(baseUrl)
                //构建
                .build();
    }

   /* @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }*/
}
