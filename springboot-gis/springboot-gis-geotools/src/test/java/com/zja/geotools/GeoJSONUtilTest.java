/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-07 13:25
 * @Since:
 */
package com.zja.geotools;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author: zhengja
 * @since: 2023/10/07 13:25
 */
public class GeoJSONUtilTest {

    @Test
    public void testSurface() {
        double x = 1.0;
        double y = 2.0;
        String geojson = "{\"type\":\"Point\",\"coordinates\":[1.0,2.0]}";
        boolean result = GeoJSONUtil.surface(x, y, geojson);
        assertTrue(result); // true 该位置在面上
    }

    @Test
    public void testGeojsonToGeometry() throws IOException {
        String geojson = "{\"type\":\"Point\",\"coordinates\":[1.0,2.0]}";
        Geometry geometry = GeoJSONUtil.geojsonToGeometry(geojson);
        assertNotNull(geometry);
        System.out.println(geometry); //POINT (1 2)
    }

    @Test
    public void testGeometryToGeojson() throws IOException {
        Point point = new GeometryFactory().createPoint(new Coordinate(1.0, 2.0));
        String geojson = GeoJSONUtil.geometryToGeojson(point);
        assertNotNull(geojson);
        System.out.println(geojson); //{"type":"Point","coordinates":[1,2]}
    }

    @Test
    public void testGeojsonToWkt() throws Exception {
        String geojson = "{\"type\":\"Point\",\"coordinates\":[1.0,2.0]}";
        String wkt = GeoJSONUtil.geojsonToWkt(geojson);
        assertNotNull(wkt);
        System.out.println(wkt); //POINT (1 2)
    }

    @Test
    public void testWktToGeojson() throws Exception {
        String wkt = "POINT (1.0 2.0)";
        String geojson = GeoJSONUtil.wktToGeojson(wkt);
        assertNotNull(geojson);
        System.out.println(geojson); //{"type":"Point","coordinates":[1,2]}
    }

    @Test
    public void testWktToGeometry() throws ParseException {
        String wkt = "POINT (1.0 2.0)";
        Geometry geometry = GeoJSONUtil.wktToGeometry(wkt);
        assertNotNull(geometry);
        System.out.println(geometry); //POINT (1 2)
    }

    @Test
    public void testGeometryToWkt() {
        Point point = new GeometryFactory().createPoint(new Coordinate(1.0, 2.0));
        String wkt = GeoJSONUtil.geometryToWkt(point);
        assertNotNull(wkt);
        System.out.println(wkt); //POINT (1 2)
    }

    @Test
    public void testObjToStringPretty() {
        // 测试objToStringPretty方法
        String obj = "Hello, World!";
        String result = GeoJSONUtil.objToStringPretty(obj);
        assertNotNull(result);
        System.out.println(result);
    }

    @Test
    public void testStringToObj() {
        // 测试stringToObj方法
        String str = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";
        Person person = GeoJSONUtil.stringToObj(str, Person.class);
        assertNotNull(person);
        System.out.println(person); //Person(name=John, age=30, city=New York)
    }

    @Data
    static class Person {
        private String name;
        private int age;
        private String city;
    }
}
