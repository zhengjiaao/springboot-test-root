package com.zja.postgis.model;

import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.UUID;

/**
 * PointEntity 实体类
 * @author: zhengja
 * @since: 2024/07/15 9:50
 */
@Getter
@Setter
@Entity
@Table(name = "test_multi_point")
@EntityListeners(value = AuditingEntityListener.class)
public class MultiPointEntity {

    @Id
    private String id = UUID.randomUUID().toString();

    /**
     * 名称
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * 类型
     */
    @Column(name = "type")
    private String type;

    // jts MultiPoint
    @Column(columnDefinition = "geometry(MultiPoint,4326)")
    private MultiPoint multiPoint;
}