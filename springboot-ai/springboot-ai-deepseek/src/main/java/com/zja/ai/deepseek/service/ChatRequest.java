package com.zja.ai.deepseek.service;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhengja
 * @Date: 2025-02-17 16:15
 */
@Data
public class ChatRequest implements Serializable {
    private String model;
    private List<Map<String, String>> messages;
    private boolean stream;

    public ChatRequest(String model, List<Map<String, String>> messages, boolean stream) {
        this.model = model;
        this.messages = messages;
        this.stream = stream;
    }
}
