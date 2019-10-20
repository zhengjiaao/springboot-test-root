package com.dist.geojsontoshape;

import com.alibaba.fastjson.JSON;
import com.dist.dto.PolygonObject;
import com.google.gson.Gson;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.WKTReader;
import org.geotools.geojson.geom.GeometryJSON;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * geometry为Arcgis中的几何对象，包括Extent、Multipoint、Point 、Polygon 、Polyline
 *
 * @author zhengja@dist.com.cn
 * @data 2019/8/16 16:31
 */
public class GeojsonToShapeTest {

    private static GeometryFactory geometryFactory = new GeometryFactory();

    @Test
    public void test() throws IOException {
        String wktjson = "{\'rings\':[[[120.01481126800002,30.686383128000074],[120.01474083399997,30.686419206000039],[120.01481126800002,30.686383128000074]]],\'spatialReference\':{\'wkid\':4490,\'latestWkid\':4490}}";

        String wktPolygon = "POLYGON ((120.01481126800002 30.686383128000074, 120.01474083399997 30.686419206000039, 120.01481126800002 30.686383128000074))";

        String wktToJson = getPOLYGONWktToJson(wktPolygon, 4990);
        System.out.println("wktToJson=" + wktToJson);

        PolygonObject polygonObject = JSON.parseObject(wktjson, PolygonObject.class);
        List<List<Double[]>> rings = polygonObject.getRings();
        for (List<Double[]> dou : rings) {
            for (Double[] dous : dou) {
                System.out.println("dous[0]=" + dous[0] + " " + "dous[1]=" + dous[1]);
            }
        }
        System.out.println("spatialReference= " + polygonObject.getSpatialReference());

        Gson gson = new Gson();
        String json = gson.toJson(polygonObject);
        System.out.println("json= " + json);

        String wkt = "POINT(114.298456 30.553544)";
        //String wktToJson2 = getPOLYGONWktToJson(wkt, 4902);
        //System.out.println("wktToJson2 = "+wktToJson2); //wktToJson = {"rings":[[[14.298456,30.55354]]],"spatialReference":{"wkid":4902}}

        //String toJson = wktToJson(wkt);
        //System.out.println("toJson= "+toJson);//toJson= {"type":"Point","coordinates":[114.298456,30.553544]}

    }

    /**
     * 多边形 转换 JSON
     *
     * @param wkt
     * @param wkid
     * @return
     */
    private String getPOLYGONWktToJson(String wkt, int wkid) {
        PolygonObject polygonObject = new PolygonObject();
        List<List<Double[]>> lists = new ArrayList<List<Double[]>>();
        String ToTailWkt = wkt.substring(0, wkt.length() - 1);
        String[] strHead = ToTailWkt.split("\\(", 2);
        String[] strList = strHead[1].split("\\), \\(");
        for (int i = 0; i < strList.length; i++) {
            String item = strList[i].trim();
            item = item.substring(1, item.length() - 1);
            String[] items = item.split(",");
            List<Double[]> list = new ArrayList<Double[]>();
            for (int j = 0; j < items.length; j++) {
                String jItem = items[j].trim();
                String[] jItems = jItem.split(" ");
                Double[] listResult = new Double[]{
                        Double.parseDouble(jItems[0]),
                        Double.parseDouble(jItems[1])};
                list.add(listResult);
            }
            lists.add(list);
        }
        HashMap<String, Integer> spatialReference = new HashMap<String, Integer>();
        spatialReference.put("wkid", wkid);
        polygonObject.setRings(lists);
        polygonObject.setSpatialReference(spatialReference);
        Gson gson = new Gson();
        return gson.toJson(polygonObject);
    }


    /**
     * 1、由wkt格式的geometry生成geojson
     * 2、 geojson格式转wkt格式
     * 3、geoToJson
     */
    @Test
    public void test2() {
        String wkt = "POINT(114.298456 30.553544)";
        String wktPolygon = "POLYGON((120.01481126800002 30.686383128000074, 120.01474083399997 30.686419206000039, 120.01481126800002 30.686383128000074))";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "北清路");
        map.put("author", "李雷");

        //String wktToJson = wktToJson(wktPolygon);
        // System.out.println("wktToJson "+wktToJson);  //{"type":"Point","coordinates":[114.298456,30.553544]}

        String json = geoToJson(wktPolygon, map);
        System.out.println("geoToJson " + json); //{"author":"李雷","name":"北清路","geometry":{"type":"Point","coordinates":[114.2985,30.5535]}}

