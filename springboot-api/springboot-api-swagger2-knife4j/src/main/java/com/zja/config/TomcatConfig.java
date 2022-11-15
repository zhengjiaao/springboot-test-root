/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2021-10-25 10:41
 * @Since:
 */
package com.zja.config;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Springboot 2.x配置同时支持 Https、Http 访问
 */
@Configuration
public class TomcatConfig {

    /**
     * server.http.port
     */
    @Value("${server.http.port}")
    private int httpPort;

    /**
     * 创建一个新增的 Tomcat Web容器
     */
    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        //添加 http
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setPort(httpPort);
        tomcat.addAdditionalTomcatConnectors(connector);
        return tomcat;
    }

}
