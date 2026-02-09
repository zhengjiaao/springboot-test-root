package com.zja.dao;

import com.zja.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ProductDao 单元测试类
 *
 * @author: zhengja
 * @since: 2026/02/06 10:30
 */
@DataJpaTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class ProductDaoTest {

    @Autowired
    private ProductDao productDao;

    @Test
    void testSaveProduct() {
        // 准备测试数据
        Product product = new Product();
        product.setProductName("测试产品");
        product.setProductCode("TP001");
        product.setPrice(99.99);
        product.setDescription("这是一个测试产品");
        product.setCategory("测试分类");
        product.setStatus(1);

        // 执行保存操作
        Product savedProduct = productDao.save(product);

        // 验证结果
        assertNotNull(savedProduct.getId());
        assertEquals("测试产品", savedProduct.getProductName());
        assertEquals("TP001", savedProduct.getProductCode());
        assertEquals(99.99, savedProduct.getPrice());
    }

    @Test
    void testFindById() {
        // 准备测试数据
        Product product = new Product();
        product.setProductName("查找产品");
        product.setProductCode("FP001");
        product.setPrice(199.99);

        Product savedProduct = productDao.save(product);
        String productId = savedProduct.getId();

        // 执行查询操作
        Optional<Product> foundProduct = productDao.findById(productId);

        // 验证结果
        assertTrue(foundProduct.isPresent());
        assertEquals("查找产品", foundProduct.get().getProductName());
        assertEquals("FP001", foundProduct.get().getProductCode());
    }

    @Test
    void testFindByProductName() {
        // 准备测试数据
        Product product = new Product();
        product.setProductName("商品名称");
        product.setProductCode("PN001");
        product.setPrice(299.99);

        productDao.save(product);

        // 执行查询操作
        Optional<Product> foundProduct = productDao.findByProductName("商品名称");

        // 验证结果
        assertTrue(foundProduct.isPresent());
        assertEquals("商品名称", foundProduct.get().getProductName());
    }

    @Test
    void testFindByProductCode() {
        // 准备测试数据
        Product product = new Product();
        product.setProductName("商品名称");
        product.setProductCode("PCODE001");
        product.setPrice(399.99);

        productDao.save(product);

        // 执行查询操作
        Optional<Product> foundProduct = productDao.findByProductCode("PCODE001");

        // 验证结果
        assertTrue(foundProduct.isPresent());
        assertEquals("PCODE001", foundProduct.get().getProductCode());
    }

    @Test
    void testUpdateProduct() {
        // 准备测试数据
        Product product = new Product();
        product.setProductName("原产品名");
        product.setProductCode("UP001");
        product.setPrice(499.99);

        Product savedProduct = productDao.save(product);
        String productId = savedProduct.getId();

        // 修改产品信息
        savedProduct.setProductName("更新后产品名");
        savedProduct.setPrice(599.99);

        // 执行更新操作
        Product updatedProduct = productDao.save(savedProduct);

        // 验证结果
        assertEquals("更新后产品名", updatedProduct.getProductName());
        assertEquals(599.99, updatedProduct.getPrice());
    }

    @Test
    void testDeleteProduct() {
        // 准备测试数据
        Product product = new Product();
        product.setProductName("待删除产品");
        product.setProductCode("DP001");
        product.setPrice(699.99);

        Product savedProduct = productDao.save(product);
        String productId = savedProduct.getId();

        // 执行删除操作
        productDao.deleteById(productId);

        // 验证结果
        Optional<Product> deletedProduct = productDao.findById(productId);
        assertFalse(deletedProduct.isPresent());
    }
}