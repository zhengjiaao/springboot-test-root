package com.zja.service.impl;

import com.zja.dao.ProductDao;
import com.zja.entity.Product;
import com.zja.mapper.ProductMapper;
import com.zja.model.base.PageData;
import com.zja.model.dto.ProductDTO;
import com.zja.model.request.ProductPageRequest;
import com.zja.model.request.ProductRequest;
import com.zja.model.request.ProductUpdateRequest;
import com.zja.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 产品 业务处理服务实现层
 *
 * @author: zhengja
 * @since: 2026/02/06 10:30
 */
@Slf4j
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductDao dao;

    @Autowired
    ProductMapper mapper;

    private Product getProductById(String id) {
        return dao.findById(id).orElseThrow(() -> new RuntimeException(String.format("传入的 id=%s 有误！", id)));
    }

    @Override
    public ProductDTO queryById(String id) {
        Product entity = this.getProductById(id);
        return mapper.toDto(entity);
    }

    @Override
    public List<ProductDTO> list() {
        List<Product> list = dao.findAll();
        return mapper.toDtoList(list);
    }

    @Override
    public PageData<ProductDTO> pageList(ProductPageRequest request) {
        int page = Math.max(0, request.getPage());
        int size = request.getSize();
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");

        // 查询条件
        Specification<Product> spec = buildQuery(request);
        // 分页查询
        Page<Product> sourcePage = dao.findAll(spec, PageRequest.of(page, size, sort));

        return PageData.of(sourcePage.map(mapper::toDto));
    }

    private Specification<Product> buildQuery(ProductPageRequest request) {
        // 构建查询条件
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // 产品名称
            if (request.getProductName() != null && !request.getProductName().isEmpty()) {
                predicates.add(cb.like(root.get("productName"), "%" + request.getProductName() + "%"));
            }
            // 产品编码
            if (request.getProductCode() != null && !request.getProductCode().isEmpty()) {
                predicates.add(cb.like(root.get("productCode"), "%" + request.getProductCode() + "%"));
            }
            // 产品分类
            if (request.getCategory() != null && !request.getCategory().isEmpty()) {
                predicates.add(cb.equal(root.get("category"), request.getCategory()));
            }
            // 产品状态
            if (request.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), request.getStatus()));
            }
            
            // 将条件连接在一起
            return query.where(predicates.toArray(new Predicate[0])).getRestriction();
        };
    }

    @Override
    public Boolean existProductName(String productName) {
        Optional<Product> byProductName = dao.findByProductName(productName);
        return byProductName.isPresent();
    }

    @Override
    public Boolean existProductCode(String productCode) {
        Optional<Product> byProductCode = dao.findByProductCode(productCode);
        return byProductCode.isPresent();
    }

    @Override
    @Transactional
    public ProductDTO add(ProductRequest request) {
        // 检验名字已存在
        if (existProductName(request.getProductName())) {
            throw new UnsupportedOperationException("产品名称不能重复");
        }
        
        // 检验产品编码已存在
        if (existProductCode(request.getProductCode())) {
            throw new UnsupportedOperationException("产品编码不能重复");
        }

        Product entity = mapper.toEntity(request);
        entity = dao.save(entity);

        return mapper.toDto(entity);
    }

    @Override
    @Transactional
    public List<ProductDTO> addBatch(List<ProductRequest> requests) {
        return requests.stream().map(this::add).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductDTO update(String id, ProductUpdateRequest request) {
        // 校验存在
        Product entity = this.getProductById(id);

        // 更新
        mapper.updateEntityFromRequest(request, entity);
        entity = dao.save(entity);

        return mapper.toDto(entity);
    }

    @Override
    @Transactional
    public boolean deleteById(String id) {
        try {
            if (dao.findById(id).isPresent()) {
                dao.deleteById(id);
            }
            return true;
        } catch (Exception e) {
            log.error("Failed to delete entity with ID: {}", id, e);
            return false;
        }
    }

    @Override
    @Transactional
    public void deleteBatch(List<String> ids) {
        ids.forEach(this::deleteById);
    }
}