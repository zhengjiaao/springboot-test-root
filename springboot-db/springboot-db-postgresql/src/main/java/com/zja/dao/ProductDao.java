package com.zja.dao;

import com.zja.entity.Product;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 产品 数据访问层
 *
 * @author: zhengja
 * @since: 2026/02/06 10:30
 */
@Repository
public interface ProductDao extends JpaRepository<Product, String>, CrudRepository<Product, String>, JpaSpecificationExecutor<Product> {

    /**
     * 根据产品名称查询产品
     *
     * @param productName 产品名称
     * @return Optional<Product>
     */
    Optional<Product> findByProductName(String productName);

    /**
     * 根据产品编码查询产品
     *
     * @param productCode 产品编码
     * @return Optional<Product>
     */
    Optional<Product> findByProductCode(String productCode);
    
    /**
     * 根据状态查询产品列表
     * @param status 状态
     * @return List<Product>
     */
    List<Product> findByStatus(Integer status);
    
    /**
     * 根据名称和状态查询产品
     * @param productName 产品名称
     * @param status 状态
     * @return Optional<Product>
     */
    Optional<Product> findByProductNameAndStatus(String productName, Integer status);
}