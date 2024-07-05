package com.zja.jts.Example;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;

/**
 * 几何对象的联合与相交分析（Union & Intersection）
 *
 * @Author: zhengja
 * @Date: 2024-07-05 9:43
 */
public class UnionIntersectionExample {

    public static void main(String[] args) {
        GeometryFactory factory = new GeometryFactory();

        // 创建两个多边形
        Coordinate[] polyCoords1 = new Coordinate[]{
                new Coordinate(0, 0), new Coordinate(0, 10),
                new Coordinate(10, 10), new Coordinate(10, 0), new Coordinate(0, 0)
        };
        Polygon polygon1 = factory.createPolygon(polyCoords1);

        Coordinate[] polyCoords2 = new Coordinate[]{
                new Coordinate(5, 5), new Coordinate(15, 5),
                new Coordinate(15, 15), new Coordinate(5, 15), new Coordinate(5, 5)
        };
        Polygon polygon2 = factory.createPolygon(polyCoords2);

        // 联合操作
        Geometry unionResult = polygon1.union(polygon2);
        System.out.println("Union Result: " + unionResult);

        // 相交操作
        Geometry intersectionResult = polygon1.intersection(polygon2);
        System.out.println("Intersection Result: " + intersectionResult);
    }
}
