/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-07 14:34
 * @Since:
 */
package com.zja.postgis.util;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;

/**
 * @author: zhengja
 * @since: 2023/11/07 14:34
 */
public class GeometryUtil {

    // 将 Geometry 对象转换为 WKT
    public static String geometryToWKT(Geometry geometry) {
        WKTWriter writer = new WKTWriter();
        return writer.write(geometry);
    }

    // 将 WKT 转换为 Geometry 对象
    public static Geometry wktToGeometry(String wkt) {
        WKTReader reader = new WKTReader();
        try {
            return reader.read(wkt);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}
