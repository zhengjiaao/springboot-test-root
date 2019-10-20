//package com.dist.utils.tree.entity;
//
//import com.dist.utils.tree.dto.Tree;
//import lombok.Data;
//
//import javax.persistence.*;
//import java.util.List;
//
///**
// * @author zhengja@dist.com.cn
// * @data 2019/7/18 15:23
// */
//@Table(name = "tree_node")
//@Entity
//@Data
//public class TreeNode implements Tree {
//    /**
//     * 当前节点id
//     */
//    @Id
//    @Column(unique = true, length = 20, nullable = false)
//    private Integer id;
//
//
//    /**
//     * 父节点
//     */
//    @Column(length = 20, nullable = false)
//    private Integer parentId;
//
//    /**
//     * 节点图标
//     */
//    @Column(length = 50)
//    private String icon;
//
//    /**
//     * 节点名称
//     */
//    @Column(length = 50)
//    private String name;
//
//    /**
//     * 子节点
//     */
//    @Transient
//    private List<Tree> childrens;
//
//}
