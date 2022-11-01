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
import com.zja.remote.RemoteUrlFegin;
import com.zja.remote.XmlFegin;
import feign.Client;
import feign.Feign;
import feign.Logger;
import feign.Request;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.jaxb.JAXBContextFactory;
import feign.jaxb.JAXBDecoder;
import feign.jaxb.JAXBEncoder;
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

    @Value("${remote.feign.url}")
    public String urlRemote;

    @Bean
    RemoteInterfaceFeignClient remoteInterfaceFeignClient() {
//        final Decoder decoder = new GsonDecoder();
//        final Encoder encoder = new GsonEncoder();
        final Decoder decoder = new JacksonDecoder();
        final Encoder encoder = new JacksonEncoder();
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
                .target(XmlFegin.class, urlRemote);
    }

    @Bean
    RemoteUrlFegin remoteUrlFegin() {
        final Decoder decoder = new JacksonDecoder();
        final Encoder encoder = new JacksonEncoder();
        final String url = "https://ghbzdw.mnr.gov.cn/";
        return Feign.builder()
                .client(skipSSLClient())
                .encoder(encoder)
                .decoder(decoder)
                .logger(new Logger.ErrorLogger())
                .logLevel(Logger.Level.BASIC)

                .options(new Request.Options(10, TimeUnit.SECONDS, 60, TimeUnit.SECONDS, true))
                .target(RemoteUrlFegin.class, url);
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
