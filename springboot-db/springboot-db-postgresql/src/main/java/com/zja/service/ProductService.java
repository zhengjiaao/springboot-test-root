package com.zja.service;

import com.zja.model.base.PageData;
import com.zja.model.dto.ProductDTO;
import com.zja.model.request.ProductPageRequest;
import com.zja.model.request.ProductRequest;
import com.zja.model.request.ProductUpdateRequest;

import java.util.List;

/**
 * 产品 服务层
 *
 * @author: zhengja
 * @since: 2026/02/06 10:30
 */
public interface ProductService {

    /**
     * 查询产品
     *
     * @param id 产品id
     */
    ProductDTO queryById(String id);

    /**
     * 查询产品列表
     */
    List<ProductDTO> list();

    /**
     * 分页查询产品
     */
    PageData<ProductDTO> pageList(ProductPageRequest request);

    /**
     * 校验产品名称是否可用
     *
     * @param productName 产品名称
     * @return Boolean
     */
    Boolean existProductName(String productName);

    /**
     * 校验产品编码是否可用
     *
     * @param productCode 产品编码
     * @return Boolean
     */
    Boolean existProductCode(String productCode);

    /**
     * 新增产品
     */
    ProductDTO add(ProductRequest request);

    /**
     * 批量添加产品
     */
    List<ProductDTO> addBatch(List<ProductRequest> requests);

    /**
     * 更新产品
     *
     * @param id 产品id
     */
    ProductDTO update(String id, ProductUpdateRequest request);

    /**
     * 删除产品
     *
     * @param id 产品id
     */
    boolean deleteById(String id);

    /**
     * 批量删除产品
     *
     * @param ids 产品ids
     */
    void deleteBatch(List<String> ids);
}