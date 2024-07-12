/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-11 11:08
 * @Since:
 */
package com.zja.shapefile;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.data.store.ContentFeatureSource;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.PropertyType;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 读取 Shapefile 文件
 *
 * @author: zhengja
 * @since: 2023/10/11 11:08
 */
public class ReadShapefileExample {

    private static final String SHP_PATH = "D:\\temp\\shp\\";

    /**
     * 读取 Shapefile 文件
     */
    @Test
    public void Read_shp_test() throws IOException {
        // URL shp_URL = new URL("file:/path/to/your/shapefile.shp");
        // URL shp_URL = new URL("https://example.com/landUse.shp");
        // URL shp_URL = Paths.get(SHP_PATH, "test", "川杨河_马家浜_创新河_河流.shp").toUri().toURL();
        URL shp_URL = new File(Paths.get(SHP_PATH, "test", "川杨河_马家浜_创新河_河流.shp").toString()).toURI().toURL();

        ShapefileDataStore dataStore = new ShapefileDataStore(shp_URL);
        System.out.println("dataStore:" + dataStore);
        dataStore.setCharset(StandardCharsets.UTF_8); // 避免读取属性值乱码 ，默认读取可能是：ISO-8859-1
        System.out.println("dataStore:" + dataStore);

        String[] typeNames = dataStore.getTypeNames();
        System.out.println("TypeNames:" + Arrays.toString(typeNames));
        System.out.println("TypeNames[0]:" + typeNames[0]);

        // SimpleFeatureSource featureSource = dataStore.getFeatureSource();
        // SimpleFeatureCollection features = featureSource.getFeatures();

        ContentFeatureSource featureSource = dataStore.getFeatureSource();
        ContentFeatureCollection features = featureSource.getFeatures();
        System.out.println("Features size=" + features.size());
        System.out.println("FeatureType schema=" + features.getSchema());
        System.out.println("FeatureType TypeName=" + features.getSchema().getTypeName());

        try (SimpleFeatureIterator iterator = features.features()) {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();

                System.out.println("-----------------------------------------------------");

                System.out.println("Feature: " + feature);

                // 获取要素的几何信息
                Geometry geometry = (Geometry) feature.getDefaultGeometry();
                System.out.println("Geometry: " + geometry);
                System.out.println("Geometry Type: " + geometry.getGeometryType());
                System.out.println("Geometry Coordinate: " + geometry.getCoordinate());
                if (geometry instanceof Point) {
                    Point point = (Point) geometry;
                    Coordinate coordinate = point.getCoordinate();
                    System.out.println("Point: " + coordinate.x + ", " + coordinate.y);
                }

                // 获取要素的指定属性值
                Object name = feature.getAttribute("name");
                System.out.println("name:" + name);

                // 方式1：获取要素的属性 key + value
                SimpleFeatureType featureType = feature.getFeatureType();
                System.out.println("Attributes Size=: " + featureType.getAttributeCount());
                for (int i = 0; i < featureType.getAttributeCount(); i++) {
                    // the_geom、name
                    String attributeName = featureType.getDescriptor(i).getName().getLocalPart();
                    Object attributeValue = feature.getAttribute(i);
                    AttributeType attributeType = featureType.getDescriptor(i).getType();
                    System.out.println("Attribute Name: " + attributeName + " , Value: " + attributeValue + " , Type: " + attributeType);
                }

                // 方式2：获取要素的属性 key + value
                for (Property property : feature.getProperties()) {
                    // the_geom、name
                    String propertyName = property.getName().toString();
                    Object propertyValue = property.getValue();
                    PropertyType propertyType = property.getType();
                    System.out.println(propertyName + ": " + propertyValue + " , propertyType=" + propertyType);
                }
            }
        }

        dataStore.dispose();
    }

    /**
     * 读取 Shapefile 文件
     */
    @Test
    public void Read_shp_test2() throws IOException {
        // Shapefile 文件路径
        URL shp_URL = new File(Paths.get(SHP_PATH, "test", "川杨河_马家浜_创新河_河流.shp").toString()).toURI().toURL();

        Map<String, Object> params = new HashMap<>();
        params.put(ShapefileDataStoreFactory.URLP.key, shp_URL);
        params.put(ShapefileDataStoreFactory.CREATE_SPATIAL_INDEX.key, true);
        params.put(ShapefileDataStoreFactory.DBFCHARSET.key, StandardCharsets.UTF_8.name()); // 设置字符集为UTF-8, 避免读取属性值乱码

        DataStore dataStore = DataStoreFinder.getDataStore(params);
        // ShapefileDataStore shpDataStore = (ShapefileDataStore) dataStore;
        System.out.println("dataStore:" + dataStore);
        // 确保数据存储是ShapefileDataStore类型
        if (dataStore instanceof ShapefileDataStore) {
            ShapefileDataStore shapefileDataStore = (ShapefileDataStore) dataStore;
            shapefileDataStore.setCharset(StandardCharsets.UTF_8); // 再次确认字符集
        }

        // 获取数据集名称
        String typeName = dataStore.getTypeNames()[0];
        System.out.println("typeName:" + typeName);
        SimpleFeatureType schema = dataStore.getSchema(typeName);
        System.out.println("schema:" + schema);

        // 获取FeatureSource
        SimpleFeatureSource featureSource = dataStore.getFeatureSource(typeName);
        // 获取FeatureCollection 要素集
        SimpleFeatureCollection featureCollection = featureSource.getFeatures();

        // 获取 FeatureCollection 的特征类型
        SimpleFeatureType featureType = featureCollection.getSchema();

        // 输出 FeatureCollection 的属性信息
        System.out.println("Feature Type: " + featureType.getTypeName());
        // 输出 FeatureCollection 的要素数量
        System.out.println("Features Size: " + featureCollection.size());

        // 迭代处理每个要素
        try (SimpleFeatureIterator featureIterator = featureCollection.features()) {
            while (featureIterator.hasNext()) {
                SimpleFeature feature = featureIterator.next();

                System.out.println("-----------------------------------------------------");
                // 获取要素的属性值
                for (Property property : feature.getProperties()) {
                    String propertyName = property.getName().toString();
                    Object propertyValue = property.getValue();
                    System.out.println(propertyName + ": " + propertyValue);
                }
            }
        }
    }

}
