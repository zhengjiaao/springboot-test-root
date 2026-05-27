package com.zja.log.loki.controller;

import com.zja.log.loki.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Loki 日志测试接口
 * <p>
 * 日志通过 Loki4j Appender 推送到 Loki（HTTP push），
 * 在 Grafana Explore 中使用 LogQL 查询日志。
 * </p>
 *
 * @author: zhengja
 * @since: 2024/01/23 16:00
 */
@Slf4j
@RestController
@RequestMapping("/api/loki")
public class LokiController {

    @Autowired
    private PaymentService paymentService;

    /**
     * 发起支付
     * GET http://localhost:8080/api/loki/pay?orderId=ORD001&amount=99.9&payMethod=alipay
     */
    @GetMapping("/pay")
    public Map<String, Object> pay(@RequestParam String orderId,
                                   @RequestParam double amount,
                                   @RequestParam(defaultValue = "alipay") String payMethod) {
        log.info("[Loki示例] 收到支付请求");
        Map<String, Object> result = new HashMap<>();
        try {
            String paymentId = paymentService.pay(orderId, amount, payMethod);
            result.put("code", 200);
            result.put("paymentId", paymentId);
            result.put("message", "支付成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 退款
     * GET http://localhost:8080/api/loki/refund?paymentId=PAY001&amount=50.0
     */
    @GetMapping("/refund")
    public Map<String, Object> refund(@RequestParam String paymentId, @RequestParam double amount) {
        log.info("[Loki示例] 收到退款请求");
        Map<String, Object> result = new HashMap<>();
        try {
            String refundId = paymentService.refund(paymentId, amount);
            result.put("code", 200);
            result.put("refundId", refundId);
            result.put("message", "退款成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * LogQL 查询示例说明
     * GET http://localhost:8080/api/loki/logql-examples
     */
    @GetMapping("/logql-examples")
    public Map<String, Object> logqlExamples() {
        Map<String, Object> result = new HashMap<>();
        result.put("查询所有日志", "{app=\"springboot-log-system-loki\"}");
        result.put("查询ERROR日志", "{app=\"springboot-log-system-loki\"} |= \"ERROR\"");
        result.put("查询支付日志", "{app=\"springboot-log-system-loki\"} |= \"支付\"");
        result.put("按级别过滤", "{app=\"springboot-log-system-loki\"} | logfmt | level=\"ERROR\"");
        result.put("统计错误率", "rate({app=\"springboot-log-system-loki\"} |= \"ERROR\" [5m])");
        return result;
    }
}
