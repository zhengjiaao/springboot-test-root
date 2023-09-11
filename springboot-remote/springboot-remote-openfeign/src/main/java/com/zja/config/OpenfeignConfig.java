/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-03-22 17:01
 * @Since:
 */
package com.zja.config;

import com.zja.fegin.MyJacksonDecoder;
import com.zja.remote.*;
import feign.Client;
import feign.Feign;
import feign.Logger;
import feign.Request;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.FormEncoder;
import feign.form.spring.SpringFormEncoder;
import feign.jackson.JacksonEncoder;
import feign.jaxb.JAXBContextFactory;
import feign.jaxb.JAXBDecoder;
import feign.jaxb.JAXBEncoder;
import feign.spring.SpringContract;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.util.concurrent.TimeUnit;

@Configuration
public class OpenfeignConfig {

    @Value("${base-url}")
    private String baseUrl;

    //推荐
    @Bean
    SpringRestFeignClient springRestFeignClient() {
//        final Decoder decoder = new GsonDecoder();
//        final Encoder encoder = new GsonEncoder();
        // Decoder使用json解码器后，若返回String类型，使用String或Object类型接受数据都会报错无法解析
        //final Decoder decoder = new JacksonDecoder();
        final Decoder decoder = new MyJacksonDecoder();
        final Encoder encoder = new JacksonEncoder();
        return Feign.builder()
                .contract(new SpringContract()) // @RequestLine("GET /get") 转为 @GetMapping("/get")
                .encoder(encoder)
                .decoder(decoder)
                .logger(new Logger.ErrorLogger())
                .logLevel(Logger.Level.BASIC)
                .options(new Request.Options(10, TimeUnit.SECONDS, 60, TimeUnit.SECONDS, true))
                .target(SpringRestFeignClient.class, baseUrl);
    }

    //推荐
    @Bean
    SpringFileFeignClient springFileFeignClient() {
//        final Decoder decoder = new JacksonDecoder();
        final Decoder decoder = new MyJacksonDecoder();
//        final Encoder encoder = new JacksonEncoder();
        //支持上传文件  @PostMapping @RequestPart MultipartFile
        final Encoder encoder = new SpringFormEncoder();
        return Feign.builder()
                .contract(new SpringContract()) // @RequestLine("GET /get") 转为 @GetMapping("/get")
                .encoder(encoder)
                .decoder(decoder)
                .logger(new Logger.ErrorLogger())
                .logLevel(Logger.Level.BASIC)
                .options(new Request.Options(10, TimeUnit.SECONDS, 60, TimeUnit.SECONDS, true))
                .target(SpringFileFeignClient.class, baseUrl);
    }

    @Bean
    RestFeignClient restFeignClient() {
//        final Decoder decoder = new GsonDecoder();
//        final Encoder encoder = new GsonEncoder();
        // Decoder使用json解码器后，若返回String类型，使用String或Object类型接受数据都会报错无法解析
        //final Decoder decoder = new JacksonDecoder();
        final Decoder decoder = new MyJacksonDecoder();
        final Encoder encoder = new JacksonEncoder();
        return Feign.builder()
//                .contract(new SpringContract()) // @RequestLine("GET /get") 转为 @GetMapping("/get")
                .encoder(encoder)
                .decoder(decoder)
                .logger(new Logger.ErrorLogger())
                .logLevel(Logger.Level.BASIC)
                .options(new Request.Options(10, TimeUnit.SECONDS, 60, TimeUnit.SECONDS, true))
                .target(RestFeignClient.class, baseUrl);
    }

    @Bean
    FileFeignClient fileFeignClient() {
//        final Decoder decoder = new JacksonDecoder();
        final Decoder decoder = new MyJacksonDecoder();
//        final Encoder encoder = new JacksonEncoder();
        //支持上传文件
        final Encoder encoder = new FormEncoder(new JacksonEncoder());
        return Feign.builder()
                .encoder(encoder)
                .decoder(decoder)
                .logger(new Logger.ErrorLogger())
                .logLevel(Logger.Level.BASIC)
                .options(new Request.Options(10, TimeUnit.SECONDS, 60, TimeUnit.SECONDS, true))
                .target(FileFeignClient.class, baseUrl);
    }

    @Bean
    XmlFegin xmlFegin() {
//        final Decoder decoder = new JacksonDecoder();
//        final Encoder encoder = new JacksonEncoder();
        //xml 编码
        JAXBContextFactory jaxbContextFactory = new JAXBContextFactory.Builder().build();
        final Decoder decoder = new JAXBDecoder(jaxbContextFactory);
        final Encoder encoder = new JAXBEncoder(jaxbContextFactory);
        return Feign.builder()
                .encoder(encoder)
                .decoder(decoder)
                .logger(new Logger.ErrorLogger())
                .logLevel(Logger.Level.BASIC)
                .options(new Request.Options(10, TimeUnit.SECONDS, 60, TimeUnit.SECONDS, true))
                .target(XmlFegin.class, baseUrl);
    }

    @Bean
    public Client skipSSLClient() {
        return new Client.Default(getSSLSocketFactory(), new NoopHostnameVerifier());
    }

    private SSLSocketFactory getSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
            return sslContext.getSocketFactory();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


}
