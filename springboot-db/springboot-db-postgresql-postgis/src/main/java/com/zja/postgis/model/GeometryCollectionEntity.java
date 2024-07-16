package com.zja.postgis.model;

import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * GeometryEntity 实体类
 * @author: zhengja
 * @since: 2024/07/15 10:36
 */
@Getter
@Setter
@Entity
@Table(name = "test_geometry_collection")
@EntityListeners(value = AuditingEntityListener.class)
public class GeometryCollectionEntity {

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

    /**
     * 一个图形集合 jts GeometryCollection
     */
    @Column(name = "geometryCollection", columnDefinition = "geometry(GeometryCollection,4326)")
    private GeometryCollection geometryCollection;
}