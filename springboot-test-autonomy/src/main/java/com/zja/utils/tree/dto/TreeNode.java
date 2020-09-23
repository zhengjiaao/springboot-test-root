package com.zja.utils.tree.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/7/18 15:00
 */
@Data
public class TreeNode implements Serializable{
    private String id;
    private String name;
    private List<TreeNode> childrends;

    public TreeNode(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
