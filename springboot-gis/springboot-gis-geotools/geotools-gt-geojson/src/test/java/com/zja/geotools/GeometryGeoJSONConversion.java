/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-07 14:44
 * @Since:
 */
package com.zja.geotools;

import org.geotools.geojson.geom.GeometryJSON;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import java.io.IOException;
import java.io.StringWriter;

/**
 * @author: zhengja
 * @since: 2023/11/07 14:44
 */
public class GeometryGeoJSONConversion {

    public static void main(String[] args) {
        // 创建一个几何对象（以点为例）
        String wkt = "POINT (30 10)";
        Geometry geometry = parseWKT(wkt);

        // 将几何对象转换为 GeoJSON
        String geoJson = convertToGeoJSON(geometry);
        System.out.println("Geometry to GeoJSON:");
        System.out.println(geoJson);

        // 将 GeoJSON 转换为几何对象
        Geometry convertedGeometry = convertToGeometry(geoJson);
        System.out.println("GeoJSON to Geometry:");
        System.out.println(convertedGeometry);
    }

    private static Geometry parseWKT(String wkt) {
        WKTReader reader = new WKTReader();
        try {
            return reader.read(wkt);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String convertToGeoJSON(Geometry geometry) {
        GeometryJSON geoJsonWriter = new GeometryJSON();
        StringWriter writer = new StringWriter();
        try {
            geoJsonWriter.write(geometry, writer);
            return writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Geometry convertToGeometry(String geoJson) {
        GeometryJSON geoJsonReader = new GeometryJSON();
        try {
            return geoJsonReader.read(geoJson);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
