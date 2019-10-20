package com.dist.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.List;

/**
 * 文件夹结构解析
 *
 * @date 2019/4/4
 */
public class FolderStructure implements Serializable {

    @Id
    private String guid;

    private String name;

    private Boolean beDir;   // 是否是目录

    @JsonIgnore
    private String path;     // 资源路径

    private Boolean expand; // 是否展开

    @JsonIgnore
    private Long depth;   // 层级深度

    private List<FolderStructure> children;     // 下级资源

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<FolderStructure> getChildren() {
        return children;
    }

    public void setChildren(List<FolderStructure> children) {
        this.children = children;
    }

    public Boolean getBeDir() {
        return beDir;
    }

    public void setBeDir(Boolean beDir) {
        this.beDir = beDir;
    }

    public Long getDepth() {
        return depth;
    }

    public void setDepth(Long depth) {
        this.depth = depth;
    }

    public Boolean getExpand() {
        return expand;
    }

    public void setExpand(Boolean expand) {
        this.expand = expand;
    }
}
