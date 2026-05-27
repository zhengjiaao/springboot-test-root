package com.zja.log.plg.controller;

import com.zja.log.plg.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * PLG 可观测性测试接口
 * <p>
 * 同时演示 Prometheus 指标采集和 Loki 日志采集：
 * - 指标端点：http://localhost:8080/actuator/prometheus
 * - 日志通过 Loki4j 推送到 Loki
 * - Grafana 统一展示指标仪表盘和日志面板
 * </p>
 *
 * @author: zhengja
 * @since: 2024/01/23 16:00
 */
@Slf4j
@RestController
@RequestMapping("/api/plg")
public class PlgController {

    @Autowired
    private ProductService productService;

    /**
     * 查看商品详情（产生指标+日志）
     * GET http://localhost:8080/api/plg/product?productId=P001
     */
    @GetMapping("/product")
    public Map<String, Object> viewProduct(@RequestParam String productId) {
        log.info("[PLG示例] 查看商品, productId={}", productId);
        String product = productService.viewProduct(productId);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("product", product);
        result.put("message", "查询成功，Prometheus已记录指标，Loki已采集日志");
        return result;
    }

    /**
     * 搜索商品（产生指标+日志）
     * GET http://localhost:8080/api/plg/search?keyword=手机
     */
    @GetMapping("/search")
    public Map<String, Object> searchProducts(@RequestParam String keyword) {
        log.info("[PLG示例] 搜索商品, keyword={}", keyword);
        int count = productService.searchProducts(keyword);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("resultCount", count);
        result.put("keyword", keyword);
        return result;
    }

    /**
     * 查看可用的可观测性端点
     * GET http://localhost:8080/api/plg/endpoints
     */
    @GetMapping("/endpoints")
    public Map<String, Object> endpoints() {
        Map<String, Object> result = new HashMap<>();
        result.put("Prometheus指标端点", "http://localhost:8080/actuator/prometheus");
        result.put("健康检查", "http://localhost:8080/actuator/health");
        result.put("所有Actuator端点", "http://localhost:8080/actuator");
        result.put("Grafana面板", "http://localhost:3000 (添加 Prometheus + Loki 数据源)");
        result.put("PromQL示例-QPS", "rate(http_server_requests_seconds_count[5m])");
        result.put("PromQL示例-商品浏览量", "product_view_total");
        result.put("LogQL示例-错误日志", "{app=\"springboot-log-system-plg\"} |= \"ERROR\"");
        return result;
    }
}
