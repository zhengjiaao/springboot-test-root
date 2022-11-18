package com.dist.rocketmq.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-06-22 17:26
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：rocketmq 消息监听，@RocketMQMessageListener中的selectorExpression为tag，默认为*
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = "queue_test_topic", selectorExpression = "*", consumerGroup = "queue_group_test")
public class RocketMQMsgListener implements RocketMQListener<MessageExt> {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public void onMessage(MessageExt message) {
        byte[] body = message.getBody();
        String msg = new String(body);
        log.info("接收到消息：{}", msg);
    }

}
