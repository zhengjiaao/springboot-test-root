/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-07 14:14
 * @Since:
 */
package com.zja.jts;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;

/**
 * @author: zhengja
 * @since: 2023/11/07 14:14
 */
public class JTSWKTConversionTest {

    /**
     * 测试几何对象转换
     */
    @Test
    public void GeometryConversion_test() {
        // 创建点
        Point point = createPoint(10, 20);
        testGeometryConversion(point);

        // 创建线
        LineString lineString = createLineString(new Coordinate(0, 0), new Coordinate(10, 10), new Coordinate(20, 0));
        testGeometryConversion(lineString);

        // 创建面
        Polygon polygon = createPolygon(new Coordinate(0, 0), new Coordinate(0, 10), new Coordinate(10, 10), new Coordinate(10, 0), new Coordinate(0, 0));
        testGeometryConversion(polygon);

        // 创建多面
        Polygon hole = createPolygon(new Coordinate(2, 2), new Coordinate(2, 8), new Coordinate(8, 8), new Coordinate(8, 2), new Coordinate(2, 2));
        MultiPolygon multiPolygon = createMultiPolygon(polygon, hole);
        testGeometryConversion(multiPolygon);

        // 创建多边形
        Polygon simplePolygon = createPolygon(new Coordinate(0, 0), new Coordinate(0, 10), new Coordinate(10, 10), new Coordinate(10, 0), new Coordinate(0, 0));
        testGeometryConversion(simplePolygon);

        // 创建矩形边界
        Polygon envelopePolygon = createPolygon(new Coordinate(0, 0), new Coordinate(0, 10), new Coordinate(10, 10), new Coordinate(10, 0), new Coordinate(0, 0));
        Envelope envelope = new Envelope(0, 10, 0, 10);
        testGeometryConversion(envelopePolygon, envelope);
    }

    // 创建点
    private static Point createPoint(double x, double y) {
        GeometryFactory geometryFactory = new GeometryFactory();
        return geometryFactory.createPoint(new Coordinate(x, y));
    }

    // 创建线
    private static LineString createLineString(Coordinate... coordinates) {
        GeometryFactory geometryFactory = new GeometryFactory();
        return geometryFactory.createLineString(coordinates);
    }

    // 创建面
    private static Polygon createPolygon(Coordinate... coordinates) {
        GeometryFactory geometryFactory = new GeometryFactory();
        LinearRing shell = geometryFactory.createLinearRing(coordinates);
        return geometryFactory.createPolygon(shell);
    }

    // 创建多面
    private static MultiPolygon createMultiPolygon(Polygon... polygons) {
        GeometryFactory geometryFactory = new GeometryFactory();
        return geometryFactory.createMultiPolygon(polygons);
    }

    // 测试几何对象和 WKT 的互相转换
    private static void testGeometryConversion(Geometry geometry) {
        // 将 Geometry 对象转换为 WKT
        String wkt = GeometryUtil.geometryToWKT(geometry);
        System.out.println("Geometry: " + geometry);
        System.out.println("WKT: " + wkt);

        // 将 WKT 转换为 Geometry 对象
        Geometry convertedGeometry = GeometryUtil.wktToGeometry(wkt);
        System.out.println("Converted Geometry: " + convertedGeometry);
        System.out.println("----------------------------------------");
    }

    // 测试 Envelope 对象的边界框与 WKT 的转换
    private static void testGeometryConversion(Polygon polygon, Envelope envelope) {
        // 将 Polygon 对象转换为 WKT
        String polygonWKT = GeometryUtil.geometryToWKT(polygon);
        System.out.println("Polygon: " + polygon);
        System.out.println("Polygon WKT: " + polygonWKT);

        // 将 Envelope 对象转换为 WKT
        String envelopeWKT = envelopeToWKT(envelope);
        System.out.println("Envelope: " + envelope);
        System.out.println("Envelope WKT: " + envelopeWKT);

        // 将 Envelope 的 WKT 转换为 Polygon 对象
        Geometry convertedGeometry = GeometryUtil.wktToGeometry(envelopeWKT);
        System.out.println("Converted Geometry: " + convertedGeometry);
        System.out.println("----------------------------------------");
    }

    private static String envelopeToWKT(Envelope envelope) {
        StringBuilder sb = new StringBuilder();
        sb.append("POLYGON ((");
        sb.append(envelope.getMinX()).append(" ").append(envelope.getMinY()).append(", ");
        sb.append(envelope.getMinX()).append(" ").append(envelope.getMaxY()).append(", ");
        sb.append(envelope.getMaxX()).append(" ").append(envelope.getMaxY()).append(", ");
        sb.append(envelope.getMaxX()).append(" ").append(envelope.getMinY()).append(", ");
        sb.append(envelope.getMinX()).append(" ").append(envelope.getMinY());
        sb.append("))");
        return sb.toString();
    }

}
