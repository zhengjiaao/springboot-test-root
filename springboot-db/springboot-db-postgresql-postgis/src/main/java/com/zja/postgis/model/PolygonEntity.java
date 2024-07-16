package com.zja.postgis.model;

import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
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
@Table(name = "test_polygon")
@EntityListeners(value = AuditingEntityListener.class)
public class PolygonEntity {

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
     * 多边形(面) jts Polygon
     */
    @Column(name = "polygon", columnDefinition = "geometry(Polygon,4326)")
    private Polygon polygon;
}