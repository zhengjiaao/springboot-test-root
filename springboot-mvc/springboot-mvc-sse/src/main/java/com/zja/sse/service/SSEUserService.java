package com.zja.sse.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 实现 A用户 给 B用户 发送消息的功能
 *
 * @Author: zhengja
 * @Date: 2025-02-27 17:34
 */
@Slf4j
@Service
public class SSEUserService {

    // 存储所有连接的客户端，按用户ID分组
    private final Map<String, List<SseEmitter>> userEmitters = new ConcurrentHashMap<>();

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * SSE 连接接口
     */
    public SseEmitter handleSse(String userId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        // 获取或创建用户对应的SseEmitter列表
        userEmitters.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>()).add(emitter);

        // 发送历史消息（从 Redis 中读取）
        List<String> historyMessages = redisTemplate.opsForList().range("messages:" + userId, 0, -1);
        if (historyMessages != null) {
            historyMessages.forEach(msg -> {
                try {
                    // 发送消息
                    emitter.send(SseEmitter.event().data(msg));
                    // 记录日志
                    log.info("Sent message to user " + userId + ": " + msg);
                } catch (IOException e) {
                    log.error("Error sending message to user " + userId, e);
                    removeEmitter(userId, emitter);
                }
            });
        }

        emitter.onCompletion(() -> {
            removeEmitter(userId, emitter);
            log.info("Emitter completed for user " + userId);
        });

        emitter.onTimeout(() -> {
            removeEmitter(userId, emitter);
            log.info("Emitter timed out for user " + userId);
        });

        return emitter;
    }

    /**
     * 发送消息接口
     */
    public String sendMessage(String targetUserId, String message) {
        // 将消息存入 Redis
        redisTemplate.opsForList().rightPush("messages:" + targetUserId, message);

        // 获取目标用户的 SSE 连接列表
        List<SseEmitter> emitters = userEmitters.get(targetUserId);
        if (emitters != null && !emitters.isEmpty()) {
            for (SseEmitter emitter : new CopyOnWriteArrayList<>(emitters)) { // 避免并发修改异常
                try {
                    // 发送消息
                    emitter.send(SseEmitter.event()
                            .id(UUID.randomUUID().toString())
                            .data(message));
                    log.info("Sent message to user " + targetUserId + ": " + message);
                } catch (IOException e) {
                    // 捕获异常并移除无效连接
                    log.error("Error sending message to user " + targetUserId, e);
                    removeEmitter(targetUserId, emitter);
                }
            }
        }

        return "Message sent to user " + targetUserId + ": " + message;
    }

    /**
     * 添加 SseEmitter 到用户连接列表
     */
    public void addEmitter(String userId, SseEmitter emitter) {
        List<SseEmitter> emitters = userEmitters.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>());
        emitters.add(emitter);

        // 设置超时时间并处理超时事件
        emitter.onCompletion(() -> removeEmitter(userId, emitter));
        emitter.onTimeout(() -> removeEmitter(userId, emitter));

        // 设置超时处理
        emitter.completeWithError(new Exception("Emitter timed out"));
    }

    /**
     * 移除无效的 SseEmitter
     */
    private void removeEmitter(String userId, SseEmitter emitter) {
        List<SseEmitter> emitters = userEmitters.get(userId);
        if (emitters != null) {
            emitters.remove(emitter);
            log.info("Removed invalid emitter for user " + userId);
        }
    }

    /**
     * 定期清理无效连接
     */
    public void cleanUpEmitters() {
        userEmitters.forEach((userId, emitters) -> {
            emitters.removeIf(emitter -> {
                try {
                    // 尝试发送空数据以检测连接状态
                    emitter.send(SseEmitter.event().data(""));
                    log.info("Emitter for user " + userId + " is connected");
                    return false; // 如果成功发送，则连接有效
                } catch (IOException e) {
                    log.warn("Emitter for user " + userId + " is disconnected", e);
                    return true; // 如果发送失败，则连接无效
                }
            });
            if (emitters.isEmpty()) {
                userEmitters.remove(userId);
                log.info("Cleaned up all emitters for user " + userId);
            }
        });
    }
}
