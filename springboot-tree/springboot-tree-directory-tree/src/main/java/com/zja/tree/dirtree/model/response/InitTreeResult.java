package com.zja.tree.dirtree.model.response;

import com.zja.tree.dirtree.entity.DirectoryNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2025-11-06 11:15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InitTreeResult implements Serializable {
    private String businessType;
    private String createdBy;
    private int totalNodes;
    private List<DirectoryNode> rootNodes;
    private List<DirectoryNode> createdNodes;
    private LocalDateTime initTime;
}