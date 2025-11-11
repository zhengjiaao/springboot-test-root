package com.zja.tree.dirtree.enums;

import lombok.Getter;

/**
 * @Author: zhengja
 * @Date: 2025-11-06 10:39
 */
@Getter
public enum NodeType {

    DIRECTORY("目录"),
    FILE("文件"),
    FROM("表单"),
    CUSTOM("自定义");

    private final String description;

    NodeType(String description) {
        this.description = description;
    }

}