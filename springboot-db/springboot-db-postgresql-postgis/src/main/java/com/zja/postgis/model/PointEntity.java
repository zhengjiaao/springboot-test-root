package com.zja.postgis.model;

import lombok.*;
import org.locationtech.jts.geom.Point;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * PointEntity 实体类
 * @author: zhengja
 * @since: 2024/07/15 9:50
 */
@Getter
@Setter
@Entity
@Table(name = "test_point")
@EntityListeners(value = AuditingEntityListener.class)
public class PointEntity {

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

    // jts Point
    @Column(columnDefinition = "geometry(Point,4326)") // 默认：geometry(Point,4326)，必须是 Point SRID 4326
    private Point point;

    @Column(name = "point2", columnDefinition = "geometry") // 默认：geometry，SRID 不设置默认是 -1，可以指定任意的SRID
    private Point point2;
}