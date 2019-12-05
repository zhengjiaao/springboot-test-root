package com.dist.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * Date: 2019-12-05 14:53
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：配置redis存储session方式2 ，86400 session过期一天
 */
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 86400)
public class HttpSessionConfig {

}
