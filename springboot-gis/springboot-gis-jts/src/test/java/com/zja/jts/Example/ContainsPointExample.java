package com.zja.jts.Example;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

/**
 * 点在多边形内判断（Contains）
 *
 * @Author: zhengja
 * @Date: 2024-07-05 9:47
 */
public class ContainsPointExample {

    // 判断一个点是否位于某个多边形内部，这是空间查询中的常见需求。
    public static void main(String[] args) {
        GeometryFactory factory = new GeometryFactory();

        // 创建一个多边形
        Coordinate[] polyCoords = {
                new Coordinate(0, 0), new Coordinate(0, 10),
                new Coordinate(10, 10), new Coordinate(10, 0), new Coordinate(0, 0)
        };
        Polygon polygon = factory.createPolygon(polyCoords);

        // 创建一个点
        Coordinate pointCoord = new Coordinate(5, 5);
        Point point = factory.createPoint(pointCoord);

        // 判断点是否在多边形内
        boolean isInside = polygon.contains(point);
        System.out.println("Is the point inside the polygon? " + isInside);
    }
}
