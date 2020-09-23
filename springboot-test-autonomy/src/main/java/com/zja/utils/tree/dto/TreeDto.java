package com.zja.utils.tree.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/7/18 14:45
 */
@Data
public class TreeDto implements Serializable{

    /**
     * 文件名
     */
    private String label;

    /**
     * 文件全路径
     */
    private String fullPath;

    /**
     * 子文件
     */
    private List<TreeDto> childrens;

    /**
     * 类型:目录/文件（可以使用枚举）
     */
    private String type;
}
