package com.zja.jts.Example;

import org.locationtech.jts.geom.*;
import org.locationtech.jts.simplify.TopologyPreservingSimplifier;

/**
 * 几何简化
 * <p>
 * 简化复杂几何对象，通常用于减少数据量，提高效率，同时尽量保持原有的空间特征。
 *
 * @Author: zhengja
 * @Date: 2024-07-05 9:42
 */
public class SimplificationExample {
    /*public static void main(String[] args) {
        // 假设polygon是一个复杂的多边形
        Polygon complexPolygon = ...;

        // 简化因子，越小简化程度越低
        double simplifyFactor = 0.1;

        // 进行简化
        Polygon simplifiedPolygon = (Polygon) TopologyPreservingSimplifier.simplify(complexPolygon, simplifyFactor);

        System.out.println("Original Coordinates Count: " + complexPolygon.getNumPoints());
        System.out.println("Simplified Coordinates Count: " + simplifiedPolygon.getNumPoints());
    }*/

    public static void main(String[] args) {
        GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 8307);

        // 创建一条复杂的线
        Coordinate[] lineCoords = { /* 复杂线的坐标 */};
        LineString line = factory.createLineString(lineCoords);

        // 简化线，指定简化容差
        double simplifyTolerance = 0.5;
        Geometry simplifiedLine = TopologyPreservingSimplifier.simplify(line, simplifyTolerance);

        System.out.println("Simplified Line: " + simplifiedLine);
    }
}
