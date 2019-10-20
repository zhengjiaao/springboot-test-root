package com.dist.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/** 线程池的属性配置
 * @author zhengja@dist.com.cn
 * @data 2019/8/13 16:16
 */
@Data
@Component
@ConfigurationProperties(prefix = "task.pool") // 该注解的locations已经被启用，现在只要是在环境中，都会优先加载
public class TaskThreadPoolConfig {

    //线程池维护线程的最少数量
    private int corePoolSize;

    //线程池维护线程的最大数量-只有在缓冲队列满了之后才会申请超过核心线程数的线程
    private int maxPoolSize;

    //允许的空闲时间,当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
    private int keepAliveSeconds;

    //缓存队列
    private int queueCapacity;
}
