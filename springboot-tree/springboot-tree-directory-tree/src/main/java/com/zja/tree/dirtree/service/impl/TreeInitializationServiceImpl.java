package com.zja.tree.dirtree.service.impl;

import com.zja.tree.dirtree.entity.DirectoryNode;
import com.zja.tree.dirtree.exception.BusinessException;
import com.zja.tree.dirtree.model.dto.TreeNodeData;
import com.zja.tree.dirtree.model.request.CreateDirectoryRequest;
import com.zja.tree.dirtree.model.request.InitTreeRequest;
import com.zja.tree.dirtree.model.response.InitTreeResult;
import com.zja.tree.dirtree.repository.DirectoryNodeRepository;
import com.zja.tree.dirtree.service.DirectoryTreeService;
import com.zja.tree.dirtree.service.TreeInitializationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @Author: zhengja
 * @Date: 2025-11-06 13:21
 */
@Service
@Transactional
@Slf4j
public class TreeInitializationServiceImpl implements TreeInitializationService {

    @Autowired
    private DirectoryTreeService directoryTreeService;

    @Autowired
    private DirectoryNodeRepository directoryNodeRepository;

    @Override
    public InitTreeResult initializeTree(InitTreeRequest request) {
        log.info("开始初始化目录树，业务类型: {}", request.getBusinessType());

        // 检查是否已存在
        if (!request.isOverwrite() && isBusinessTypeInitialized(request.getBusinessType())) {
            throw new BusinessException("该业务类型的目录树已存在，如需覆盖请设置overwrite=true");
        }

        // 如果覆盖，先删除现有的
        if (request.isOverwrite()) {
            deleteExistingTree(request.getBusinessType());
        }

        // 构建目录树
        List<DirectoryNode> createdNodes = new ArrayList<>();
        int totalCount = buildTreeRecursively(request.getNodes(), null,
                request.getBusinessType(), request.getCreatedBy(), 0, createdNodes);

        log.info("目录树初始化完成，共创建 {} 个节点", totalCount);

        return InitTreeResult.builder()
                .businessType(request.getBusinessType())
                .createdBy(request.getCreatedBy())
                .totalNodes(totalCount)
                .rootNodes(createdNodes.stream()
                        .filter(DirectoryNode::isRoot)
                        .collect(Collectors.toList()))
                .createdNodes(createdNodes)
                .build();
    }

    /**
     * 递归构建目录树
     */
    private int buildTreeRecursively(List<TreeNodeData> nodeDataList, Long parentId,
                                     String businessType, String createdBy,
                                     int currentDepth, List<DirectoryNode> createdNodes) {
        if (nodeDataList == null || nodeDataList.isEmpty()) {
            return 0;
        }

        int count = 0;
        for (TreeNodeData nodeData : nodeDataList) {
            // 创建当前节点
            DirectoryNode node = createSingleNode(nodeData, parentId, businessType, createdBy, currentDepth);
            createdNodes.add(node);
            count++;

            // 递归创建子节点
            if (nodeData.getChildren() != null && !nodeData.getChildren().isEmpty()) {
                int childCount = buildTreeRecursively(nodeData.getChildren(), node.getId(),
                        businessType, createdBy, currentDepth + 1, createdNodes);
                count += childCount;
            }
        }

        return count;
    }

    /**
     * 创建单个节点
     */
    private DirectoryNode createSingleNode(TreeNodeData nodeData, Long parentId,
                                           String businessType, String createdBy, int depth) {
        CreateDirectoryRequest request = CreateDirectoryRequest.builder()
                .name(nodeData.getName())
                .alias(nodeData.getAlias())
                .description(nodeData.getDescription())
                .parentId(parentId)
                .businessType(businessType)
                .businessId(nodeData.getBusinessId())
                .nodeType(nodeData.getNodeType())
                .sortOrder(nodeData.getSortOrder())
                .icon(nodeData.getIcon())
                .customAttributes(nodeData.getCustomAttributes())
                .build();

        DirectoryNode node = directoryTreeService.createNode(request);

        // 设置额外属性
        node.setDepth(depth);

        // 设置创建者
        if (createdBy != null) {
            node.setCreatedBy(createdBy);
            node.setUpdatedBy(createdBy);
        }

        return directoryNodeRepository.save(node);
    }

    private void deleteExistingTree(String businessType) {
        List<DirectoryNode> existingNodes = directoryNodeRepository.findByBusinessType(businessType);
        if (!existingNodes.isEmpty()) {
            directoryNodeRepository.deleteAll(existingNodes);
            log.info("已删除业务类型 {} 的现有目录树，共 {} 个节点",
                    businessType, existingNodes.size());
        }
    }

    @Override
    public boolean isBusinessTypeInitialized(String businessType) {
        return directoryNodeRepository.countByBusinessType(businessType) > 0;
    }

    @Override
    public List<DirectoryNode> batchCreateNodes(List<CreateDirectoryRequest> requests) {
        List<DirectoryNode> createdNodes = new ArrayList<>();
        for (CreateDirectoryRequest request : requests) {
            DirectoryNode node = directoryTreeService.createNode(request);
            createdNodes.add(node);
        }
        return createdNodes;
    }

    @Async
    @Override
    public CompletableFuture<InitTreeResult> initializeTreeAsync(InitTreeRequest request) {
        return CompletableFuture.completedFuture(initializeTree(request));
    }

    @Override
    public List<DirectoryNode> buildTree(List<DirectoryNode> nodes) {
        if (nodes == null || nodes.isEmpty()) {
            return new ArrayList<>();
        }

        Map<Long, DirectoryNode> nodeMap = new HashMap<>();
        List<DirectoryNode> rootNodes = new ArrayList<>();

        // 按排序值排序
        nodes.sort(Comparator.comparing(DirectoryNode::getSortOrder));

        // 初始化所有节点
        for (DirectoryNode node : nodes) {
            nodeMap.put(node.getId(), node);
        }

        // 构建父子关系
        for (DirectoryNode node : nodes) {
            Long parentId = node.getParentId();
            if (parentId == null || parentId == 0 || !nodeMap.containsKey(parentId)) {
                rootNodes.add(node);
            } else {
                DirectoryNode parent = nodeMap.get(parentId);
                parent.getChildren().add(node);
            }
        }

        return rootNodes;
    }
}