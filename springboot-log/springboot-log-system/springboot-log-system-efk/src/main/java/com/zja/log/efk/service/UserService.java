package com.zja.log.efk.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 用户服务：模拟用户注册/登录等真实业务场景
 * <p>
 * 日志输出为 JSON 格式，Fluentd 采集后可在 Kibana 中按字段过滤查询
 * </p>
 *
 * @author: zhengja
 * @since: 2024/01/23 16:00
 */
@Slf4j
@Service
public class UserService {

    /**
     * 用户注册
     */
    public String register(String username, String email) {
        String requestId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        MDC.put("requestId", requestId);
        MDC.put("action", "register");
        try {
            log.info("开始用户注册, username={}, email={}", username, email);

            // 模拟校验用户名
            if ("admin".equals(username)) {
                log.warn("用户名已被占用, username={}", username);
                throw new IllegalArgumentException("用户名已被占用: " + username);
            }

            // 模拟保存用户
            String userId = "USR-" + System.currentTimeMillis();
            log.info("用户注册成功, userId={}, username={}", userId, username);

            // 模拟发送欢迎邮件
            log.debug("发送欢迎邮件, email={}", email);

            return userId;
        } catch (Exception e) {
            log.error("用户注册失败, username={}", username, e);
            throw e;
        } finally {
            MDC.clear();
        }
    }

    /**
     * 用户登录
     */
    public boolean login(String username, String password) {
        MDC.put("action", "login");
        try {
            log.info("用户尝试登录, username={}", username);

            // 模拟认证
            if ("password123".equals(password)) {
                log.info("用户登录成功, username={}", username);
                return true;
            } else {
                log.warn("用户登录失败-密码错误, username={}", username);
                return false;
            }
        } finally {
            MDC.clear();
        }
    }
}
