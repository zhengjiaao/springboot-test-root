/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-07 13:11
 * @Since:
 */
package com.zja.geotools;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.geotools.geojson.geom.GeometryJSON;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * @author: zhengja
 * @since: 2023/10/07 13:11
 */
public class GeoJSONUtil {

    /**
     * 判断是否在面上
     *
     * @param x
     * @param y
     * @param geojson
     * @return boolean
     */
    public static boolean surface(double x, double y, String geojson) {
        try {
            String wktStr = geojsonToWkt(geojson);
            Point wktPoint = new GeometryFactory().createPoint(new Coordinate(x, y));
            return contains(wktPoint, wktStr);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断是否在面上
     *
     * @param wktPoint
     * @param wktMapStr
     * @return boolean
     */
    private static boolean contains(Point wktPoint, String wktMapStr) {
        //从WKT字符串读取几何图形
        Geometry g = null;
        try {
            //读取面
            g = new WKTReader().read(wktMapStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        //计算点是否在面内
        return g.contains(wktPoint);
    }

    /**
     * geojson 转 geometry
     *
     * @return geometry
     */
    public static Geometry geojsonToGeometry(String geojson) throws IOException {
        GeometryJSON gjson = new GeometryJSON(7);
        return gjson.read(new StringReader(geojson));
    }

    /**
     * geometry 转 geojson
     *
     * @return geojson
     */
    public static String geometryToGeojson(Geometry geometry) throws IOException {
        GeometryJSON gjson = new GeometryJSON(7);
        StringWriter writer = new StringWriter();
        gjson.write(geometry, writer);
        return writer.toString();
    }

    /**
     * geojson 转 wtk
     *
     * @return wtk
     */
    public static String geojsonToWkt(String geojson) throws Exception {
        Geometry geometry = geojsonToGeometry(geojson);
        return geometryToWkt(geometry);
    }

    /**
     * wkt 转 geojson
     *
     * @return geojson
     */
    public static String wktToGeojson(String wkt) throws Exception {
        Geometry geometry = wktToGeometry(wkt);
        return geometryToGeojson(geometry);
    }

    /**
     * wtk 转 geometry
     *
     * @return geometry
     */
    public static Geometry wktToGeometry(String wkt) throws ParseException {
        WKTReader reader = new WKTReader();
        return reader.read(wkt);
    }

    /**
     * geometry 转 wtk
     *
     * @return wkt
     */
    public static String geometryToWkt(Geometry geometry) {
        WKTWriter writer = new WKTWriter();
        return writer.write(geometry);
    }

    /**
     * obj 转 String
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String objToStringPretty(T obj) {
        ObjectMapper mapper = new ObjectMapper();
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * string 转 object
     *
     * @param str
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T stringToObj(String str, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        if (StringUtils.isEmpty(str) || clazz == null) {
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T) str : mapper.readValue(str, clazz);
        } catch (IOException e) {
            return null;
        }
    }
}
