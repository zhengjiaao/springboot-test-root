package com.zja.jts.Example;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;

/**
 * 几何对象的差异分析（Difference）
 *
 * @Author: zhengja
 * @Date: 2024-07-05 9:43
 */
public class DifferenceExample {
    public static void main(String[] args) {
        GeometryFactory factory = new GeometryFactory();

        // 定义两个多边形
        Coordinate[] polyCoords1 = new Coordinate[]{
                new Coordinate(0, 0), new Coordinate(0, 20),
                new Coordinate(20, 20), new Coordinate(20, 0), new Coordinate(0, 0)
        };
        Polygon polygon1 = factory.createPolygon(polyCoords1);

        Coordinate[] polyCoords2 = new Coordinate[]{
                new Coordinate(5, 5), new Coordinate(15, 5),
                new Coordinate(15, 15), new Coordinate(5, 15), new Coordinate(5, 5)
        };
        Polygon polygon2 = factory.createPolygon(polyCoords2);

        // 差异分析
        Geometry differenceResult = polygon1.difference(polygon2);
        System.out.println("Difference Result: " + differenceResult);
    }
}
