package com.zja.sse.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author: zhengja
 * @Date: 2025-02-27 16:54
 */
@Slf4j
@Service
public class SSEService {

    // 存储所有连接的客户端
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * SSE 连接接口
     */
    public SseEmitter handleSse() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        // 发送历史消息（从 Redis 中读取）
        List<String> historyMessages = redisTemplate.opsForList().range("messages", 0, -1);
        if (historyMessages != null) {
            historyMessages.forEach(msg -> {
                try {
                    emitter.send(SseEmitter.event().data(msg));
                } catch (IOException e) {
                    emitters.remove(emitter);
                    log.error("Error sending historical message to emitter", e);
                }
            });
        }

        emitters.add(emitter);

        emitter.onCompletion(() -> {
            emitters.remove(emitter);
            log.info("Emitter completed and removed");
        });

        emitter.onTimeout(() -> {
            emitters.remove(emitter);
            log.info("Emitter timed out and removed");
        });

        return emitter;
    }

    /**
     * 发送消息接口
     */
    public String sendMessage(String message) {
        redisTemplate.opsForList().rightPush("messages", message);

        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .id(UUID.randomUUID().toString())
                        .data(message));
            } catch (IOException e) {
                emitters.remove(emitter);
                log.error("Error sending message to emitter", e);
            }
        }
        return "Message sent: " + message;
    }
}
