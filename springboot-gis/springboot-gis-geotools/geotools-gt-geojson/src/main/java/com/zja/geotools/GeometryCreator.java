/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-07 13:37
 * @Since:
 */
package com.zja.geotools;

import org.geotools.geojson.geom.GeometryJSON;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import java.util.List;

/**
 * 自定义 Geometry 几何对象构造器
 *
 * @author: zhengja
 * @since: 2023/11/07 13:37
 */
public class GeometryCreator {

    private static final GeometryCreator GEOMETRY_CREATOR = null;
    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();

    /**
     * GeometryJSON 默认保留4位小数
     */
    private static final GeometryJSON GEOMETRY_JSON = new GeometryJSON(6);


    private GeometryCreator() {
    }

    /**
     * 返回本类的唯一实例
     *
     * @return
     */
    public static GeometryCreator getInstance() {
        if (GEOMETRY_CREATOR == null) {
            return new GeometryCreator();
        }
        return GEOMETRY_CREATOR;
    }


    /**
     * 1.1根据X，Y坐标构建一个几何对象： 点 【Point】
     */
    public Point createPoint(double x, double y) {
        Coordinate coord = new Coordinate(x, y);
        return GEOMETRY_FACTORY.createPoint(coord);
    }

    /**
     * 1.2根据几何对象的WKT描述【String】创建几何对象： 点 【Point】
     */
    public Point createPointByWKT(String pointWKT) throws ParseException {
        WKTReader reader = new WKTReader(GEOMETRY_FACTORY);
        return (Point) reader.read(pointWKT);
    }

    /**
     * 1.3根据几何对象的WKT描述【String】创建几何对象：多点 【MultiPoint】
     */
    public MultiPoint createMulPointByWKT(String mPointWKT) throws ParseException {
        WKTReader reader = new WKTReader(GEOMETRY_FACTORY);
        return (MultiPoint) reader.read(mPointWKT);
    }

    /**
     * 2.1根据两点 创建几何对象：线 【LineString】
     *
     * @param ax 第一个点的x坐标
     * @param ay 第一个点的y坐标
     * @param bx 第二个点的x坐标
     * @param by 第二个点的y坐标
     * @return
     */
    public LineString createLine(double ax, double ay, double bx, double by) {
        Coordinate[] coords = new Coordinate[]{new Coordinate(ax, ay), new Coordinate(bx, by)};
        return GEOMETRY_FACTORY.createLineString(coords);
    }

    /**
     * 2.2根据线的WKT描述创建几何对象：线 【LineString】
     *
     * @param LineStringWKT
     * @return
     * @throws ParseException
     */
    public LineString createLineByWKT(String LineStringWKT) throws ParseException {
        WKTReader reader = new WKTReader(GEOMETRY_FACTORY);
        return (LineString) reader.read(LineStringWKT);
    }

    /**
     * 2.3根据点组合的线数组，创建几何对象：多线 【MultiLineString】
     *
     * @param list
     * @return
     */
    public MultiLineString createMLine(List<Coordinate[]> list) {

        if (list == null) {
            return null;
        }

        LineString[] lineStrings = new LineString[list.size()];
        int i = 0;
        for (Coordinate[] coordinates : list) {
            lineStrings[i] = GEOMETRY_FACTORY.createLineString(coordinates);
        }

        return GEOMETRY_FACTORY.createMultiLineString(lineStrings);
    }


    /**
     * 2.4根据几何对象的WKT描述【String】创建几何对象 ： 多线【MultiLineString】
     *
     * @param MLineStringWKT
     * @return
     * @throws ParseException
     */
    public MultiLineString createMLineByWKT(String MLineStringWKT) throws ParseException {
        WKTReader reader = new WKTReader(GEOMETRY_FACTORY);
        return (MultiLineString) reader.read(MLineStringWKT);
    }


    /**
     * 3.1 根据几何对象的WKT描述【String】创建几何对象：多边形 【Polygon】
     *
     * @param PolygonWKT
     * @return
     * @throws ParseException
     */
    public Polygon createPolygonByWKT(String PolygonWKT) throws ParseException {
        WKTReader reader = new WKTReader(GEOMETRY_FACTORY);
        return (Polygon) reader.read(PolygonWKT);
    }

    /**
     * 3.2 根据几何对象的WKT描述【String】创建几何对象： 多多边形 【MultiPolygon】
     *
     * @param MPolygonWKT
     * @return
     * @throws ParseException
     */
    public MultiPolygon createMulPolygonByWKT(String MPolygonWKT) throws ParseException {
        WKTReader reader = new WKTReader(GEOMETRY_FACTORY);
        return (MultiPolygon) reader.read(MPolygonWKT);
    }

    /**
     * 根据多边形数组 进行多多边形的创建
     *
     * @param polygons
     * @return
     * @throws ParseException
     */
    public MultiPolygon createMulPolygonByPolygon(Polygon[] polygons) throws ParseException {
        return GEOMETRY_FACTORY.createMultiPolygon(polygons);
    }

    /**
     * 4.1 根据几何对象数组，创建几何对象集合：【GeometryCollection】
     *
     * @return
     * @throws ParseException
     */
    public GeometryCollection createGeoCollect(Geometry[] geoArray) throws ParseException {
        return GEOMETRY_FACTORY.createGeometryCollection(geoArray);
    }

    /**
     * 5.1 根据圆点以及半径创建几何对象：特殊的多边形--圆 【Polygon】
     *
     * @param x      圆点x坐标
     * @param y      圆点y坐标
     * @param radius 半径
     * @return
     */
    public Polygon createCircle(double x, double y, final double radius) {

        //圆上面的点个数
        final int sides = 32;
        Coordinate[] coords = new Coordinate[sides + 1];
        for (int i = 0; i < sides; i++) {
            double angle = ((double) i / (double) sides) * Math.PI * 2.0;
            double dx = Math.cos(angle) * radius;
            double dy = Math.sin(angle) * radius;
            coords[i] = new Coordinate((double) x + dx, (double) y + dy);
        }
        coords[sides] = coords[0];
        //线性环
        LinearRing ring = GEOMETRY_FACTORY.createLinearRing(coords);
        return GEOMETRY_FACTORY.createPolygon(ring, null);
    }


    /**
     * 6.1 根据WKT创建环
     *
     * @param ringWKT
     * @return
     * @throws ParseException
     */
    public LinearRing createLinearRingByWKT(String ringWKT) throws ParseException {
        WKTReader reader = new WKTReader(GEOMETRY_FACTORY);
        return (LinearRing) reader.read(ringWKT);
    }
}
