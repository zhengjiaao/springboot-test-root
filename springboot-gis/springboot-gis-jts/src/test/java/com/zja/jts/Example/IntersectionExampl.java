package com.zja.jts.Example;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;

/**
 * 交集（Intersection）
 *
 * @Author: zhengja
 * @Date: 2024-07-05 9:43
 */
public class IntersectionExampl {
    public static void main(String[] args) {
        GeometryFactory factory = new GeometryFactory();

        // 创建两个多边形
        Coordinate[] polyCoords1 = { /* 多边形1的坐标 */};
        Polygon polygon1 = factory.createPolygon(polyCoords1);

        Coordinate[] polyCoords2 = { /* 多边形2的坐标 */};
        Polygon polygon2 = factory.createPolygon(polyCoords2);

        // 计算交集
        Geometry intersection = polygon1.intersection(polygon2);
        System.out.println("Intersection: " + intersection);
    }
}
