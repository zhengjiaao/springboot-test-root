/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-07 13:59
 * @Since:
 */
package com.zja.jts;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;

import static org.junit.Assert.assertEquals;

/**
 * @author: zhengja
 * @since: 2023/11/07 13:59
 */
public class JTSWKTConversionTest2 {

    @Test
    public void convertPointToWKT() {
        Coordinate coordinate = new Coordinate(1, 2);
        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint(coordinate);

        WKTWriter wktWriter = new WKTWriter();
        String wkt = wktWriter.write(point);

        assertEquals("POINT (1 2)", wkt);
    }

    @Test
    public void convertLineStringToWKT() {
        Coordinate[] lineCoordinates = new Coordinate[]{
                new Coordinate(0, 0),
                new Coordinate(1, 1),
                new Coordinate(2, 2)
        };
        GeometryFactory geometryFactory = new GeometryFactory();
        LineString lineString = geometryFactory.createLineString(lineCoordinates);

        WKTWriter wktWriter = new WKTWriter();
        String wkt = wktWriter.write(lineString);

        assertEquals("LINESTRING (0 0, 1 1, 2 2)", wkt);
    }

    @Test
    public void convertPolygonToWKT() {
        Coordinate[] polygonCoordinates = new Coordinate[]{
                new Coordinate(0, 0),
                new Coordinate(0, 3),
                new Coordinate(4, 3),
                new Coordinate(4, 0),
                new Coordinate(0, 0)
        };
        GeometryFactory geometryFactory = new GeometryFactory();
        LinearRing shell = geometryFactory.createLinearRing(polygonCoordinates);
        Polygon polygon = geometryFactory.createPolygon(shell);

        WKTWriter wktWriter = new WKTWriter();
        String wkt = wktWriter.write(polygon);

        assertEquals("POLYGON ((0 0, 0 3, 4 3, 4 0, 0 0))", wkt);
    }

    @Test
    public void convertMultiPolygonToWKT() {
        Coordinate[][] multiPolygonCoordinates = new Coordinate[][]{
                new Coordinate[]{
                        new Coordinate(0, 0),
                        new Coordinate(0, 3),
                        new Coordinate(4, 3),
                        new Coordinate(4, 0),
                        new Coordinate(0, 0)
                },
                new Coordinate[]{
                        new Coordinate(2, 2),
                        new Coordinate(2, 4),
                        new Coordinate(6, 4),
                        new Coordinate(6, 2),
                        new Coordinate(2, 2)
                }
        };
        GeometryFactory geometryFactory = new GeometryFactory();
        Polygon[] polygons = new Polygon[multiPolygonCoordinates.length];
        for (int i = 0; i < multiPolygonCoordinates.length; i++) {
            LinearRing shell = geometryFactory.createLinearRing(multiPolygonCoordinates[i]);
            polygons[i] = geometryFactory.createPolygon(shell);
        }
        MultiPolygon multiPolygon = geometryFactory.createMultiPolygon(polygons);

        WKTWriter wktWriter = new WKTWriter();
        String wkt = wktWriter.write(multiPolygon);

        assertEquals("MULTIPOLYGON (((0 0, 0 3, 4 3, 4 0, 0 0)), ((2 2, 2 4, 6 4, 6 2, 2 2)))", wkt);
    }

    @Test
    public void convertRectangleToWKT() {
        Envelope envelope = new Envelope(0, 4, 0, 3);
        GeometryFactory geometryFactory = new GeometryFactory();
        Polygon rectangle = (Polygon) geometryFactory.toGeometry(envelope);

        // 创建 WKTWriter
        WKTWriter wktWriter = new WKTWriter();
        // 将矩形转换为 WKT
        String wkt = wktWriter.write(rectangle);

        assertEquals("POLYGON ((0 0, 0 3, 4 3, 4 0, 0 0))", wkt);
    }

    @Test
    public void convertWKTToPoint() throws ParseException {
        String wkt = "POINT (1 2)";

        WKTReader wktReader = new WKTReader();
        Geometry geometry = wktReader.read(wkt);

        assertEquals(Geometry.TYPENAME_POINT, geometry.getGeometryType());
        assertEquals(new Coordinate(1, 2), geometry.getCoordinates()[0]);
    }

    @Test
    public void convertWKTToLineString() throws ParseException {
        String wkt = "LINESTRING (0 0, 1 1, 2 2)";

        WKTReader wktReader = new WKTReader();
        Geometry geometry = wktReader.read(wkt);

        assertEquals(Geometry.TYPENAME_LINESTRING, geometry.getGeometryType());
        assertEquals(3, geometry.getCoordinates().length);
    }

    @Test
    public void convertWKTToPolygon() throws ParseException {
        String wkt = "POLYGON ((0 0, 0 3, 4 3, 4 0, 0 0))";

        WKTReader wktReader = new WKTReader();
        Geometry geometry = wktReader.read(wkt);

        assertEquals(Geometry.TYPENAME_POLYGON, geometry.getGeometryType());
        assertEquals(5, geometry.getCoordinates().length);
    }

    @Test
    public void convertWKTToMultiPolygon() throws ParseException {
        String wkt = "MULTIPOLYGON (((0 0, 0 3, 4 3, 4 0, 0 0)), ((2 2, 2 4, 6 4, 6 2, 2 2)))";

        WKTReader wktReader = new WKTReader();
        Geometry geometry = wktReader.read(wkt);

        assertEquals(Geometry.TYPENAME_MULTIPOLYGON, geometry.getGeometryType());
        assertEquals(2, geometry.getNumGeometries());
    }

    @Test
    public void convertWKTToRectangle() throws ParseException {
        String wkt = "POLYGON ((0 0, 0 3, 4 3, 4 0, 0 0))";

        WKTReader wktReader = new WKTReader();
        Geometry geometry = wktReader.read(wkt);

        assertEquals(Geometry.TYPENAME_POLYGON, geometry.getGeometryType());
        assertEquals(5, geometry.getCoordinates().length);

        Envelope envelope = geometry.getEnvelopeInternal();
        assertEquals(0, envelope.getMinX(), 0);
        assertEquals(4, envelope.getMaxX(), 0);
        assertEquals(0, envelope.getMinY(), 0);
        assertEquals(3, envelope.getMaxY(), 0);
    }
}
