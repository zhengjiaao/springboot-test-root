package com.zja.log.efk.controller;

import com.zja.log.efk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * EFK 日志测试接口
 * <p>
 * 日志以 JSON 格式输出到文件，Fluentd 采集后转发到 Elasticsearch，Kibana 可视化查询。
 * 适用于 K8s/Docker 容器化环境，Fluentd 以 DaemonSet 方式部署在每个节点采集容器日志。
 * </p>
 *
 * @author: zhengja
 * @since: 2024/01/23 16:00
 */
@Slf4j
@RestController
@RequestMapping("/api/efk")
public class EfkController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     * GET http://localhost:8080/api/efk/register?username=zhangsan&email=zhangsan@example.com
     */
    @GetMapping("/register")
    public Map<String, Object> register(@RequestParam String username, @RequestParam String email) {
        log.info("[EFK示例] 收到用户注册请求, username={}", username);
        Map<String, Object> result = new HashMap<>();
        try {
            String userId = userService.register(username, email);
            result.put("code", 200);
            result.put("userId", userId);
            result.put("message", "注册成功");
        } catch (Exception e) {
            result.put("code", 400);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 用户登录
     * GET http://localhost:8080/api/efk/login?username=zhangsan&password=password123
     */
    @GetMapping("/login")
    public Map<String, Object> login(@RequestParam String username, @RequestParam String password) {
        log.info("[EFK示例] 收到用户登录请求, username={}", username);
        boolean success = userService.login(username, password);
        Map<String, Object> result = new HashMap<>();
        result.put("code", success ? 200 : 401);
        result.put("message", success ? "登录成功" : "密码错误");
        return result;
    }

    /**
     * 模拟批量操作日志
     * GET http://localhost:8080/api/efk/batch?count=10
     */
    @GetMapping("/batch")
    public Map<String, Object> batchLog(@RequestParam(defaultValue = "5") int count) {
        log.info("[EFK示例] 开始批量日志测试, count={}", count);
        for (int i = 0; i < count; i++) {
            log.info("批量日志第 {}/{} 条, timestamp={}", i + 1, count, System.currentTimeMillis());
        }
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "已生成 " + count + " 条日志，Fluentd 将自动采集");
        return result;
    }
}
