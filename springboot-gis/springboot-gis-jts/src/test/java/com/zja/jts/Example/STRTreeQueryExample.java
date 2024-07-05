package com.zja.jts.Example;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.index.strtree.STRtree;

import java.util.List;

/**
 * 空间索引查询（STRtree）
 * <p>
 * 使用空间索引可以高效地进行空间查询，如查找某个点周围最近的设施。
 *
 * @Author: zhengja
 * @Date: 2024-07-05 9:48
 */
public class STRTreeQueryExample {
    public static void main(String[] args) {
        GeometryFactory factory = new GeometryFactory();

        // 创建点集合，模拟设施位置
        STRtree index = new STRtree();
        Coordinate[] facilitiesCoords = { /* 设施坐标数组 */};
        for (Coordinate coord : facilitiesCoords) {
            Point facility = factory.createPoint(coord);
            index.insert(facility.getEnvelopeInternal(), facility);
        }

        // 查询点
        Coordinate queryPointCoord = new Coordinate(5, 5);
        Point queryPoint = factory.createPoint(queryPointCoord);

        // 执行查询
        List<Geometry> nearestFacilities = index.query(queryPoint.getEnvelopeInternal());

        System.out.println("Nearest Facilities to Query Point:");
        for (Geometry facility : nearestFacilities) {
            System.out.println(facility);
        }
    }

   /* public static void main(String[] args) {
        STRtree tree = new STRtree();

        // 假设我们有一系列多边形
        Geometry[] polygons = { *//* 初始化你的多边形数组 *//*};

        // 将多边形加入到空间索引中
        for (Geometry poly : polygons) {
            tree.insert(poly.getEnvelopeInternal(), poly);
        }

        // 查询一个点是否在任何多边形内
        Coordinate queryPoint = new Coordinate(5, 5);
        Geometry queryGeom = factory.createPoint(queryPoint);
        List<Geometry> results = tree.query(queryGeom.getEnvelopeInternal());

        System.out.println("Polygons containing the query point: " + results.size());
    }*/
}
