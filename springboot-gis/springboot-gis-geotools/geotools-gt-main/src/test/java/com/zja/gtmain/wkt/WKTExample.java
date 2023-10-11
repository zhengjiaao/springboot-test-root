/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-11 10:22
 * @Since:
 */
package com.zja.gtmain.wkt;

import org.geotools.geometry.jts.WKTReader2;
import org.geotools.geometry.jts.WKTWriter2;
import org.locationtech.jts.geom.Geometry;

/**
 * @author: zhengja
 * @since: 2023/10/11 10:22
 */
public class WKTExample {

    public static void main(String[] args) throws Exception {
        // 读取 WKT 格式的数据
        String wkt = "POINT (30 10)";
        Geometry geometry = readWKT(wkt);

        // 处理几何对象
        processGeometry(geometry);

        // 将几何对象转换为 WKT 格式的数据
        String convertedWKT = writeWKT(geometry);
        System.out.println("Converted WKT: " + convertedWKT);
    }

    public static Geometry readWKT(String wkt) throws Exception {
        WKTReader2 reader = new WKTReader2();
        return reader.read(wkt);
    }

    public static void processGeometry(Geometry geometry) {
        // 处理几何对象的逻辑
        System.out.println("Geometry Type: " + geometry.getGeometryType());
        System.out.println("Coordinates: " + geometry.getCoordinates().length);
    }

    public static String writeWKT(Geometry geometry) {
        WKTWriter2 writer = new WKTWriter2();
        return writer.write(geometry);
    }
}
