package com.zja.jts.Example;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;

/**
 * 对称差（Symmetric Difference）
 *
 * @Author: zhengja
 * @Date: 2024-07-05 9:45
 */
public class SymDifferenceExample {
    public static void main(String[] args) {
        GeometryFactory factory = new GeometryFactory();

        // 创建两个多边形
        Coordinate[] polyCoords1 = { /* 多边形1的坐标 */};
        Polygon polygon1 = factory.createPolygon(polyCoords1);

        Coordinate[] polyCoords2 = { /* 多边形2的坐标 */};
        Polygon polygon2 = factory.createPolygon(polyCoords2);

        // 计算对称差
        Geometry symDifference = polygon1.symDifference(polygon2);
        System.out.println("Symmetric Difference: " + symDifference);
    }
}
