package com.zja.log.elk.controller;

import com.zja.log.elk.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * ELK 日志测试接口
 * <p>
 * 访问接口后，日志通过 logback-spring.xml 配置的 LogstashTcpSocketAppender
 * 以 JSON 格式发送到 Logstash（默认端口 5044），再由 Logstash 写入 Elasticsearch，
 * 最后通过 Kibana 进行可视化分析。
 * </p>
 *
 * @author: zhengja
 * @since: 2024/01/23 16:00
 */
@Slf4j
@RestController
@RequestMapping("/api/elk")
public class ElkController {

    @Autowired
    private OrderService orderService;

    /**
     * 创建订单（正常流程）
     * GET http://localhost:8080/api/elk/order?userId=U001&product=iPhone15
     */
    @GetMapping("/order")
    public Map<String, Object> createOrder(@RequestParam String userId, @RequestParam String product) {
        log.info("[ELK示例] 收到创建订单请求, userId={}, product={}", userId, product);
        String orderId = orderService.createOrder(userId, product);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("orderId", orderId);
        result.put("message", "订单创建成功");
        return result;
    }

    /**
     * 模拟不同级别日志输出
     * GET http://localhost:8080/api/elk/levels
     */
    @GetMapping("/levels")
    public Map<String, Object> testLogLevels() {
        log.trace("TRACE级别日志 - 最详细的跟踪信息");
        log.debug("DEBUG级别日志 - 调试信息");
        log.info("INFO级别日志 - 常规业务信息");
        log.warn("WARN级别日志 - 潜在问题警告");
        log.error("ERROR级别日志 - 错误信息");

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "已输出各级别日志，请在 Kibana 中查看");
        return result;
    }

    /**
     * 模拟异常日志
     * GET http://localhost:8080/api/elk/error
     */
    @GetMapping("/error")
    public Map<String, Object> testErrorLog() {
        Map<String, Object> result = new HashMap<>();
        try {
            log.info("模拟异常场景...");
            int i = 1 / 0;
        } catch (Exception e) {
            log.error("模拟业务异常, 堆栈信息将被 Logstash 收集", e);
            result.put("code", 500);
            result.put("message", "异常日志已发送到 ELK");
        }
        return result;
    }
}
