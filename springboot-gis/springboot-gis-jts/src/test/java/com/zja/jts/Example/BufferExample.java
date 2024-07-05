package com.zja.jts.Example;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;

/**
 * 缓冲区分析（Buffer）
 *
 * @Author: zhengja
 * @Date: 2024-07-05 9:41
 */
public class BufferExample {
    /*public static void main(String[] args) {
        GeometryFactory factory = new GeometryFactory();

        // 创建一个多边形
        Coordinate[] polyCoords = new Coordinate[]{new Coordinate(0, 0), new Coordinate(0, 10), new Coordinate(10, 10), new Coordinate(10, 0), new Coordinate(0, 0)};
        Polygon polygon = factory.createPolygon(polyCoords);

        // 创建缓冲区
        double bufferDistance = 2.0; // 缓冲区半径
        Geometry bufferedPolygon = polygon.buffer(bufferDistance);

        System.out.println("Buffered Polygon: " + bufferedPolygon);
    }*/

    public static void main(String[] args) {
        GeometryFactory factory = new GeometryFactory();

        // 创建一个多边形
        Coordinate[] polyCoords = {
                new Coordinate(0, 0), new Coordinate(0, 10),
                new Coordinate(10, 10), new Coordinate(10, 0), new Coordinate(0, 0)
        };
        Polygon polygon = factory.createPolygon(polyCoords);

        // 创建缓冲区，指定缓冲区半径
        double bufferDistance = 2.0;
        Geometry bufferedPolygon = polygon.buffer(bufferDistance);

        System.out.println("Buffered Polygon: " + bufferedPolygon);
    }
}
