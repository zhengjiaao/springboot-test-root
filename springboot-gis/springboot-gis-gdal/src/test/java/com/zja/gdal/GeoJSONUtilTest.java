/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-07 14:32
 * @Since:
 */
package com.zja.gdal;

import org.gdal.ogr.Geometry;
import org.gdal.ogr.ogr;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author: zhengja
 * @since: 2023/10/07 14:32
 */
public class GeoJSONUtilTest {

    @Test
    public void testWktToGeometry() {
        String wkt = "POINT (30 10)";
        Geometry geometry = GeoJSONUtil.wktToGeometry(wkt);  // 需要先安装 gdal
        assertEquals("POINT", geometry.GetGeometryName());
        assertEquals(30.0, geometry.GetX(), 0.0001);
        assertEquals(10.0, geometry.GetY(), 0.0001);
        System.out.println(geometry); //
    }

    @Test
    public void testGeometryToWkt() {
        Geometry geometry = new Geometry(ogr.wkbPoint);
        geometry.AddPoint_2D(30, 10); // 使用AddPoint_2D来明确创建二维点
        String wkt = GeoJSONUtil.geometryToWkt(geometry);
        assertEquals("POINT (30 10)", wkt);
        System.out.println("Generated WKT: " + wkt);
    }

}
