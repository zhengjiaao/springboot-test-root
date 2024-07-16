package com.zja.postgis.model;

import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.LineString;
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
@Table(name = "test_linestring")
@EntityListeners(value = AuditingEntityListener.class)
public class LineStringEntity {

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

    // jts LineString
    @Column(columnDefinition = "geometry(LineString,4326)")
    private LineString lineString;
}