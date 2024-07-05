package com.zja.jts.Example;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;

/**
 * 距离计算
 *
 * @Author: zhengja
 * @Date: 2024-07-05 9:42
 */
public class DistanceExample {
    /*public static void main(String[] args) {
        Geometry geom1 = ...; // 第一个几何对象
        Geometry geom2 = ...; // 第二个几何对象

        double distance = geom1.distance(geom2);
        System.out.println("Distance between two geometries: " + distance);
    }*/

    public static void main(String[] args) {
        GeometryFactory factory = new GeometryFactory();

        // 创建两个多边形
        Coordinate[] polyCoords1 = { /* 多边形1的坐标 */ };
        Polygon polygon1 = factory.createPolygon(polyCoords1);

        Coordinate[] polyCoords2 = { /* 多边形2的坐标 */ };
        Polygon polygon2 = factory.createPolygon(polyCoords2);

        // 计算差集
        Geometry difference = polygon1.difference(polygon2);
        System.out.println("Difference: " + difference);
    }
}
