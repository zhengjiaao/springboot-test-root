/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-11 12:28
 * @Since:
 */
package com.zja.geotools;

import com.zja.geotools.util.TargetPathUtil;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author: zhengja
 * @since: 2023/10/11 12:28
 */
public class WriteGeoJSONExample {

    @Test
    public void testWriteGeoJSONFile() {
        // GeoJSON 文件路径
        String geoJSONFilePath = TargetPathUtil.createTempFile("output.geojson");

        // 创建要素类型
        SimpleFeatureType featureType = createFeatureType();

        // 创建要素集合
        SimpleFeatureCollection featureCollection = createFeatureCollection(featureType);

        // 写入 GeoJSON 文件
        writeGeoJSON(featureCollection, geoJSONFilePath);
    }

    //创建要素类型
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
        builder.setCRS(DefaultGeographicCRS.WGS84);

        // 构建要素类型
        return builder.buildFeatureType();
    }

    //创建要素集合
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

    private static void writeGeoJSON(SimpleFeatureCollection featureCollection, String filePath) {
        try (OutputStream outputStream = Files.newOutputStream(Paths.get(filePath))) {
            // 使用 FeatureJSON 将要素集合写入 GeoJSON 文件
            FeatureJSON featureJSON = new FeatureJSON();
            featureJSON.writeFeatureCollection(featureCollection, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
