package com.zja.log.trace.service;

import brave.Span;
import brave.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 库存服务：演示 Sleuth 自动追踪和手动创建 Span
 * <p>
 * Sleuth 会自动追踪 Spring Bean 间的调用，
 * 同时支持通过 Tracer API 手动创建子 Span 进行更细粒度的追踪。
 * </p>
 *
 * @author: zhengja
 * @since: 2024/01/23 16:00
 */
@Slf4j
@Service
public class InventoryService {

    @Autowired
    private Tracer tracer;

    /**
     * 检查库存
     */
    public int checkStock(String productId) {
        log.info("检查库存, productId={}", productId);
        // Sleuth 自动在日志中添加 traceId/spanId

        // 手动创建子 Span（更细粒度的追踪）
        Span dbSpan = tracer.nextSpan().name("db-query-stock").start();
        try (Tracer.SpanInScope ws = tracer.withSpanInScope(dbSpan)) {
            dbSpan.tag("db.type", "mysql");
            dbSpan.tag("db.table", "t_inventory");
            dbSpan.tag("product.id", productId);

            // 模拟数据库查询
            log.info("查询数据库库存, productId={}", productId);
            Thread.sleep(30);

            int stock = (int) (Math.random() * 100);
            dbSpan.tag("stock.count", String.valueOf(stock));
            log.info("库存查询结果, productId={}, stock={}", productId, stock);
            return stock;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            dbSpan.error(e);
            throw new RuntimeException("库存查询被中断", e);
        } finally {
            dbSpan.finish();
        }
    }

    /**
     * 扣减库存
     */
    public boolean deductStock(String productId, int quantity) {
        log.info("扣减库存, productId={}, quantity={}", productId, quantity);

        Span deductSpan = tracer.nextSpan().name("db-deduct-stock").start();
        try (Tracer.SpanInScope ws = tracer.withSpanInScope(deductSpan)) {
            deductSpan.tag("product.id", productId);
            deductSpan.tag("deduct.quantity", String.valueOf(quantity));

            int currentStock = checkStock(productId);
            if (currentStock < quantity) {
                log.warn("库存不足, productId={}, currentStock={}, required={}", productId, currentStock, quantity);
                deductSpan.tag("deduct.result", "insufficient");
                return false;
            }

            // 模拟扣减
            Thread.sleep(20);
            log.info("库存扣减成功, productId={}, deducted={}", productId, quantity);
            deductSpan.tag("deduct.result", "success");
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            deductSpan.error(e);
            throw new RuntimeException("库存扣减被中断", e);
        } finally {
            deductSpan.finish();
        }
    }
}
