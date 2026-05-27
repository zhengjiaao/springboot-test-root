package com.zja.log.plg.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 商品服务：演示 Prometheus 自定义业务指标 + Loki 日志
 * <p>
 * Prometheus 采集自定义指标（访问次数、处理耗时），
 * Loki 采集应用日志，Grafana 统一展示。
 * </p>
 *
 * @author: zhengja
 * @since: 2024/01/23 16:00
 */
@Slf4j
@Service
public class ProductService {

    private final Counter productViewCounter;
    private final Counter productSearchCounter;
    private final Timer productQueryTimer;

    public ProductService(MeterRegistry meterRegistry) {
        // 自定义计数器：商品浏览次数
        this.productViewCounter = Counter.builder("product_view_total")
                .description("商品浏览总次数")
                .tag("service", "product")
                .register(meterRegistry);

        // 自定义计数器：商品搜索次数
        this.productSearchCounter = Counter.builder("product_search_total")
                .description("商品搜索总次数")
                .tag("service", "product")
                .register(meterRegistry);

        // 自定义计时器：商品查询耗时
        this.productQueryTimer = Timer.builder("product_query_duration")
                .description("商品查询耗时")
                .tag("service", "product")
                .register(meterRegistry);
    }

    /**
     * 查看商品详情
     */
    public String viewProduct(String productId) {
        return productQueryTimer.record(() -> {
            productViewCounter.increment();
            log.info("查看商品详情, productId={}", productId);

            // 模拟查询耗时
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(10, 100));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            log.info("商品详情查询完成, productId={}", productId);
            return "Product-" + productId;
        });
    }

    /**
     * 搜索商品
     */
    public int searchProducts(String keyword) {
        productSearchCounter.increment();
        log.info("搜索商品, keyword={}", keyword);

        // 模拟搜索耗时
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(20, 200));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        int resultCount = ThreadLocalRandom.current().nextInt(0, 100);
        log.info("搜索完成, keyword={}, resultCount={}", keyword, resultCount);

        if (resultCount == 0) {
            log.warn("搜索结果为空, keyword={}", keyword);
        }
        return resultCount;
    }
}
