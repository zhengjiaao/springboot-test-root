package com.zja.security.controller;

import com.zja.security.model.Product;
import com.zja.security.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 产品管理 接口层
 * <p>
 * 权限说明：
 * - GET    /api/products          — 所有认证用户可访问（USER仅看在售，ADMIN看全部）
 * - GET    /api/products/{id}     — 所有认证用户
 * - GET    /api/products/categories — 所有认证用户
 * - POST   /api/products          — 仅ADMIN
 * - PUT    /api/products/{id}     — 仅ADMIN
 * - DELETE /api/products/{id}     — 仅ADMIN
 *
 * @author: zhengja
 * @since: 2026/05/12
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/api/products")
@Api(tags = {"产品管理"})
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @ApiOperation("查询产品列表")
    public ResponseEntity<List<Product>> list(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword) {
        // 根据角色决定数据范围：ADMIN看全部，USER仅看在售
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        String role = isAdmin ? "ADMIN" : "USER";
        List<Product> products = productService.findAll(role, category, keyword);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/categories")
    @ApiOperation("获取产品分类列表")
    public ResponseEntity<List<String>> categories() {
        return ResponseEntity.ok(productService.findAllCategories());
    }

    @GetMapping("/{id}")
    @ApiOperation("查询产品详情")
    public ResponseEntity<Product> detail(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping
    @ApiOperation("新增产品")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> create(@Valid @RequestBody Product product) {
        return ResponseEntity.ok(productService.save(product));
    }

    @PutMapping("/{id}")
    @ApiOperation("更新产品")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> update(@PathVariable Long id, @Valid @RequestBody Product product) {
        return ResponseEntity.ok(productService.update(id, product));
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除产品")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok().build();
    }
}
