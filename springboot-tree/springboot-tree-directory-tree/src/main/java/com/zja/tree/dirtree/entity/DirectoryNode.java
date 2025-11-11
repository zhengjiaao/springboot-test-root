package com.zja.tree.dirtree.entity;

import com.zja.tree.dirtree.entity.enums.NodeType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DirectoryNode 实体类
 *
 * @author: zhengja
 * @since: 2025/11/06 10:34
 */
@Getter
@Setter
@Entity
// @Table(name = "")
@Table(name = "directory_node", indexes = {
        @Index(name = "idx_business_type", columnList = "business_type"),
        @Index(name = "idx_parent_id", columnList = "parent_id"),
        @Index(name = "idx_business_type_business_id", columnList = "business_type,business_id")
})
@EntityListeners(value = AuditingEntityListener.class)
// public class DirectoryNode extends BaseEntity {
public class DirectoryNode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 树形结构字段 - 父ID默认为-1表示根节点
    @Column(name = "parent_id", nullable = false)
    private Long parentId = -1L;

    @Transient
    private List<DirectoryNode> children = new ArrayList<>();

    // 目录元信息
    @Column(nullable = false)
    private String name;
    private String alias;
    private String description;
    private String icon;
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;
    /**
     * 排列顺序
     */
    // @Column(name = "sort", nullable = false, unique = true)
    // private Long sort = SortGenerator.next();

    // 业务关联字段
    @Column(name = "business_type", nullable = false)
    private String businessType;

    @Column(name = "business_id")
    private String businessId;

    // 节点类型
    @Enumerated(EnumType.STRING)
    @Column(name = "node_type", nullable = false)
    private NodeType nodeType;

    // 扩展字段
    private Integer depth = 0;
    @Column(name = "full_path")
    private String fullPath;

    // 自定义属性
    @ElementCollection
    @CollectionTable(name = "directory_node_attributes",
            joinColumns = @JoinColumn(name = "node_id"))
    @MapKeyColumn(name = "attr_key")
    @Column(name = "attr_value")
    private Map<String, String> customAttributes = new HashMap<>();

    /**
     * 备注
     */
    private String remarks;

    // 审计字段

    /**
     * 创建时间
     */
    @Column(name = "create_time", nullable = false)
    @CreatedDate
    private LocalDateTime createTime;

    /**
     * 最后一次修改时间
     */
    @Column(name = "last_modified_date")
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    /**
     * 创建人
     */
    @Column(name = "created_by")
    private String createdBy;

    /**
     * 最后一次修改人
     */
    @Column(name = "updated_by")
    private String updatedBy;

    // 工具方法
    public void addCustomAttribute(String key, String value) {
        if (this.customAttributes == null) {
            this.customAttributes = new HashMap<>();
        }
        this.customAttributes.put(key, value);
    }

    public boolean isRoot() {
        return parentId != null && parentId.equals(-1L);
    }

    public boolean hasParent() {
        return parentId != null && !parentId.equals(-1L);
    }
}