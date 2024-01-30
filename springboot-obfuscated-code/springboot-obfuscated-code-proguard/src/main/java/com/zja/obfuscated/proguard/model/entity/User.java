package com.zja.obfuscated.proguard.model.entity;

import com.zja.obfuscated.proguard.model.entity.base.BaseEntity;
import com.zja.obfuscated.proguard.util.IdGeneratorUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author: zhengja
 * @since: 2024/01/26 15:20
 */
@Getter
@Setter
@Entity
@Table(name = "test_user", uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_sort", columnNames = {"sort"})
})
@EntityListeners(value = AuditingEntityListener.class)
public class User extends BaseEntity {

    /**
     * 名称
     */
    @Column(nullable = false)
    private String name;

    /**
     * 描述
     */
    private String remarks;

    /**
     * 排列顺序
     */
    @Column(nullable = false, unique = true)
    private Long sort = IdGeneratorUtil.nextSortLong();

    /**
     * 创建时间
     */
    @CreatedDate
    private LocalDateTime createTime;

    /**
     * 最后一次修改时间
     */
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
}