package com.zja.tree.dirtree.service;

import com.zja.tree.dirtree.entity.DirectoryNode;
import com.zja.tree.dirtree.model.request.CreateDirectoryRequest;
import com.zja.tree.dirtree.model.request.UpdateDirectoryRequest;

import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2025-11-06 10:29
 */
public interface DirectoryTreeService {

    DirectoryNode createNode(CreateDirectoryRequest request);

    List<DirectoryNode> getBusinessTree(String businessType);

    DirectoryNode getNode(Long nodeId);

    DirectoryNode updateNode(Long nodeId, UpdateDirectoryRequest request);

    void deleteNode(Long nodeId, boolean cascade);

    List<DirectoryNode> buildTree(List<DirectoryNode> nodes);

    List<DirectoryNode> getNodeChildren(Long nodeId);

    DirectoryNode getNodeByBusinessId(String businessType, String businessId);

    boolean existsByBusinessTypeAndBusinessId(String businessType, String businessId);

    void deleteBusinessTree(String businessType);

    DirectoryNode moveNode(Long nodeId, Long newParentId);

    List<DirectoryNode> findRootNodesByBusinessType(String businessType);
}