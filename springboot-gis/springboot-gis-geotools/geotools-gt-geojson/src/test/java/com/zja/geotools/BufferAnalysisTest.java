package com.zja.geotools;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

/**
 * @Author: zhengja
 * @Date: 2024-07-05 14:17
 */
public class BufferAnalysisTest {

    private static final GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

    /**
     * 缓冲区分析 (Buffer)
     */
    @Test
    public void test_1() {
        Point point = geometryFactory.createPoint(new Coordinate(0, 0));

        // 创建点的缓冲区，半径为5
        Geometry buffer = point.buffer(5);
        System.out.println("point: " + point);
        System.out.println("Buffer point: " + buffer);
    }
}
