package com.zja.tree.dirtree.service;

import com.zja.tree.dirtree.entity.DirectoryNode;
import com.zja.tree.dirtree.model.request.CreateDirectoryRequest;
import com.zja.tree.dirtree.model.request.InitTreeRequest;
import com.zja.tree.dirtree.model.response.InitTreeResult;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @Author: zhengja
 * @Date: 2025-11-06 13:21
 */
public interface TreeInitializationService {

    InitTreeResult initializeTree(InitTreeRequest request);

    List<DirectoryNode> batchCreateNodes(List<CreateDirectoryRequest> requests);

    boolean isBusinessTypeInitialized(String businessType);

    CompletableFuture<InitTreeResult> initializeTreeAsync(InitTreeRequest request);

    List<DirectoryNode> buildTree(List<DirectoryNode> nodes);
}