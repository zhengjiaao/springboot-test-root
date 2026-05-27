package com.zja.log.loki.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

/**
 * 支付服务：模拟支付流程日志
 * <p>
 * 日志推送到 Loki 后，可在 Grafana 中使用 LogQL 查询，例如：
 * {app="springboot-log-system-loki"} |= "支付"
 * {app="springboot-log-system-loki"} | json | level="ERROR"
 * </p>
 *
 * @author: zhengja
 * @since: 2024/01/23 16:00
 */
@Slf4j
@Service
public class PaymentService {

    /**
     * 发起支付
     */
    public String pay(String orderId, double amount, String payMethod) {
        MDC.put("orderId", orderId);
        MDC.put("payMethod", payMethod);
        try {
            log.info("发起支付请求, orderId={}, amount={}, payMethod={}", orderId, amount, payMethod);

            // 模拟参数校验
            if (amount <= 0) {
                log.error("支付金额无效, amount={}", amount);
                throw new IllegalArgumentException("支付金额必须大于0");
            }

            // 模拟第三方支付网关调用
            log.info("调用第三方支付网关, payMethod={}", payMethod);
            Thread.sleep(100);

            // 模拟支付结果
            if (amount > 10000) {
                log.warn("大额支付, 需要风控审核, amount={}", amount);
            }

            String paymentId = "PAY-" + System.currentTimeMillis();
            log.info("支付成功, paymentId={}, amount={}", paymentId, amount);
            return paymentId;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("支付被中断, orderId={}", orderId, e);
            throw new RuntimeException("支付被中断", e);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("支付异常, orderId={}", orderId, e);
            throw new RuntimeException("支付失败", e);
        } finally {
            MDC.clear();
        }
    }

    /**
     * 退款
     */
    public String refund(String paymentId, double amount) {
        MDC.put("paymentId", paymentId);
        try {
            log.info("发起退款请求, paymentId={}, amount={}", paymentId, amount);

            // 模拟退款处理
            Thread.sleep(50);
            String refundId = "REF-" + System.currentTimeMillis();
            log.info("退款成功, refundId={}", refundId);
            return refundId;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("退款被中断", e);
            throw new RuntimeException("退款被中断", e);
        } finally {
            MDC.clear();
        }
    }
}
