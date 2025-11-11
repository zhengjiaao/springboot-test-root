package com.zja.tree.dirtree.controller;

import com.zja.tree.dirtree.entity.DirectoryNode;
import com.zja.tree.dirtree.model.request.CreateDirectoryRequest;
import com.zja.tree.dirtree.model.request.UpdateDirectoryRequest;
import com.zja.tree.dirtree.service.DirectoryTreeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2025-11-06 10:28
 */
@RestController
@RequestMapping("/api/directory/tree")
@RequiredArgsConstructor
public class DirectoryTreeController {

    private final DirectoryTreeService directoryTreeService;

    /**
     * 创建目录节点
     */
    @PostMapping
    public ResponseEntity<DirectoryNode> createNode(@RequestBody @Valid CreateDirectoryRequest request) {
        DirectoryNode node = directoryTreeService.createNode(request);
        return ResponseEntity.ok(node);
    }

    /**
     * 获取业务目录树
     */
    @GetMapping("/business/{businessType}")
    public ResponseEntity<List<DirectoryNode>> getBusinessTree(@PathVariable String businessType) {
        List<DirectoryNode> tree = directoryTreeService.getBusinessTree(businessType);
        return ResponseEntity.ok(tree);
    }

    /**
     * 获取节点详情
     */
    @GetMapping("/{nodeId}")
    public ResponseEntity<DirectoryNode> getNode(@PathVariable Long nodeId) {
        DirectoryNode node = directoryTreeService.getNode(nodeId);
        return ResponseEntity.ok(node);
    }

    /**
     * 获取节点的直接子节点
     */
    @GetMapping("/{nodeId}/children")
    public ResponseEntity<List<DirectoryNode>> getNodeChildren(@PathVariable Long nodeId) {
        List<DirectoryNode> children = directoryTreeService.getNodeChildren(nodeId);
        return ResponseEntity.ok(children);
    }

    /**
     * 根据业务ID获取节点
     */
    @GetMapping("/business/{businessType}/id/{businessId}")
    public ResponseEntity<DirectoryNode> getNodeByBusinessId(
            @PathVariable String businessType,
            @PathVariable String businessId) {
        DirectoryNode node = directoryTreeService.getNodeByBusinessId(businessType, businessId);
        return ResponseEntity.ok(node);
    }

    /**
     * 更新目录节点
     */
    @PutMapping("/{nodeId}")
    public ResponseEntity<DirectoryNode> updateNode(
            @PathVariable Long nodeId,
            @RequestBody @Valid UpdateDirectoryRequest request) {
        DirectoryNode node = directoryTreeService.updateNode(nodeId, request);
        return ResponseEntity.ok(node);
    }

    /**
     * 删除目录节点
     */
    @DeleteMapping("/{nodeId}")
    public ResponseEntity<Void> deleteNode(
            @PathVariable Long nodeId,
            @RequestParam(defaultValue = "false") boolean cascade) {
        directoryTreeService.deleteNode(nodeId, cascade);
        return ResponseEntity.noContent().build();
    }

    /**
     * 获取业务根节点
     */
    @GetMapping("/business/{businessType}/roots")
    public ResponseEntity<List<DirectoryNode>> getBusinessRoots(@PathVariable String businessType) {
        List<DirectoryNode> rootNodes = directoryTreeService.findRootNodesByBusinessType(businessType);
        return ResponseEntity.ok(rootNodes);
    }

    /**
     * 移动节点
     */
    @PutMapping("/{nodeId}/move")
    public ResponseEntity<DirectoryNode> moveNode(
            @PathVariable Long nodeId,
            @RequestParam Long newParentId) {
        DirectoryNode node = directoryTreeService.moveNode(nodeId, newParentId);
        return ResponseEntity.ok(node);
    }

    /**
     * 删除整个业务树
     */
    @DeleteMapping("/business/{businessType}")
    public ResponseEntity<Void> deleteBusinessTree(@PathVariable String businessType) {
        directoryTreeService.deleteBusinessTree(businessType);
        return ResponseEntity.noContent().build();
    }
}