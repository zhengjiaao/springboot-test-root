package com.zja.tree.dirtree.model.dto;

import com.zja.tree.dirtree.entity.enums.NodeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhengja
 * @Date: 2025-11-06 10:28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JsonTreeDefinition implements Serializable {

    private String name;
    private String description;
    private String version;
    private String businessType;
    private String createdBy;
    private boolean overwrite = false;
    private List<JsonNodeDefinition> nodes;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JsonNodeDefinition {
        private String name;
        private String alias;
        private String description;
        private String icon;
        private NodeType nodeType;
        private Integer sortOrder = 0;
        private String businessId;
        private Map<String, String> customAttributes;
        private List<JsonNodeDefinition> children;
    }
}
