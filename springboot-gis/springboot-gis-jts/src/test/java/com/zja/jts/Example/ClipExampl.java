package com.zja.jts.Example;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;

/**
 * 几何对象的裁剪（Clip）
 *
 * @Author: zhengja
 * @Date: 2024-07-05 9:42
 */
public class ClipExampl {
    /*public static void main(String[] args) {
        GeometryFactory factory = new GeometryFactory();

        // 创建原始多边形
        Coordinate[] originalCoords = new Coordinate[]{
                new Coordinate(0, 0), new Coordinate(0, 20),
                new Coordinate(20, 20), new Coordinate(20, 0), new Coordinate(0, 0)
        };
        Polygon originalPolygon = factory.createPolygon(originalCoords);

        // 创建裁剪多边形
        Coordinate[] clipCoords = new Coordinate[]{
                new Coordinate(5, 5), new Coordinate(15, 5),
                new Coordinate(15, 15), new Coordinate(5, 15), new Coordinate(5, 5)
        };
        Polygon clipPolygon = factory.createPolygon(clipCoords);

        // 裁剪操作
        Geometry clipped = OverlayNG.clip(originalPolygon, clipPolygon);

        System.out.println("Clipped Polygon: " + clipped);
    }*/

    public static void main(String[] args) {
        GeometryFactory factory = new GeometryFactory();

        // 创建原始多边形和裁剪多边形
        Coordinate[] originalCoords = {  /*原始多边形的坐标*/  };
        Polygon originalPolygon = factory.createPolygon(originalCoords);

        Coordinate[] clipCoords = {  /*裁剪多边形的坐标*/  };
        Polygon clipPolygon = factory.createPolygon(clipCoords);

        // 裁剪操作（实际上可以使用差集操作实现）
        Geometry clipped = originalPolygon.difference(clipPolygon);
        System.out.println("Clipped: " + clipped);
    }
}
