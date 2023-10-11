/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-10 16:34
 * @Since:
 */
package com.zja.gtmain.geojson;

import com.zja.gtmain.TestUtil;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author: zhengja
 * @since: 2023/10/10 16:34
 */
public class GeoJSONReadWriteExample {

    @Test
    public void testGeoJSONReadWrite() {
        String filePath = TestUtil.createTempFile("geojson", "readWrite.geojson");
        try {
            writeGeoJSON(filePath);
            readGeoJSON(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 读取 GeoJSON 数据
    public static void readGeoJSON(String filePath) throws IOException {
        File geojsonFile = new File(filePath);
        FeatureJSON featureJSON = new FeatureJSON();
        SimpleFeatureCollection featureCollection = (SimpleFeatureCollection) featureJSON.readFeatureCollection(new FileInputStream(geojsonFile));

        try (SimpleFeatureIterator iterator = featureCollection.features()) {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                // 处理每个要素的属性和几何信息
                System.out.println("Feature ID: " + feature.getID());
                System.out.println("Geometry: " + feature.getDefaultGeometry());
                System.out.println("Attributes: " + feature.getAttributes());
                System.out.println();
            }
        }
    }

    // 写入 GeoJSON 数据
    public static void writeGeoJSON(String filePath) throws IOException {
        SimpleFeatureType featureType = createFeatureType(); // 创建要素类型
        SimpleFeatureCollection featureCollection = createFeatureCollection(featureType); // 创建要素集合

        File geojsonFile = new File(filePath);
        FeatureJSON featureJSON = new FeatureJSON();
        try (FileWriter writer = new FileWriter(geojsonFile)) {
            featureJSON.writeFeatureCollection(featureCollection, writer);
        }
    }

    public static SimpleFeatureType createFeatureType() {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("MyFeatureType"); // 设置要素类型名称

        // 添加属性字段
        builder.add("name", String.class); // 添加名为 "name" 的字符串属性字段
        builder.add("population", Integer.class); // 添加名为 "population" 的整数属性字段

        // 添加几何字段
        builder.setDefaultGeometry("geometry"); // 设置默认几何字段名为 "geometry"
        builder.add("geometry", Point.class); // 添加名为 "geometry" 的点几何字段

        // 设置坐标参考系统
        CoordinateReferenceSystem crs = getEPSG4326CRS();
        builder.setCRS(crs);

        // 构建要素类型
        return builder.buildFeatureType();
    }

    public static CoordinateReferenceSystem getEPSG4326CRS() {
        try {
            return CRS.decode("EPSG:4326");
        } catch (Exception e) {
            // 如果没有定义，手动创建并返回 EPSG:4326 坐标参考系统
            return DefaultGeographicCRS.WGS84;
        }
    }

    public static SimpleFeatureCollection createFeatureCollection(SimpleFeatureType featureType) {
        // 创建要素集合
        ListFeatureCollection featureCollection = new ListFeatureCollection(featureType);

        // 创建要素
        GeometryFactory geometryFactory = new GeometryFactory();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);

        // 创建第一个要素
        Point point1 = geometryFactory.createPoint(new Coordinate(0, 0));
        featureBuilder.addAll("Feature 1", 100, point1);
        SimpleFeature feature1 = featureBuilder.buildFeature(null);
        featureCollection.add(feature1);

        // 创建第二个要素
        Point point2 = geometryFactory.createPoint(new Coordinate(1, 1));
        featureBuilder.addAll("Feature 2", 200, point2);
        SimpleFeature feature2 = featureBuilder.buildFeature(null);
        featureCollection.add(feature2);

        // 创建更多的要素...

        return featureCollection;
    }
}