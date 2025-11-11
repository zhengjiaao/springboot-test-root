package com.zja.tree.dirtree.entity.enums;

import lombok.Getter;

/**
 * @Author: zhengja
 * @Date: 2025-11-06 11:18
 */
@Getter
public enum TemplateType {
    PRODUCT_CATEGORY("product-category", "商品分类目录树"),
    DOCUMENT_STRUCTURE("document-structure", "文档结构目录树"),
    ORGANIZATION("organization", "组织架构目录树"),
    MENU_STRUCTURE("menu-structure", "菜单结构目录树"),
    PERMISSION("permission", "权限目录树"),
    KNOWLEDGE_BASE("knowledge-base", "知识库目录树");

    private final String templateName;
    private final String description;

    TemplateType(String templateName, String description) {
        this.templateName = templateName;
        this.description = description;
    }

    public String getTemplatePath() {
        return "classpath:tree-templates/" + templateName + ".json";
    }

    public static TemplateType fromName(String name) {
        for (TemplateType type : values()) {
            if (type.getTemplateName().equals(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的模板类型: " + name);
    }
}