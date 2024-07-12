package com.zja.shapefile;

import org.geotools.data.*;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.*;

/**
 * @Author: zhengja
 * @Date: 2024-07-05 14:44
 */
public class ShapefileTest {

    String shp_path = "D:\\temp\\shp\\";

    @Test
    public void ReadShapefile_test() throws MalformedURLException {

        String vectorFile = Paths.get(shp_path, "test", "园区及公司图层.shp").toString();

        // File file = new File("path/to/your/shapefile.shp"); // 替换为你的Shapefile路径
        File file = new File(vectorFile); // 替换为你的Shapefile路径

        Map<String, Object> map = new HashMap<>();
        map.put("url", file.toURI().toURL());

        try {
            DataStore dataStore = DataStoreFinder.getDataStore(map);
            // 获取数据集的类型名称
            String typeName = dataStore.getTypeNames()[0];
            System.out.println("Type Name: " + typeName);

            // 获取FeatureSource
            SimpleFeatureSource featureSource = dataStore.getFeatureSource(typeName);
            // 获取所有的Feature
            SimpleFeatureCollection features = featureSource.getFeatures();

            SimpleFeatureType schema = features.getSchema();
            System.out.println("Feature Type Name: " + schema.getName().getLocalPart());

            try (SimpleFeatureIterator iterator = features.features()) {
                while (iterator.hasNext()) {
                    SimpleFeature feature = iterator.next();
                    System.out.println("Feature ID: " + feature.getID());
                    System.out.println("Attributes: ");

                    // 获取所有属性名称、值
                    int attributeCount = feature.getAttributeCount();
                    for (int i = 0; i < attributeCount; i++) {
                        System.out.println("\t" + schema.getDescriptor(i).getLocalName() + ": " + feature.getAttribute(i));
                    }

                    // 获取所有属性(值)
                    // feature.getAttributes().forEach(attribute -> System.out.println("\t" + attribute));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void WriteShapefile_test() throws IOException {
        // 定义Shapefile的路径和名称
        File newShapefile = new File("target/new_shapefile.shp");

        // 几何类型工厂，用于创建几何对象
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

        // 构建Shapefile的特征类型（schema）
        SimpleFeatureTypeBuilder featureTypeBuilder = new SimpleFeatureTypeBuilder();
        featureTypeBuilder.setName("MyPointFeature");
        featureTypeBuilder.setCRS(org.geotools.referencing.crs.DefaultGeographicCRS.WGS84); // 设置坐标参考系统为WGS84
        featureTypeBuilder.add("the_geom", Point.class); // 添加几何属性
        featureTypeBuilder.add("name", String.class); // 添加一个属性“name”
        SimpleFeatureType featureType = featureTypeBuilder.buildFeatureType();

        // 创建Shapefile数据存储
        Map<String, Object> params = new HashMap<>();
        params.put("url", newShapefile.toURI().toURL());
        DataStoreFactorySpi dataStoreFactory = new org.geotools.data.shapefile.ShapefileDataStoreFactory();

        // 创建Shapefile数据存储
        DataStore newDataStore = dataStoreFactory.createNewDataStore(params);

        // 设置数据存储的模式
        newDataStore.createSchema(featureType); // 此时，会生成shp数据结构文件
        String typeName = newDataStore.getTypeNames()[0];

        // 转换为可写特征存储
        SimpleFeatureStore featureStore = (SimpleFeatureStore) newDataStore.getFeatureSource(typeName);

        // 准备特征数据
        List<SimpleFeature> features = new ArrayList<>();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);

        // 添加几个示例特征
        featureBuilder.add(geometryFactory.createPoint(new Coordinate(121.60344592, 31.19171069)));
        featureBuilder.add("Location A-张江润和国际总部园-食堂");
        // featureBuilder.set("the_geom", geometryFactory.createPoint(new Coordinate(-73.985693, 40.748817)));
        // featureBuilder.set("name", "Location A");
        features.add(featureBuilder.buildFeature(null));

        featureBuilder.reset();
        featureBuilder.add(geometryFactory.createPoint(new Coordinate(121.60249979, 31.19245260)));
        featureBuilder.add("Location B-张江润和国际总部园-银联商务");
        features.add(featureBuilder.buildFeature(null));

        // 使用收集到的特征创建SimpleFeatureCollection
        SimpleFeatureCollection collection = new ListFeatureCollection(featureType, features);

        // 写入特征到Shapefile
        featureStore.addFeatures(collection);

        // 关闭数据存储
        newDataStore.dispose();

        System.out.println("Shapefile created successfully!");
    }

    @Test
    public void WriteShapefile_test2() throws IOException {
        // 定义Shapefile的路径和名称
        File newShapefile = new File("target/new_shapefile.shp");

        // 尝试通过数据存储直接写入特征，绕过获取FeatureSource的步骤
        try (Transaction transaction = new DefaultTransaction()) {
            // 几何类型工厂，用于创建几何对象
            GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

            // 构建Shapefile的特征类型（schema）
            SimpleFeatureTypeBuilder featureTypeBuilder = new SimpleFeatureTypeBuilder();
            featureTypeBuilder.setName("MyPointFeature");
            featureTypeBuilder.setCRS(org.geotools.referencing.crs.DefaultGeographicCRS.WGS84); // 设置坐标参考系统为WGS84
            featureTypeBuilder.add("the_geom", Point.class); // 添加几何属性
            featureTypeBuilder.add("name", String.class); // 添加一个属性“name”
            SimpleFeatureType featureType = featureTypeBuilder.buildFeatureType();

            // 创建Shapefile数据存储
            Map<String, Object> params = new HashMap<>();
            params.put("url", newShapefile.toURI().toURL());
            DataStoreFactorySpi dataStoreFactory = new org.geotools.data.shapefile.ShapefileDataStoreFactory();

            // 创建Shapefile数据存储
            DataStore newDataStore = dataStoreFactory.createNewDataStore(params);

            // 设置数据存储的模式
            newDataStore.createSchema(featureType);
            String typeName = newDataStore.getTypeNames()[0];
            SimpleFeatureSource featureSource = newDataStore.getFeatureSource(typeName);

            if (featureSource instanceof SimpleFeatureStore) {
                SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;

                // 准备和写入特征数据的逻辑

                // 准备特征数据
                List<SimpleFeature> features = new ArrayList<>();
                SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);

                // 添加几个示例特征
                featureBuilder.add(geometryFactory.createPoint(new Coordinate(121.60344592, 31.19171069)));
                featureBuilder.add("Location A-张江润和国际总部园-食堂");
                // featureBuilder.set("the_geom", geometryFactory.createPoint(new Coordinate(-73.985693, 40.748817)));
                // featureBuilder.set("name", "Location A");
                features.add(featureBuilder.buildFeature(null));

                featureBuilder.reset();
                featureBuilder.add(geometryFactory.createPoint(new Coordinate(121.60249979, 31.19245260)));
                featureBuilder.add("Location B-银联商务");
                features.add(featureBuilder.buildFeature(null));

                // 使用收集到的特征创建SimpleFeatureCollection
                SimpleFeatureCollection simpleFeatures = new ListFeatureCollection(featureType, features);

                // 写入特征到Shapefile
                featureStore.addFeatures(simpleFeatures);

                // 提交事务
                transaction.commit();
                // 关闭数据存储
                newDataStore.dispose();
            } else {
                throw new IOException("The datastore does not support read/write access for the schema '" + typeName + "'.");
            }
        } catch (IOException | RuntimeException e) {
            // 处理异常
            e.printStackTrace();
        }

        System.out.println("Shapefile created successfully!");
    }
}
