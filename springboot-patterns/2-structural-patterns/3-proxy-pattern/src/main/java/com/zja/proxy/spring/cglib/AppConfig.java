/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 15:20
 * @Since:
 */
package com.zja.proxy.spring.cglib;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author: zhengja
 * @since: 2023/10/08 15:20
 */
// 配置类中使用代理
@Configuration
@EnableAspectJAutoProxy
public class AppConfig {
    @Bean
    public UserService userService() {
        return new UserService();
    }

    @Bean
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }
}

