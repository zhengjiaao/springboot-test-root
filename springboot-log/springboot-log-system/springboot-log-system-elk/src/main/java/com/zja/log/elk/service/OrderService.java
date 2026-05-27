package com.zja.log.elk.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 订单服务：模拟真实业务场景日志输出
 * <p>
 * 通过 MDC 添加 traceId 实现日志链路追踪，
 * 在 Kibana 中可根据 traceId 过滤查询完整调用链。
 * </p>
 *
 * @author: zhengja
 * @since: 2024/01/23 16:00
 */
@Slf4j
@Service
public class OrderService {

    /**
     * 创建订单
     */
    public String createOrder(String userId, String product) {
        String traceId = UUID.randomUUID().toString().replace("-", "");
        MDC.put("traceId", traceId);
        try {
            log.info("开始创建订单, userId={}, product={}", userId, product);

            // 模拟参数校验
            validateOrder(userId, product);

            // 模拟库存检查
            checkInventory(product);

            // 模拟订单入库
            String orderId = saveOrder(userId, product);

            log.info("订单创建成功, orderId={}", orderId);
            return orderId;
        } catch (Exception e) {
            log.error("订单创建失败, userId={}, product={}", userId, product, e);
            throw e;
        } finally {
            MDC.remove("traceId");
        }
    }

    private void validateOrder(String userId, String product) {
        log.debug("校验订单参数, userId={}, product={}", userId, product);
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        if (product == null || product.isEmpty()) {
            throw new IllegalArgumentException("商品名称不能为空");
        }
    }

    private void checkInventory(String product) {
        log.info("检查库存, product={}", product);
        // 模拟库存不足告警
        int stock = (int) (Math.random() * 100);
        if (stock < 10) {
            log.warn("库存不足告警, product={}, stock={}", product, stock);
        }
    }

    private String saveOrder(String userId, String product) {
        String orderId = "ORD-" + System.currentTimeMillis();
        log.info("保存订单到数据库, orderId={}, userId={}, product={}", orderId, userId, product);
        // 模拟耗时
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return orderId;
    }
}
