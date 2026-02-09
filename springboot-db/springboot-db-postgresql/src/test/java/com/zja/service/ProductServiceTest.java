package com.zja.service;

import com.zja.dao.ProductDao;
import com.zja.entity.Product;
import com.zja.mapper.ProductMapper;
import com.zja.model.base.PageData;
import com.zja.model.dto.ProductDTO;
import com.zja.model.request.ProductPageRequest;
import com.zja.model.request.ProductRequest;
import com.zja.model.request.ProductUpdateRequest;
import com.zja.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * ProductService 单元测试类
 *
 * @author: zhengja
 * @since: 2026/02/06 10:30
 */
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testQueryById_Success() {
        // 准备测试数据
        String productId = "1";
        Product product = new Product();
        product.setId(productId);
        product.setProductName("测试产品");
        product.setProductCode("TP001");

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(productId);
        productDTO.setProductName("测试产品");

        when(productDao.findById(productId)).thenReturn(Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(productDTO);

        // 执行测试
        ProductDTO result = productService.queryById(productId);

        // 验证结果
        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals("测试产品", result.getProductName());
        verify(productDao, times(1)).findById(productId);
        verify(productMapper, times(1)).toDto(product);
    }

    @Test
    void testQueryById_NotFound() {
        // 准备测试数据
        String productId = "nonexistent";

        when(productDao.findById(productId)).thenReturn(Optional.empty());

        // 执行测试并验证异常
        assertThrows(RuntimeException.class, () -> {
            productService.queryById(productId);
        });

        verify(productDao, times(1)).findById(productId);
    }

    @Test
    void testList() {
        // 准备测试数据
        Product product1 = new Product();
        product1.setId("1");
        product1.setProductName("产品1");

        Product product2 = new Product();
        product2.setId("2");
        product2.setProductName("产品2");

        List<Product> productList = Arrays.asList(product1, product2);

        ProductDTO dto1 = new ProductDTO();
        dto1.setId("1");
        dto1.setProductName("产品1");

        ProductDTO dto2 = new ProductDTO();
        dto2.setId("2");
        dto2.setProductName("产品2");

        List<ProductDTO> dtoList = Arrays.asList(dto1, dto2);

        when(productDao.findAll()).thenReturn(productList);
        when(productMapper.toDtoList(productList)).thenReturn(dtoList);

        // 执行测试
        List<ProductDTO> result = productService.list();

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("产品1", result.get(0).getProductName());
        assertEquals("产品2", result.get(1).getProductName());
        verify(productDao, times(1)).findAll();
        verify(productMapper, times(1)).toDtoList(productList);
    }

    @Test
    void testPageList() {
        // 准备测试数据
        ProductPageRequest pageRequest = new ProductPageRequest();
        pageRequest.setPage(1);
        pageRequest.setSize(10);

        Product product1 = new Product();
        product1.setId("1");
        product1.setProductName("分页产品1");

        List<Product> productList = Collections.singletonList(product1);
        Page<Product> productPage = new PageImpl<>(productList, PageRequest.of(0, 10), 1);

        ProductDTO dto1 = new ProductDTO();
        dto1.setId("1");
        dto1.setProductName("分页产品1");
        List<ProductDTO> dtoList = Collections.singletonList(dto1);

        when(productDao.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(productPage);
        when(productMapper.toDto(any(Product.class))).thenReturn(dtoList.get(0));

        // 执行测试
        PageData<ProductDTO> result = productService.pageList(pageRequest);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getLength());
        assertEquals(1, result.getCount());
        assertEquals("分页产品1", result.getData().get(0).getProductName());
    }

    @Test
    void testExistProductName_True() {
        // 准备测试数据
        String productName = "存在的产品名";
        Product product = new Product();
        product.setProductName(productName);

        when(productDao.findByProductName(productName)).thenReturn(Optional.of(product));

        // 执行测试
        Boolean result = productService.existProductName(productName);

        // 验证结果
        assertTrue(result);
        verify(productDao, times(1)).findByProductName(productName);
    }

    @Test
    void testExistProductName_False() {
        // 准备测试数据
        String productName = "不存在的产品名";

        when(productDao.findByProductName(productName)).thenReturn(Optional.empty());

        // 执行测试
        Boolean result = productService.existProductName(productName);

        // 验证结果
        assertFalse(result);
        verify(productDao, times(1)).findByProductName(productName);
    }

    @Test
    void testExistProductCode_True() {
        // 准备测试数据
        String productCode = "EXIST001";
        Product product = new Product();
        product.setProductCode(productCode);

        when(productDao.findByProductCode(productCode)).thenReturn(Optional.of(product));

        // 执行测试
        Boolean result = productService.existProductCode(productCode);

        // 验证结果
        assertTrue(result);
        verify(productDao, times(1)).findByProductCode(productCode);
    }

    @Test
    void testExistProductCode_False() {
        // 准备测试数据
        String productCode = "NOTEXIST001";

        when(productDao.findByProductCode(productCode)).thenReturn(Optional.empty());

        // 执行测试
        Boolean result = productService.existProductCode(productCode);

        // 验证结果
        assertFalse(result);
        verify(productDao, times(1)).findByProductCode(productCode);
    }

    @Test
    void testAdd_Success() {
        // 准备测试数据
        ProductRequest request = new ProductRequest();
        request.setProductName("新增产品");
        request.setProductCode("NEW001");
        request.setPrice(199.99);

        Product product = new Product();
        product.setId("1");
        product.setProductName("新增产品");
        product.setProductCode("NEW001");
        product.setPrice(199.99);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId("1");
        productDTO.setProductName("新增产品");
        productDTO.setProductCode("NEW001");
        productDTO.setPrice(199.99);

        when(productDao.findByProductName("新增产品")).thenReturn(Optional.empty());
        when(productDao.findByProductCode("NEW001")).thenReturn(Optional.empty());
        when(productMapper.toEntity(request)).thenReturn(product);
        when(productDao.save(any(Product.class))).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(productDTO);

        // 执行测试
        ProductDTO result = productService.add(request);

        // 验证结果
        assertNotNull(result);
        assertEquals("新增产品", result.getProductName());
        verify(productDao, times(1)).findByProductName("新增产品");
        verify(productDao, times(1)).findByProductCode("NEW001");
        verify(productMapper, times(1)).toEntity(request);
        verify(productDao, times(1)).save(any(Product.class));
        verify(productMapper, times(1)).toDto(product);
    }

    @Test
    void testAdd_ProductNameExists() {
        // 准备测试数据
        ProductRequest request = new ProductRequest();
        request.setProductName("已存在产品");
        request.setProductCode("EXIST002");

        Product existingProduct = new Product();
        existingProduct.setProductName("已存在产品");

        when(productDao.findByProductName("已存在产品")).thenReturn(Optional.of(existingProduct));

        // 执行测试并验证异常
        assertThrows(UnsupportedOperationException.class, () -> {
            productService.add(request);
        });

        verify(productDao, times(1)).findByProductName("已存在产品");
    }

    @Test
    void testAdd_ProductCodeExists() {
        // 准备测试数据
        ProductRequest request = new ProductRequest();
        request.setProductName("新产品");
        request.setProductCode("EXIST003");

        when(productDao.findByProductName("新产品")).thenReturn(Optional.empty());
        when(productDao.findByProductCode("EXIST003")).thenReturn(Optional.of(new Product()));

        // 执行测试并验证异常
        assertThrows(UnsupportedOperationException.class, () -> {
            productService.add(request);
        });

        verify(productDao, times(1)).findByProductName("新产品");
        verify(productDao, times(1)).findByProductCode("EXIST003");
    }

    @Test
    void testUpdate_Success() {
        // 准备测试数据
        String productId = "1";
        ProductUpdateRequest updateRequest = new ProductUpdateRequest();
        updateRequest.setProductName("更新后产品名");
        updateRequest.setPrice(299.99);

        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setProductName("原产品名");
        existingProduct.setProductCode("UP002");

        Product updatedProduct = new Product();
        updatedProduct.setId(productId);
        updatedProduct.setProductName("更新后产品名");
        updatedProduct.setProductCode("UP002");
        updatedProduct.setPrice(299.99);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(productId);
        productDTO.setProductName("更新后产品名");
        productDTO.setPrice(299.99);

        when(productDao.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productDao.save(any(Product.class))).thenReturn(updatedProduct);
        when(productMapper.toDto(updatedProduct)).thenReturn(productDTO);

        // 执行测试
        ProductDTO result = productService.update(productId, updateRequest);

        // 验证结果
        assertNotNull(result);
        assertEquals("更新后产品名", result.getProductName());
        assertEquals(299.99, result.getPrice());
        verify(productDao, times(1)).findById(productId);
        verify(productDao, times(1)).save(any(Product.class));
        verify(productMapper, times(1)).toDto(updatedProduct);
    }

    @Test
    void testDeleteById_Success() {
        // 准备测试数据
        String productId = "1";
        Product product = new Product();
        product.setId(productId);

        when(productDao.findById(productId)).thenReturn(Optional.of(product));

        // 执行测试
        boolean result = productService.deleteById(productId);

        // 验证结果
        assertTrue(result);
        verify(productDao, times(1)).findById(productId);
        verify(productDao, times(1)).deleteById(productId);
    }

    @Test
    void testDeleteById_NotFound() {
        // 准备测试数据
        String productId = "nonexistent";

        when(productDao.findById(productId)).thenReturn(Optional.empty());

        // 执行测试
        boolean result = productService.deleteById(productId);

        // 验证结果
        assertTrue(result); // 即使未找到也返回true
        verify(productDao, times(1)).findById(productId);
        verify(productDao, never()).deleteById(anyString()); // 不会调用删除
    }
}