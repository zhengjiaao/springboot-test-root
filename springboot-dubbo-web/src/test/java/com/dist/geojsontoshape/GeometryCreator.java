//package com.dist.geojsontoshape;
//
//import com.vividsolutions.jts.geom.*;
//import com.vividsolutions.jts.io.ParseException;
//import com.vividsolutions.jts.io.WKTReader;
//
//import java.util.List;
//
///**几何对象构建器   单例模式
// * @author zhengja@dist.com.cn
// * @data 2019/8/28 15:45
// */
//public class GeometryCreator {
//
//    public static GeometryCreator geometryCreator = null;
//
//    private GeometryFactory geometryFactory = new GeometryFactory();
//
//    private GeometryCreator() {
//    }
//
//    /**
//     * 返回本类的唯一实例
//     * @return
//     */
//    public static GeometryCreator getInstance() {
//        if (geometryCreator == null) {
//            return new GeometryCreator();
//        }
//        return geometryCreator;
//    }
//
//
//    /**
//     * 1.构建点
//     */
//
//    /**
//     * 1.1根据X，Y坐标构建一个几何对象： 点 【Point】
//     * @param x
//     * @param y
//     * @return
//     */
//    public Point createPoint(double x,double y){
//        Coordinate coord = new Coordinate(x, y);
//        Point point = geometryFactory.createPoint(coord);
//        return point;
//    }
//
//    /**
//     * 1.2根据几何对象的WKT描述【String】创建几何对象： 点 【Point】
//     * @return
//     * @throws ParseException
//     */
//    public Point createPointByWKT(String PointWKT) throws ParseException {
//        WKTReader reader = new WKTReader(geometryFactory);
//        Point point = (Point) reader.read(PointWKT);
//        return point;
//    }
//
//    /**
//     * 1.3根据几何对象的WKT描述【String】创建几何对象：多点 【MultiPoint】
//     * @return
//     * @throws ParseException
//     */
//    public MultiPoint createMulPointByWKT(String MPointWKT)throws ParseException{
//        WKTReader reader = new WKTReader( geometryFactory );
//        MultiPoint mpoint = (MultiPoint) reader.read(MPointWKT);
//        return mpoint;
//    }
//
//
//    /**
//     * 2.构建线
//     */
//
//
//    /**
//     * 2.1根据两点 创建几何对象：线 【LineString】
//     * @param ax
//     * @param ay
//     * @param bx
//     * @param by
//     * @return
//     */
//    public LineString createLine(double ax,double ay,double bx,double by){
//        Coordinate[] coords  = new Coordinate[] {new Coordinate(ax, ay), new Coordinate(bx, by)};
//        LineString line = geometryFactory.createLineString(coords);
//        return line;
//    }
//
//    /**
//     * 2.2根据线的WKT描述创建几何对象：线 【LineString】
//     * @param LineStringWKT
//     * @return
//     * @throws ParseException
//     */
//    public LineString createLineByWKT(String LineStringWKT) throws ParseException{
//        WKTReader reader = new WKTReader( geometryFactory );
//        LineString line = (LineString) reader.read("LINESTRING(0 0, 2 0)");
//        return line;
//    }
//
//    /**
//     * 2.3根据点组合的线数组，创建几何对象：多线 【MultiLineString】
//     * @param list
//     * @return
//     */
//    public MultiLineString createMLine(List<Coordinate[]> list){
//
//        MultiLineString ms = null;
//
//
//        if(list == null){
//            return ms;
//        }
//
//        LineString[] lineStrings = new LineString[list.size()];
//
//
////      Coordinate[] coords1  = new Coordinate[] {new Coordinate(2, 2), new Coordinate(2, 2)};
////      LineString line1 = geometryFactory.createLineString(coords1);
////
////      Coordinate[] coords2  = new Coordinate[] {new Coordinate(2, 2), new Coordinate(2, 2)};
////      LineString line2 = geometryFactory.createLineString(coords2);
//
//        int i = 0;
//        for (Coordinate[] coordinates : list) {
//            lineStrings[i] = geometryFactory.createLineString(coordinates);
//        }
//
//        ms = geometryFactory.createMultiLineString(lineStrings);
//
//        return ms;
//    }
//
//
//    /**
//     * 2.4根据几何对象的WKT描述【String】创建几何对象 ： 多线【MultiLineString】
//     * @param MLineStringWKT
//     * @return
//     * @throws ParseException
//     */
//    public MultiLineString createMLineByWKT(String MLineStringWKT)throws ParseException{
//        WKTReader reader = new WKTReader( geometryFactory );
//        MultiLineString line = (MultiLineString) reader.read(MLineStringWKT);
//        return line;
//    }
//
//
//
//    /**
//     * 3.构建多边形
//     */
//
//
//    /**
//     * 3.1 根据几何对象的WKT描述【String】创建几何对象：多边形 【Polygon】
//     * @param PolygonWKT
//     * @return
//     * @throws ParseException
//     */
//    public Polygon createPolygonByWKT(String PolygonWKT) throws ParseException{
//        WKTReader reader = new WKTReader( geometryFactory );
//        Polygon polygon = (Polygon) reader.read(PolygonWKT);
//        return polygon;
//    }
//
//    /**
//     * 3.2 根据几何对象的WKT描述【String】创建几何对象： 多多边形 【MultiPolygon】
//     * @param MPolygonWKT
//     * @return
//     * @throws ParseException
//     */
//    public MultiPolygon createMulPolygonByWKT(String MPolygonWKT) throws ParseException{
//        WKTReader reader = new WKTReader( geometryFactory );
//        MultiPolygon mpolygon = (MultiPolygon) reader.read(MPolygonWKT);
//        return mpolygon;
//    }
//
//
//    /**
//     * 4.构建几何对象集合
//     */
//
//
//    /**
//     * 4.1 根据几何对象数组，创建几何对象集合：【GeometryCollection】
//     * @return
//     * @throws ParseException
//     */
//    public GeometryCollection createGeoCollect(Geometry[] geoArray) throws ParseException{
////            LineString line = createLine(125.12,25.4,85.63,99.99);
////            Polygon poly    =  createPolygonByWKT("POLYGON((20 10, 30 0, 40 10, 30 20, 20 10))");
////            Geometry g1     = geometryFactory.createGeometry(line);
////            Geometry g2     = geometryFactory.createGeometry(poly);
////            Geometry[] geoArray = new Geometry[]{g1,g2};
//        GeometryCollection gc = geometryFactory.createGeometryCollection(geoArray);
//        return gc;
//    }
//
//
//    /**
//     * 5.构建圆
//     */
//
//    /**
//     * 5.1 根据圆点以及半径创建几何对象：特殊的多边形--圆 【Polygon】
//     * @param x
//     * @param y
//     * @param RADIUS
//     * @return
//     */
//    public Polygon createCircle(double x, double y, final double RADIUS){
//
//        final int SIDES = 32;//圆上面的点个数
//
//        Coordinate coords[] = new Coordinate[SIDES+1];
//        for( int i = 0; i < SIDES; i++){
//            double angle = ((double) i / (double) SIDES) * Math.PI * 2.0;
//            double dx = Math.cos( angle ) * RADIUS;
//            double dy = Math.sin( angle ) * RADIUS;
//            coords[i] = new Coordinate( (double) x + dx, (double) y + dy );
//        }
//        coords[SIDES] = coords[0];
//        LinearRing ring = geometryFactory.createLinearRing(coords);
//        Polygon polygon = geometryFactory.createPolygon(ring, null);
//        return polygon;
//    }
//
//}