        System.out.println("jsonToWkt " + jsonToWkt(json)); //POINT (114.2985 30.5535)

    }


    /**
     * 由wkt格式的geometry生成geojson
     *
     * @param wkt
     * @return
     */
    public static String wktToJson(String wkt) {
        String json = null;
        try {
            WKTReader reader = new WKTReader();
            Geometry geometry = reader.read(wkt);
            StringWriter writer = new StringWriter();
            GeometryJSON g = new GeometryJSON(20);
            g.write(geometry, writer);
            json = writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * geojson格式转wkt格式
     *
     * @param geoJson
     * @return
     */
    public static String jsonToWkt(String geoJson) {
        String wkt = null;
        GeometryJSON gjson = new GeometryJSON();
        Reader reader = new StringReader(geoJson);
        try {
            Geometry geometry = gjson.read(reader);
            wkt = geometry.toText();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wkt;
    }

    //geoToJson
    public static String geoToJson(String wkt, Map<String, Object> map) {
        String json = null;
        try {
            WKTReader reader = new WKTReader();
            Geometry geometry = reader.read(wkt);
            StringWriter writer = new StringWriter();
            GeometryJSON g = new GeometryJSON();
            g.write(geometry, writer);

            map.put("geometry", writer);

            json = JSONObject.toJSONString(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }


    /**
     * wkt 转 GeoJson   成功
     *
     * @throws Exception
     */
    @Test
    public void test3() throws Exception {
        String wkt = "POINT(114.298456 30.553544)";
        JSONObject jsonObject = GeomWktToGeoJson(wkt);
        System.out.println("GeomWktToGeoJson =" + jsonObject);
    }

    /**
     * wkt 转 GeoJson
     *
     * @param wkt
     * @return
     * @throws Exception
     */
    public static JSONObject GeomWktToGeoJson(String wkt) throws Exception {

        WKTReader wktReader = new WKTReader();

        Geometry g = (Geometry) wktReader.read(wkt);

        //GeomToGeoJson(g);

        GeometryJSON j = new GeometryJSON(20);// 注意在转换的过程中使用默认的构造参数转换的点会出现丢失的现象，这里传入的参数可以使传入点精度精确到20位

        Object obj = JSONValue.parse(j.toString(g));

        JSONObject array = (JSONObject) obj;

        // System.out.println(array.toJSONString());

        return array;

    }

    /**
     * Geometry 转 GeoJson
     *
     * @param g
     * @return
     * @throws Exception
     */
    public static JSONObject GeomToGeoJson(Geometry g) throws Exception {

        GeometryJSON j = new GeometryJSON(20);   //// 注意在转换的过程中使用默认的构造参数转换的点会出现丢失的现象，这里传入的参数可以使传入点精度精确到20位

        Object obj = JSONValue.parse(j.toString(g));

        JSONObject array = (JSONObject) obj;

        System.out.println(array.toJSONString());

        return array;

    }

    @Test
    public void test8() {

        Coordinate coords[] = new Coordinate[4];
        coords[0] = new Coordinate(113.6248492, 34.7384023);
        coords[1] = new Coordinate(113.6248465, 34.7381533);
        coords[2] = new Coordinate(113.6248492, 34.7384024);
        coords[3] = new Coordinate(113.6248492, 34.7384023);

        Geometry geometry = null;

        if (isClosed(coords)) {
            //如果是闭合的多边形
            geometry = geometryFactory.createPolygon(coords);
        } else {
            if (coords.length == 1) {
                //如果坐标数组就一个元素的话，除了Point，我想不到其他Geometry
                geometry = geometryFactory.createPoint(coords[0]);
            } else {
                //否则的话，不闭合也不是点，那么它只能是线了--- LineString
                geometry = geometryFactory.createLineString(coords);
            }
        }
        System.err.println("类型：" + geometry.getGeometryType() + "\n形态：" + geometry);

        //类型：Polygon
        //形态：POLYGON ((113.6248492 34.7384023, 113.6248465 34.7381533, 113.6248492 34.7384024, 113.6248492 34.7384023))
    }

    /**
     * 判斷 -- 是否是闭合的(Polygon)
     *
     * @param coords
     * @return
     */
    public boolean isClosed(Coordinate coords[]) {
        /**
         * 闭合条件
         * 1.点(坐标)数组不等于空
         * 2.点(坐标)数组至少含4个元素(>=4 or >3 --  最基本的闭合多边形是---> 三角形)
         * 3.点(坐标)数组首尾元素相当(关键条件，所谓的闭合，也就是首尾点是同一个点，绕了一圈又绕回来了才称之为闭合)
         */
        return coords != null && coords.length > 3 && coords[0].equals(coords[coords.length - 1]);
    }
}

