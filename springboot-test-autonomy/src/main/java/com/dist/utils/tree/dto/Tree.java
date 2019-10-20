package com.dist.utils.tree.dto;

import java.util.List;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/7/18 15:19
 */
public interface Tree {
    /** 当前节点id*/
    Integer id = 0;

    /** 父节点id*/
    Integer parentId = 0;

    /** 子节点集合*/
    List<Tree> childrens = null;

    Integer getId();

    void setId(Integer id);

    Integer getParentId();

    void setParentId(Integer id);

    List<Tree> getChildrens();

    void setChildrens(List<Tree> childrens);
}
