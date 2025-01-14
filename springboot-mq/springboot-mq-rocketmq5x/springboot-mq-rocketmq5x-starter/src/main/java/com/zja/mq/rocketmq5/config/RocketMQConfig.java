package com.zja.mq.rocketmq5.config;

import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Import;

/**
 * @Author: zhengja
 * @Date: 2025-01-13 13:12
 */
@Configurable
@Import(RocketMQAutoConfiguration.class) // 让 RocketMQ 消费者监听器生效,无法收到消息。
public class RocketMQConfig {

}
