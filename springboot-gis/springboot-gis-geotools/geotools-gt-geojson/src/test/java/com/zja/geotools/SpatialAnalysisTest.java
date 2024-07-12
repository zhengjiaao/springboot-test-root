package com.zja.geotools;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.simplify.TopologyPreservingSimplifier;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.identity.FeatureId;

/**
 * GeoTools 常见空间分析操作
 * <p>
 * 包括几何对象的交集、并集、差集以及缓冲区分析
 *
 * @Author: zhengja
 * @Date: 2024-07-05 14:12
 */
public class SpatialAnalysisTest {

    private static final GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

    private static Polygon createPolygon(int[][] points) {
        Coordinate[] coords = new Coordinate[points.length];
        for (int i = 0; i < points.length; i++) {
            coords[i] = new Coordinate(points[i][0], points[i][1]);
        }
        return geometryFactory.createPolygon(coords);
    }

    /**
     * 几何对象的交集 (Intersection)
     */
    @Test
    public void intersection_test_() {
        // 创建两个多边形
        Polygon polygon1 = createPolygon(new int[][]{{0, 0}, {0, 10}, {10, 10}, {10, 0}, {0, 0}});
        Polygon polygon2 = createPolygon(new int[][]{{5, 5}, {15, 5}, {15, 15}, {5, 15}, {5, 5}});

        // 计算交集
        Geometry intersection = polygon1.intersection(polygon2);
        System.out.println("Intersection: " + intersection);
    }

    /**
     * 几何对象的并集 (Union)
     */
    @Test
    public void union_test_1() {
        Polygon polygon1 = createPolygon(new int[][]{{0, 0}, {0, 10}, {10, 10}, {10, 0}, {0, 0}});
        Polygon polygon2 = createPolygon(new int[][]{{11, 0}, {11, 10}, {20, 10}, {20, 0}, {11, 0}});

        // 计算并集
        Geometry union = polygon1.union(polygon2);
        System.out.println("Union: " + union);
    }

    /**
     * 几何对象的差集 (Difference)
     */
    @Test
    public void test_2() {
        Polygon polygon1 = createPolygon(new int[][]{{0, 0}, {0, 20}, {20, 20}, {20, 0}, {0, 0}});
        Polygon polygon2 = createPolygon(new int[][]{{5, 5}, {15, 5}, {15, 15}, {5, 15}, {5, 5}});

        // 计算差集，即从polygon1中减去polygon2
        Geometry difference = polygon1.difference(polygon2);
        System.out.println("Difference: " + difference);
    }

    /**
     * 距离计算 (Distance)：计算两个几何对象的距离
     */
    @Test
    public void distance_test_4() {
        Point pointA = geometryFactory.createPoint(new Coordinate(0, 0));
        Point pointB = geometryFactory.createPoint(new Coordinate(3, 4));

        // 计算两点间的距离
        double distance = pointA.distance(pointB);
        System.out.println("Distance between points A and B: " + distance);
    }

    /**
     * 几何对象简化 (Simplify): 通过简化算法去除几何对象的冗余顶点
     * <p>
     * 简化操作可以减少几何对象的复杂度，同时尽量保持其总体形状不变，适用于大数据量的优化显示。
     */
    @Test
    public void simplify_test_5() {
        // 创建一个复杂的线段（这里简化示例，用折线代替）
        LineString complexLine = geometryFactory.createLineString(new Coordinate[]{
                new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, 2),
                new Coordinate(1, 2), new Coordinate(2, 2), new Coordinate(3, 2),
                new Coordinate(3, 1), new Coordinate(3, 0)
        });

        // 简化线段，参数决定简化程度
        // 简化线，指定简化容差为0.5（具体值应根据实际应用场景调整）
        double simplifyTolerance = 0.01;
        Geometry simplifiedLine = TopologyPreservingSimplifier.simplify(complexLine, simplifyTolerance);

        System.out.println("Original Line: " + complexLine);
        System.out.println("Simplified Line: " + simplifiedLine);
    }

    /**
     * 基于属性的过滤 (Attribute Filtering): 根据属性条件过滤几何对象
     * <p>
     * 在处理带有属性的几何数据时，经常需要根据属性值来过滤数据。
     */
    @Test
    public void attributeFilter_test_6() {
        // 假设我们有一个FeatureCollection，这里简化处理直接创建一个FeatureId过滤器作为示例
        FilterFactory2 filterFactory = CommonFactoryFinder.getFilterFactory2(null);

        FeatureId fid = filterFactory.featureId("feature1");

        Filter attributeFilter = filterFactory.id(new FeatureId[]{fid});

        System.out.println("Attribute Filter: " + attributeFilter);
    }

}
