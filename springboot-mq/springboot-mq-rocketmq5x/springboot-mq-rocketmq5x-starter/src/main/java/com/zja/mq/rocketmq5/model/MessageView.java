package com.zja.mq.rocketmq5.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: zhengja
 * @Date: 2025-01-13 16:58
 */
@Data
public class MessageView implements Serializable {
    private String messageId;
    private String messageBody;
}
