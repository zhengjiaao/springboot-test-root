package com.zja.tree.dirtree.controller;

import com.zja.tree.dirtree.model.request.InitTreeRequest;
import com.zja.tree.dirtree.model.response.InitTreeResult;
import com.zja.tree.dirtree.repository.DirectoryNodeRepository;
import com.zja.tree.dirtree.service.TreeInitializationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zhengja
 * @Date: 2025-11-06 13:54
 */
@RestController
@RequestMapping("/api/directory/tree")
public class TreeInitializationController {

    @Autowired
    private TreeInitializationService treeInitializationService;

    @Autowired
    private DirectoryNodeRepository directoryNodeRepository;

    /**
     * 初始化目录树
     */
    @PostMapping("/init")
    public ResponseEntity<InitTreeResult> initializeTree(
            @RequestBody @Valid InitTreeRequest request) {
        InitTreeResult result = treeInitializationService.initializeTree(request);
        return ResponseEntity.ok(result);
    }

    /**
     * 异步初始化目录树
     */
    @PostMapping("/init/async")
    public ResponseEntity<Map<String, Object>> initializeTreeAsync(
            @RequestBody @Valid InitTreeRequest request) {
        treeInitializationService.initializeTreeAsync(request);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "目录树初始化任务已提交，正在后台处理");
        response.put("businessType", request.getBusinessType());
        response.put("status", "processing");

        return ResponseEntity.accepted().body(response);
    }

    /**
     * 检查业务类型是否已初始化
     */
    @GetMapping("/init/check/{businessType}")
    public ResponseEntity<Map<String, Object>> checkInitialized(
            @PathVariable String businessType) {
        boolean initialized = treeInitializationService.isBusinessTypeInitialized(businessType);
        Map<String, Object> result = new HashMap<>();
        result.put("businessType", businessType);
        result.put("initialized", initialized);

        if (initialized) {
            long nodeCount = directoryNodeRepository.countByBusinessType(businessType);
            result.put("nodeCount", nodeCount);
        }

        return ResponseEntity.ok(result);
    }
}