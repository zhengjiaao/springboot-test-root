/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-07 13:43
 * @Since:
 */
package com.zja.geotools;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;

/**
 * @author: zhengja
 * @since: 2023/11/07 13:43
 */
public class GeometryCreatorTest {

    private static GeometryCreator gCreator = GeometryCreator.getInstance();

    @Test
    public void test() throws ParseException {
        //Point 【点】
        String pointWkt = "POINT (120.76164848270959 31.22001141278534)";
        Point point = gCreator.createPointByWKT(pointWkt);
        System.out.println(point);

        // LineString【线】
        String linestringWkt = "LINESTRING(113.511315990174 41.7274734296674,113.51492087909 41.7284983348307,113.516079593384 41.727649586406,113.515907932007 41.7262243043929,113.514019656861 41.7247989907606,113.512131381714 41.7250872589898,113.51138036319 41.7256637915682,113.511315990174 41.7274734296674)";
        LineString lineString = gCreator.createLineByWKT(linestringWkt);
        System.out.println(lineString);

        // Polygon【面】
        String polygonWkt = "POLYGON ((103.859188 34.695908, 103.85661 34.693788, 103.862027 34.69259, 103.863709 34.695078, 103.859188 34.695908))";
        Polygon polygon = gCreator.createPolygonByWKT(polygonWkt);
        System.out.println(polygon);

        // MultiPolygon【多面】
        String multiPolyWkt = "MULTIPOLYGON(((101.870371 25.19228,101.873633 25.188183,101.880564 25.184416,101.886808 25.186028,101.892043 25.189969,101.896592 25.190163,101.903716 25.190785,101.905454 25.193464,101.899897 25.196202,101.894146 25.197911,101.891657 25.19826,101.886078 25.197658,101.884211145538 25.2007060137013,101.88172564506 25.1949712942389,101.87874 25.199619,101.874641 25.200998,101.868547 25.202415,101.863741 25.202415,101.85887 25.202842,101.854557 25.202182,101.852604 25.199736,101.852282 25.19628,101.854492 25.194183,101.855608 25.192668,101.863698 25.192105,101.870371 25.19228)))";
        MultiPolygon multiPolygon = gCreator.createMulPolygonByWKT(multiPolyWkt);
        System.out.println(multiPolygon);

        // 几何对象的范围【矩形边界】
        Envelope envelope = polygon.getEnvelopeInternal();
        System.out.println(envelope);
    }
}
