package com.zja.custom.util.entity;

import com.zja.custom.util.tree3.BaseTreeNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2024-10-24 16:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuEntity implements BaseTreeNode<MenuEntity> {

    private Long id;
    private Long parentId;
    private String name;
    private String type;
    private List<MenuEntity> children;

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public Long getParentId() {
        return this.parentId;
    }

    @Override
    public void setChildren(List<MenuEntity> children) {
        this.children = children;
    }

    @Override
    public List<MenuEntity> getChildren() {
        return this.children;
    }
}
