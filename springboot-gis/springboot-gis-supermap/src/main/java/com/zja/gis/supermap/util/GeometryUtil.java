package com.zja.gis.supermap.util;

import com.supermap.data.Geometry;
import com.supermap.data.Toolkit;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @Author: zhengja
 * @Date: 2024-07-23 16:27
 */
@Slf4j
public class GeometryUtil {

    // 将 Geometry 对象转换为 WKT 字符串
    public static String geometryToWKT(Geometry geometry) {
        return geometry == null ? null : Toolkit.GeometryToWKT(geometry);
    }

    // 将 WKT 转换为 Geometry
    public static Geometry wktToGeometry(String wkt) {
        return wkt == null ? null : Toolkit.WKTToGeometry(wkt);
    }

    // 将 Geometry 对象转换为 GeoJson 字符串
    public static String geometryToGeoJson(Geometry geometry) {
        return geometry == null ? null : Toolkit.GeometryToGeoJson(geometry);
    }

    // 将 GeoJson 转换为 Geometry 对象
    public static Geometry geoJsonToGeometry(String geojson) {
        return geojson == null ? null : Toolkit.GeoJsonToGeometry(geojson);
    }
}
