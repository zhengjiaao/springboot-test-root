package com.zja.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zja.model.base.PageData;
import com.zja.model.dto.ProductDTO;
import com.zja.model.request.ProductPageRequest;
import com.zja.model.request.ProductRequest;
import com.zja.model.request.ProductUpdateRequest;
import com.zja.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ProductController 单元测试类
 *
 * @author: zhengja
 * @since: 2026/02/06 10:30
 */
class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private JacksonTester<ProductRequest> jsonProductRequest;
    private JacksonTester<ProductUpdateRequest> jsonProductUpdateRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .build();
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    void testQueryById() throws Exception {
        // 准备测试数据
        String productId = "1";
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(productId);
        productDTO.setProductName("测试产品");
        productDTO.setProductCode("TP001");
        productDTO.setPrice(99.99);

        given(productService.queryById(productId)).willReturn(productDTO);

        // 执行请求
        MockHttpServletResponse response = mockMvc.perform(
                        get("/rest/product/query/{id}", productId))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse();
        response.setCharacterEncoding("UTF-8");

        // 验证结果
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString())
                .contains("测试产品")
                .contains("TP001")
                .contains("99.99");
    }

    @Test
    void testList() throws Exception {
        // 准备测试数据
        ProductDTO productDTO1 = new ProductDTO();
        productDTO1.setId("1");
        productDTO1.setProductName("产品1");
        productDTO1.setProductCode("P001");

        ProductDTO productDTO2 = new ProductDTO();
        productDTO2.setId("2");
        productDTO2.setProductName("产品2");
        productDTO2.setProductCode("P002");

        List<ProductDTO> productList = Arrays.asList(productDTO1, productDTO2);
        given(productService.list()).willReturn(productList);

        // 执行请求
        MockHttpServletResponse response = mockMvc.perform(
                        get("/rest/product/list"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse();
        response.setCharacterEncoding("UTF-8");

        // 验证结果
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString())
                .contains("产品1")
                .contains("产品2")
                .contains("P001")
                .contains("P002");
    }

    @Test
    void testPageList() throws Exception {
        // 准备测试数据
        ProductPageRequest pageRequest = new ProductPageRequest();
        pageRequest.setPage(2);
        pageRequest.setSize(10);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId("1");
        productDTO.setProductName("分页产品");
        productDTO.setProductCode("PP001");

        PageData<ProductDTO> pageData = new PageData<>();
        pageData.setData(Collections.singletonList(productDTO));
        pageData.setIndex(0);
        pageData.setSize(10);
        pageData.setCount(1);

        given(productService.pageList(any(ProductPageRequest.class))).willReturn(pageData);

        // 执行请求
        MockHttpServletResponse response = mockMvc.perform(
                        get("/rest/product/page/list")
                                .param("page", "1")
                                .param("size", "10"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse();
        response.setCharacterEncoding("UTF-8");

        // 验证结果
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString())
                .contains("分页产品")
                .contains("PP001");
    }

    @Test
    void testAdd() throws Exception {
        // 准备测试数据
        ProductRequest request = new ProductRequest();
        request.setProductName("新增产品");
        request.setProductCode("NP001");
        request.setPrice(199.99);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId("1");
        productDTO.setProductName("新增产品");
        productDTO.setProductCode("NP001");
        productDTO.setPrice(199.99);

        given(productService.add(any(ProductRequest.class))).willReturn(productDTO);

        // 执行请求
        MockHttpServletResponse response = mockMvc.perform(
                        post("/rest/product/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(jsonProductRequest.write(request).getJson()))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse();
        response.setCharacterEncoding("UTF-8");

        // 验证结果
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString())
                .contains("新增产品")
                .contains("NP001")
                .contains("199.99");
    }

    @Test
    void testAddBatch() throws Exception {
        // 准备测试数据
        ProductRequest request1 = new ProductRequest();
        request1.setProductName("批量产品1");
        request1.setProductCode("BP001");
        request1.setPrice(299.99);

        ProductRequest request2 = new ProductRequest();
        request2.setProductName("批量产品2");
        request2.setProductCode("BP002");
        request2.setPrice(399.99);

        List<ProductRequest> requests = Arrays.asList(request1, request2);

        ProductDTO productDTO1 = new ProductDTO();
        productDTO1.setId("1");
        productDTO1.setProductName("批量产品1");
        productDTO1.setPrice(299.99);

        ProductDTO productDTO2 = new ProductDTO();
        productDTO2.setId("2");
        productDTO2.setProductName("批量产品2");
        productDTO2.setPrice(399.99);

        List<ProductDTO> result = Arrays.asList(productDTO1, productDTO2);

        given(productService.addBatch(any(List.class))).willReturn(result);

        // 执行请求
        MockHttpServletResponse response = mockMvc.perform(
                        post("/rest/product/add/batch")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content("[{\"productName\":\"批量产品1\",\"productCode\":\"BP001\",\"price\":299.99},{\"productName\":\"批量产品2\",\"productCode\":\"BP002\",\"price\":399.99}]"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse();
        response.setCharacterEncoding("UTF-8");

        // 验证结果
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString())
                .contains("批量产品1")
                .contains("批量产品2");
    }

    @Test
    void testUpdate() throws Exception {
        // 准备测试数据
        String productId = "1";
        ProductUpdateRequest updateRequest = new ProductUpdateRequest();
        updateRequest.setProductName("更新后产品");
        updateRequest.setPrice(499.99);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(productId);
        productDTO.setProductName("更新后产品");
        productDTO.setPrice(499.99);

        given(productService.update(eq(productId), any(ProductUpdateRequest.class))).willReturn(productDTO);

        // 执行请求
        MockHttpServletResponse response = mockMvc.perform(
                        put("/rest/product/update/{id}", productId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(jsonProductUpdateRequest.write(updateRequest).getJson()))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse();
        response.setCharacterEncoding("UTF-8");

        // 验证结果
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString())
                .contains("更新后产品")
                .contains("499.99");
    }

    @Test
    void testDeleteById() throws Exception {
        // 准备测试数据
        String productId = "1";
        when(productService.deleteById(productId)).thenReturn(true);

        // 执行请求
        MockHttpServletResponse response = mockMvc.perform(
                        delete("/rest/product/delete/{id}", productId))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse();
        response.setCharacterEncoding("UTF-8");

        // 验证结果
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).isEqualTo("true");
    }

    @Test
    void testDeleteBatch() throws Exception {
        // 准备测试数据
        List<String> ids = Arrays.asList("1", "2", "3");
        doNothing().when(productService).deleteBatch(ids);

        // 执行请求
        MockHttpServletResponse response = mockMvc.perform(
                        delete("/rest/product/delete/batch")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("[\"1\", \"2\", \"3\"]"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse();
        response.setCharacterEncoding("UTF-8");

        // 验证结果
        assertThat(response.getStatus()).isEqualTo(200);
    }
}