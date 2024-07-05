package com.zja.jts;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;

/**
 * 缓冲区分析是GIS中常见的空间分析方法，用于创建给定几何对象周围一定宽度的区域。
 * <p>
 * 请注意，缓冲区的单位取决于你的地理坐标系。如果使用投影坐标系（如UTM），缓冲区距离可以直接对应到真实世界距离（如米、千米）。而在地理坐标系（如WGS84）中，由于经度和纬度之间的距离随纬度变化而变化，缓冲区可能会出现变形，此时可能需要先将数据投影到合适的平面坐标系再进行缓冲区分析。
 *
 * @Author: zhengja
 * @Date: 2024-07-05 9:49
 */
public class BufferAnalysisTest {

    /**
     * 创建一个点对象的缓冲区（圆形区域)
     * <p>
     * 创建了一个位于原点(0, 0)的点对象，然后设置了缓冲区半径为5.0（假设单位为公里），并使用buffer()方法生成了该点周围的缓冲区（圆形区域）。
     */
    @Test
    public void testBufferAnalysis() {
        // 创建GeometryFactory对象
        GeometryFactory factory = new GeometryFactory();

        // 创建一个点对象，作为缓冲区分析的中心点
        Coordinate centerPoint = new Coordinate(0, 0);
        Geometry point = factory.createPoint(centerPoint);

        // 设置缓冲区半径，单位取决于你的坐标系，默认情况下JTS使用的是度量单位（如米、千米等）
        double bufferDistance = 5.0; // 假设单位为公里

        // 应用缓冲区分析
        Geometry buffer = point.buffer(bufferDistance);

        // 输出缓冲区的WKT表示
        System.out.println("原始点: " + point);
        System.out.println("缓冲区圆: " + buffer);
    }

    /**
     * 缓冲区分析（Buffer）: 创建一个多边形的缓冲区。
     * <p>
     * 缓冲区分析是围绕矢量要素创建一定宽度范围内的区域，常用于影响范围分析、可视性分析等。
     */
    @Test
    public void Buffer_test_6() {
        GeometryFactory factory = new GeometryFactory();

        // 创建一个多边形
        Coordinate[] polyCoords = {new Coordinate(0, 0), new Coordinate(0, 10), new Coordinate(10, 10), new Coordinate(10, 0), new Coordinate(0, 0)};
        Polygon polygon = factory.createPolygon(polyCoords);

        // 创建缓冲区，指定缓冲区半径
        double bufferDistance = 2.0;
        Geometry bufferedPolygon = polygon.buffer(bufferDistance);

        System.out.println("Polygon: " + polygon);
        System.out.println("Buffered Polygon: " + bufferedPolygon);
    }

}
