package com.zja.log.prometheus.controller;

import com.zja.log.prometheus.service.MetricsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Prometheus 指标监控测试接口
 * <p>
 * 指标端点：http://localhost:8080/actuator/prometheus
 * 健康检查：http://localhost:8080/actuator/health
 * 所有端点：http://localhost:8080/actuator
 * </p>
 *
 * @author: zhengja
 * @since: 2024/01/23 16:00
 */
@Slf4j
@RestController
@RequestMapping("/api/prometheus")
public class PrometheusController {

    @Autowired
    private MetricsService metricsService;

    /**
     * 模拟 API 调用（增加请求计数和响应时间指标）
     * GET http://localhost:8080/api/prometheus/call?api=/user/list
     */
    @GetMapping("/call")
    public Map<String, Object> apiCall(@RequestParam(defaultValue = "/default") String api) {
        String result = metricsService.simulateApiCall(api);
        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("result", result);
        map.put("message", "指标已记录：api_requests_total +1, api_response_time 已计时");
        return map;
    }

    /**
     * 模拟错误（增加错误计数指标）
     * GET http://localhost:8080/api/prometheus/error
     */
    @GetMapping("/error")
    public Map<String, Object> simulateError() {
        metricsService.simulateError();
        Map<String, Object> map = new HashMap<>();
        map.put("code", 500);
        map.put("message", "指标已记录：api_errors_total +1");
        return map;
    }

    /**
     * 模拟用户上线（Gauge 指标增加）
     * GET http://localhost:8080/api/prometheus/online
     */
    @GetMapping("/online")
    public Map<String, Object> userOnline() {
        int count = metricsService.userOnline();
        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("onlineUsers", count);
        map.put("message", "Gauge 指标 online_users 已更新");
        return map;
    }

    /**
     * 模拟用户下线（Gauge 指标减少）
     * GET http://localhost:8080/api/prometheus/offline
     */
    @GetMapping("/offline")
    public Map<String, Object> userOffline() {
        int count = metricsService.userOffline();
        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("onlineUsers", count);
        map.put("message", "Gauge 指标 online_users 已更新");
        return map;
    }

    /**
     * PromQL 查询示例说明
     * GET http://localhost:8080/api/prometheus/promql-examples
     */
    @GetMapping("/promql-examples")
    public Map<String, Object> promqlExamples() {
        Map<String, Object> result = new HashMap<>();
        result.put("1.请求总数", "api_requests_total");
        result.put("2.QPS(每秒请求数)", "rate(api_requests_total[5m])");
        result.put("3.错误率", "rate(api_errors_total[5m]) / rate(api_requests_total[5m])");
        result.put("4.在线用户数", "online_users");
        result.put("5.P99响应时间", "api_response_time_seconds{quantile=\"0.99\"}");
        result.put("6.JVM堆内存", "jvm_memory_used_bytes{area=\"heap\"}");
        result.put("7.JVM GC次数", "rate(jvm_gc_pause_seconds_count[5m])");
        result.put("8.HTTP请求P95", "http_server_requests_seconds{quantile=\"0.95\"}");
        return result;
    }
}
