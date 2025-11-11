package com.zja.tree.dirtree.model.dto;

import com.zja.tree.dirtree.entity.enums.NodeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhengja
 * @Date: 2025-11-06 11:15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TreeNodeData implements Serializable {

    @NotBlank(message = "节点名称不能为空")
    private String name;

    private String alias;
    private String description;
    private String icon;

    @NotNull(message = "节点类型不能为空")
    private NodeType nodeType;

    private Integer sortOrder = 0;
    private String businessId;
    private List<TreeNodeData> children;
    private Map<String, String> customAttributes;
}
