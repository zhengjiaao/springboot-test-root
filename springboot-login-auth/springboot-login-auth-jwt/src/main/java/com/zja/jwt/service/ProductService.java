package com.zja.jwt.service;

import com.zja.jwt.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 产品服务（内存模拟数据，演示用）
 * <p>
 * 权限说明：
 * - ADMIN: 可查询、新增、修改、删除产品
 * - USER:  仅可查询在售产品
 *
 * @Author: zhengja
 * @Date: 2025-07-11
 */
@Slf4j
@Service
public class ProductService {

    private final Map<Long, Product> productStore = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @PostConstruct
    public void init() {
        // 模拟数据
        save(new Product(null, "MacBook Pro 16", new BigDecimal("18999"), "电脑", 50, "ON_SALE", null));
        save(new Product(null, "iPhone 15 Pro", new BigDecimal("8999"), "手机", 200, "ON_SALE", null));
        save(new Product(null, "AirPods Pro", new BigDecimal("1899"), "配件", 500, "ON_SALE", null));
        save(new Product(null, "iPad Air", new BigDecimal("4799"), "平板", 100, "ON_SALE", null));
        save(new Product(null, "Apple Watch Ultra", new BigDecimal("5999"), "配件", 80, "ON_SALE", null));
        save(new Product(null, "iMac 24", new BigDecimal("9999"), "电脑", 30, "OFF_SHELF", null));
        save(new Product(null, "Mac Mini", new BigDecimal("4499"), "电脑", 60, "ON_SALE", null));
        save(new Product(null, "HomePod Mini", new BigDecimal("749"), "配件", 300, "OFF_SHELF", null));
        log.info("产品模拟数据初始化完成，共{}条", productStore.size());
    }

    /**
     * 查询所有产品（ADMIN可看全部，USER只看在售）
     */
    public List<Product> findAll(String role, String category, String keyword) {
        return productStore.values().stream()
                .filter(p -> {
                    // USER角色只能看在售产品
                    if (!"ADMIN".equals(role) && !"ON_SALE".equals(p.getStatus())) {
                        return false;
                    }
                    // 分类过滤
                    if (category != null && !category.isEmpty() && !category.equals(p.getCategory())) {
                        return false;
                    }
                    // 关键词搜索
                    if (keyword != null && !keyword.isEmpty()
                            && (p.getName() == null || !p.getName().toLowerCase().contains(keyword.toLowerCase()))) {
                        return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    /**
     * 根据ID查询产品
     */
    public Product findById(Long id) {
        Product product = productStore.get(id);
        if (product == null) {
            throw new IllegalArgumentException("产品不存在: " + id);
        }
        return product;
    }

    /**
     * 新增产品（仅ADMIN）
     */
    public Product save(Product product) {
        if (product.getId() == null) {
            product.setId(idGenerator.getAndIncrement());
        }
        if (product.getCreateTime() == null) {
            product.setCreateTime(LocalDateTime.now());
        }
        productStore.put(product.getId(), product);
        log.info("产品保存成功: id={}, name={}", product.getId(), product.getName());
        return product;
    }

    /**
     * 更新产品（仅ADMIN）
     */
    public Product update(Long id, Product product) {
        Product existing = findById(id);
        product.setId(id);
        product.setCreateTime(existing.getCreateTime());
        productStore.put(id, product);
        log.info("产品更新成功: id={}", id);
        return product;
    }

    /**
     * 删除产品（仅ADMIN）
     */
    public void delete(Long id) {
        Product removed = productStore.remove(id);
        if (removed == null) {
            throw new IllegalArgumentException("产品不存在: " + id);
        }
        log.info("产品删除成功: id={}, name={}", id, removed.getName());
    }

    /**
     * 获取所有分类
     */
    public List<String> findAllCategories() {
        return productStore.values().stream()
                .map(Product::getCategory)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}
