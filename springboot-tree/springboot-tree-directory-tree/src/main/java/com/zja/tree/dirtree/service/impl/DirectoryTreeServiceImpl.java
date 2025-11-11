package com.zja.tree.dirtree.service.impl;

import com.zja.tree.dirtree.entity.DirectoryNode;
import com.zja.tree.dirtree.exception.BusinessException;
import com.zja.tree.dirtree.model.request.CreateDirectoryRequest;
import com.zja.tree.dirtree.model.request.UpdateDirectoryRequest;
import com.zja.tree.dirtree.repository.DirectoryNodeRepository;
import com.zja.tree.dirtree.service.DirectoryTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: zhengja
 * @Date: 2025-11-06 10:29
 */
@Service
@Transactional
public class DirectoryTreeServiceImpl implements DirectoryTreeService {

    @Autowired
    private DirectoryNodeRepository directoryNodeRepository;

    @Override
    public DirectoryNode createNode(CreateDirectoryRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("CreateDirectoryRequest cannot be null");
        }

        // 验证父节点是否存在（如果parentId不是-1）
        Long parentId = request.getParentId() != null ? request.getParentId() : -1L;

        // 验证父节点是否存在（如果parentId不是-1）
        if (!parentId.equals(-1L)) {
            Optional<DirectoryNode> parentOpt = directoryNodeRepository.findById(parentId);
            if (!parentOpt.isPresent()) {
                throw new BusinessException("父节点不存在: " + parentId);
            }
            DirectoryNode parent = parentOpt.get();

            // 设置深度
            int depth = parent.getDepth() + 1;

            // 构建完整路径
            String fullPath = buildFullPath(parent, request.getName());

            // 创建节点
            return createNodeWithPath(request, parentId, depth, fullPath);
        } else {
            // 创建根节点
            return createNodeWithPath(request, -1L, 0, "/" + request.getName());
        }
    }

    /**
     * 构建完整路径
     */
    private String buildFullPath(DirectoryNode parent, String nodeName) {
        if (parent.isRoot()) {
            return "/" + nodeName;
        } else {
            return parent.getFullPath() + "/" + nodeName;
        }
    }

    private DirectoryNode createNodeWithPath(CreateDirectoryRequest request, Long parentId,
                                             Integer depth, String fullPath) {
        // 检查业务ID是否重复
        if (request.getBusinessId() != null && !request.getBusinessId().isEmpty()) {
            boolean exists = directoryNodeRepository.existsByBusinessTypeAndBusinessId(
                    request.getBusinessType(), request.getBusinessId());
            if (exists) {
                throw new BusinessException("业务ID已存在: " + request.getBusinessId());
            }
        }

        // 对于根节点，检查是否已存在同业务类型的根节点
        if (parentId.equals(-1L)) {
            List<DirectoryNode> existingRoots = directoryNodeRepository
                    .findRootNodesByBusinessType(request.getBusinessType());
            if (!existingRoots.isEmpty() && request.getBusinessId() == null) {
                throw new BusinessException("该业务类型下已存在根节点，请指定businessId或使用子节点");
            }
        }

        DirectoryNode node = new DirectoryNode();
        node.setName(request.getName());
        node.setAlias(request.getAlias());
        node.setDescription(request.getDescription());
        node.setParentId(parentId);
        node.setBusinessType(request.getBusinessType());
        node.setBusinessId(request.getBusinessId());
        node.setNodeType(request.getNodeType());
        node.setSortOrder(request.getSortOrder());
        node.setIcon(request.getIcon());
        node.setDepth(depth);
        node.setFullPath(fullPath);

        if (request.getCustomAttributes() != null) {
            node.setCustomAttributes(new HashMap<>(request.getCustomAttributes()));
        }

        return directoryNodeRepository.save(node);
    }

    @Override
    public List<DirectoryNode> getBusinessTree(String businessType) {
        List<DirectoryNode> allNodes = directoryNodeRepository.findByBusinessType(businessType);
        return buildTree(allNodes);
    }

    @Override
    public DirectoryNode getNode(Long nodeId) {
        return directoryNodeRepository.findById(nodeId)
                .orElseThrow(() -> new BusinessException("节点不存在: " + nodeId));
    }

    @Override
    public DirectoryNode updateNode(Long nodeId, UpdateDirectoryRequest request) {
        DirectoryNode node = getNode(nodeId);

        if (request.getName() != null && !request.getName().equals(node.getName())) {
            node.setName(request.getName());
            // 更新完整路径（需要递归更新子节点路径）
            updateNodePath(node);
        }

        if (request.getAlias() != null) {
            node.setAlias(request.getAlias());
        }

        if (request.getDescription() != null) {
            node.setDescription(request.getDescription());
        }

        if (request.getSortOrder() != null) {
            node.setSortOrder(request.getSortOrder());
        }

        if (request.getIcon() != null) {
            node.setIcon(request.getIcon());
        }

        if (request.getCustomAttributes() != null) {
            node.setCustomAttributes(new HashMap<>(request.getCustomAttributes()));
        }

        // node.setUpdateTime(LocalDateTime.now());

        return directoryNodeRepository.save(node);
    }

    private void updateNodePath(DirectoryNode node) {
        String newFullPath;
        if (node.isRoot()) {
            newFullPath = "/" + node.getName();
        } else {
            DirectoryNode parent = directoryNodeRepository.findById(node.getParentId())
                    .orElseThrow(() -> new BusinessException("父节点不存在: " + node.getParentId()));
            newFullPath = parent.getFullPath() + "/" + node.getName();
        }

        node.setFullPath(newFullPath);

        // 递归更新子节点路径
        updateChildrenPaths(node);
    }

    private void updateChildrenPaths(DirectoryNode parent) {
        List<DirectoryNode> children = directoryNodeRepository.findByParentId(parent.getId());
        for (DirectoryNode child : children) {
            String childFullPath = parent.getFullPath() + "/" + child.getName();
            child.setFullPath(childFullPath);
            directoryNodeRepository.save(child);
            updateChildrenPaths(child);
        }
    }

    @Override
    public void deleteNode(Long nodeId, boolean cascade) {
        DirectoryNode node = getNode(nodeId);

        // 防止删除根节点（除非级联删除整个业务树）
        if (node.isRoot() && !cascade) {
            throw new BusinessException("根节点不能直接删除，请使用级联删除或删除整个业务树");
        }

        if (cascade) {
            // 级联删除所有子节点
            deleteSubtree(nodeId);
        } else {
            // 检查是否有子节点
            long childCount = directoryNodeRepository.countByParentId(nodeId);
            if (childCount > 0) {
                throw new BusinessException("节点存在子节点，无法直接删除。请使用级联删除或先删除子节点。");
            }
            directoryNodeRepository.delete(node);
        }
    }

    private void deleteSubtree(Long nodeId) {
        List<DirectoryNode> subtree = directoryNodeRepository.findSubtree(nodeId);
        directoryNodeRepository.deleteAll(subtree);
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
            if (parentId == null || parentId.equals(-1L) || !nodeMap.containsKey(parentId)) {
                rootNodes.add(node);
            } else {
                DirectoryNode parent = nodeMap.get(parentId);
                parent.getChildren().add(node);
            }
        }

        return rootNodes;
    }

    @Override
    public List<DirectoryNode> getNodeChildren(Long nodeId) {
        return directoryNodeRepository.findByParentId(nodeId)
                .stream()
                .sorted(Comparator.comparing(DirectoryNode::getSortOrder))
                .collect(Collectors.toList());
    }

    @Override
    public DirectoryNode getNodeByBusinessId(String businessType, String businessId) {
        return directoryNodeRepository.findByBusinessTypeAndBusinessId(businessType, businessId)
                .orElseThrow(() -> new BusinessException(
                        "节点不存在: businessType=" + businessType + ", businessId=" + businessId));
    }

    @Override
    public boolean existsByBusinessTypeAndBusinessId(String businessType, String businessId) {
        return directoryNodeRepository.existsByBusinessTypeAndBusinessId(businessType, businessId);
    }

    /**
     * 删除整个业务树
     */
    @Override
    public void deleteBusinessTree(String businessType) {
        List<DirectoryNode> businessNodes = directoryNodeRepository.findByBusinessType(businessType);
        if (!businessNodes.isEmpty()) {
            directoryNodeRepository.deleteAll(businessNodes);
        }
    }

    /**
     * 移动节点到新的父节点
     */
    @Override
    public DirectoryNode moveNode(Long nodeId, Long newParentId) {
        DirectoryNode node = getNode(nodeId);
        DirectoryNode newParent = getNode(newParentId);

        // 防止循环引用
        if (isCircularReference(node, newParent)) {
            throw new BusinessException("不能将节点移动到其子节点下");
        }

        node.setParentId(newParentId);
        node.setDepth(newParent.getDepth() + 1);
        node.setFullPath(buildFullPath(newParent, node.getName()));

        // 更新子节点路径
        updateChildrenPaths(node);

        return directoryNodeRepository.save(node);
    }

    private boolean isCircularReference(DirectoryNode node, DirectoryNode potentialParent) {
        // 检查potentialParent是否是node的子节点
        List<DirectoryNode> subtree = directoryNodeRepository.findSubtree(node.getId());
        return subtree.stream().anyMatch(n -> n.getId().equals(potentialParent.getId()));
    }

    @Override
    public List<DirectoryNode> findRootNodesByBusinessType(String businessType) {
        return directoryNodeRepository.findRootNodesByBusinessType(businessType);
    }
}