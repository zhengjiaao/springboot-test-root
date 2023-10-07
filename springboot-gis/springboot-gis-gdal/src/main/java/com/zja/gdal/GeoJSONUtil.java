/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-07 14:21
 * @Since:
 */
package com.zja.gdal;

import org.gdal.ogr.Geometry;

/**
 * @author: zhengja
 * @since: 2023/10/07 14:21
 */
public class GeoJSONUtil {

    /**
     * wtk 转 geometry
     *
     * @return geometry
     */
    public static Geometry wktToGeometry(String wkt) {
        return Geometry.CreateFromWkt(wkt);
    }

    /**
     * geometry 转 wtk
     *
     * @return wkt
     */
    public static String geometryToWkt(Geometry geometry) {
        return geometry.ExportToWkt();
    }


}
