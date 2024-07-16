package com.zja.postgis.model;

import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.UUID;

/**
 * GeometryEntity 实体类
 * @author: zhengja
 * @since: 2024/07/15 10:36
 */
@Getter
@Setter
@Entity
@Table(name = "test_multi_polygon")
@EntityListeners(value = AuditingEntityListener.class)
public class MultiPolygonEntity {

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
     * 多边形(面) jts MultiPolygon
     */
    @Column(name = "multiPolygon", columnDefinition = "geometry(MultiPolygon,4326)")
    private MultiPolygon multiPolygon;
}