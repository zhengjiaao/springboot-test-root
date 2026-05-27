package com.zja.log.trace.controller;

import com.zja.log.trace.service.InventoryService;
import brave.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Sleuth + Zipkin 链路追踪测试接口
 * <p>
 * Sleuth 自动在日志中注入 [appName, traceId, spanId, exportable]，
 * 无需任何额外代码，日志自动包含链路信息。
 * </p>
 *
 * @author: zhengja
 * @since: 2024/01/23 16:00
 */
@Slf4j
@RestController
@RequestMapping("/api/trace")
public class TraceController {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private Tracer tracer;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 检查库存（演示自动追踪 + 手动 Span）
     * GET http://localhost:8080/api/trace/stock?productId=P001
     */
    @GetMapping("/stock")
    public Map<String, Object> checkStock(@RequestParam String productId) {
        log.info("[Trace示例] 检查库存, productId={}", productId);
        // 日志自动包含：[springboot-log-system-trace, traceId, spanId, true]

        int stock = inventoryService.checkStock(productId);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("productId", productId);
        result.put("stock", stock);
        result.put("traceId", currentTraceId());
        result.put("message", "在 Zipkin UI 中可查看完整调用链（包含手动 Span）");
        return result;
    }

    /**
     * 扣减库存
     * GET http://localhost:8080/api/trace/deduct?productId=P001&quantity=5
     */
    @GetMapping("/deduct")
    public Map<String, Object> deductStock(@RequestParam String productId,
                                           @RequestParam(defaultValue = "1") int quantity) {
        log.info("[Trace示例] 扣减库存, productId={}, quantity={}", productId, quantity);

        boolean success = inventoryService.deductStock(productId, quantity);

        Map<String, Object> result = new HashMap<>();
        result.put("code", success ? 200 : 400);
        result.put("productId", productId);
        result.put("deducted", success);
        result.put("traceId", currentTraceId());
        return result;
    }

    /**
     * 模拟跨服务调用（RestTemplate 自动传播 traceId）
     * GET http://localhost:8080/api/trace/cross-service
     */
    @GetMapping("/cross-service")
    public Map<String, Object> crossServiceCall() {
        String traceId = currentTraceId();
        log.info("[Trace示例] 发起跨服务调用, traceId={}", traceId);

        Map<String, Object> result = new HashMap<>();
        result.put("traceId", traceId);

        try {
            // RestTemplate 调用会自动在请求头中传播 traceId
            // 如果目标服务也集成了 Sleuth，两端日志将拥有相同的 traceId
            String response = restTemplate.getForObject(
                    "http://localhost:8080/api/trace/stock?productId=CROSS001", String.class);
            result.put("code", 200);
            result.put("response", response);
            result.put("message", "RestTemplate 自动传播 traceId，两端日志可关联");
        } catch (Exception e) {
            log.warn("跨服务调用失败（预期行为，演示用）", e);
            result.put("code", 200);
            result.put("message", "跨服务调用演示 - RestTemplate 会自动在 Header 中注入 traceId");
        }
        return result;
    }

    /**
     * 获取当前链路信息
     * GET http://localhost:8080/api/trace/info
     */
    @GetMapping("/info")
    public Map<String, Object> traceInfo() {
        Map<String, Object> result = new HashMap<>();
        result.put("traceId", currentTraceId());
        result.put("说明-自动追踪", "Sleuth 自动追踪 HTTP/MQ/定时任务/异步方法");
        result.put("说明-日志格式", "[appName, traceId, spanId, exportable]");
        result.put("说明-传播机制", "通过 HTTP Header (X-B3-TraceId) 跨服务传播");
        result.put("Zipkin UI", "http://localhost:9411 查看链路详情");
        return result;
    }

    private String currentTraceId() {
        if (tracer.currentSpan() != null && tracer.currentSpan().context() != null) {
            return tracer.currentSpan().context().traceIdString();
        }
        return "N/A";
    }
}
