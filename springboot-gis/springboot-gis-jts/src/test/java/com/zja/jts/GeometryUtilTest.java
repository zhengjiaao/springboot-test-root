package com.zja.jts;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;

/**
 * @Author: zhengja
 * @Date: 2024-07-04 16:25
 */
public class GeometryUtilTest {

    @Test
    public void GeometryConversion_test() {
        // 创建点
        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate(10, 20));

        // 将 Geometry 对象转换为 WKT
        String wkt = GeometryUtil.geometryToWKT(point);
        System.out.println("Geometry: " + point);
        System.out.println("WKT: " + wkt);

        // 将 WKT 转换为 Geometry 对象
        Geometry convertedGeometry = GeometryUtil.wktToGeometry(wkt);
        System.out.println("Converted Geometry: " + convertedGeometry);
        System.out.println("----------------------------------------");
    }

}
