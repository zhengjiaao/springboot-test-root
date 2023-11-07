/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-07 15:58
 * @Since:
 */
package com.zja.geotools;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.*;

/**
 * @author: zhengja
 * @since: 2023/11/07 15:58
 */
public class GeometryUnitTest {

    public static void main(String[] args) {
        // 创建点
        Point point = createPoint(30, 10);
        System.out.println("Point: " + point); //POINT (30 10)

        // 创建线
        LineString lineString = createLineString(new Coordinate(30, 10), new Coordinate(40, 20), new Coordinate(50, 30));
        System.out.println("LineString: " + lineString); //LINESTRING (30 10, 40 20, 50 30)

        // 创建面
        Polygon polygon = createPolygon(new Coordinate(30, 10), new Coordinate(40, 40), new Coordinate(20, 40), new Coordinate(10, 20), new Coordinate(30, 10));
        System.out.println("Polygon: " + polygon); //POLYGON ((30 10, 40 40, 20 40, 10 20, 30 10))

        // 创建多面
        Polygon[] polygons = createMultiPolygon(
                new Coordinate[]{new Coordinate(30, 10), new Coordinate(40, 40), new Coordinate(20, 40), new Coordinate(10, 20), new Coordinate(30, 10)},
                new Coordinate[]{new Coordinate(60, 20), new Coordinate(70, 50), new Coordinate(50, 60), new Coordinate(40, 30), new Coordinate(60, 20)}
        );
        System.out.println("MultiPolygon: ");
        for (Polygon poly : polygons) {
            System.out.println(poly);
            //POLYGON ((30 10, 40 40, 20 40, 10 20, 30 10))
            //POLYGON ((60 20, 70 50, 50 60, 40 30, 60 20))
        }

        // 创建矩形边界
        Envelope envelope = createEnvelope(10, 20, 40, 50);
        System.out.println("Envelope: " + envelope); //Env[10.0 : 40.0, 20.0 : 50.0]
    }

    private static Point createPoint(double x, double y) {
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        return geometryFactory.createPoint(new Coordinate(x, y));
    }

    private static LineString createLineString(Coordinate... coordinates) {
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        return geometryFactory.createLineString(coordinates);
    }

    private static Polygon createPolygon(Coordinate... coordinates) {
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        LinearRing shell = geometryFactory.createLinearRing(coordinates);
        return geometryFactory.createPolygon(shell);
    }

    private static Polygon[] createMultiPolygon(Coordinate[] coordinates1, Coordinate[] coordinates2) {
        Polygon polygon1 = createPolygon(coordinates1);
        Polygon polygon2 = createPolygon(coordinates2);
        return new Polygon[]{polygon1, polygon2};
    }

    private static Envelope createEnvelope(double minX, double minY, double maxX, double maxY) {
        return new Envelope(minX, maxX, minY, maxY);
    }
}
