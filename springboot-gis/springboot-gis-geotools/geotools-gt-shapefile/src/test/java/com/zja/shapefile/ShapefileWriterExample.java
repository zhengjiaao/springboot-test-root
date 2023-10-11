/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-11 12:14
 * @Since:
 */
package com.zja.shapefile;

import org.geotools.data.*;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author: zhengja
 * @since: 2023/10/11 12:14
 */
public class ShapefileWriterExample {

    @Test
    public void test() throws IOException, FactoryException {
        //shapefile 文件路径
        String shapefilePath = TestUtil.createTempFile("output", "1_shapefile.shp");

        //创建空 shapefile
        createEmpty_shapefile(shapefilePath);

        //写数据到 shapefile文件
        writerData_shapefile(shapefilePath);
    }

    private void writerData_shapefile(String shapefilePath) throws IOException {
        //shape中feature定义
        Map<String, Object> params = new HashMap<>();
        params.put("url", new File(shapefilePath).toURI().toURL());
        DataStore dataStore2 = DataStoreFinder.getDataStore(params);
        String typeName = dataStore2.getTypeNames()[0]; // 值为：1_shapefile  文件名
        SimpleFeatureSource featureSource = dataStore2.getFeatureSource(typeName);
        SimpleFeatureType schema = featureSource.getSchema();

        //事务处理
        Transaction transaction = new DefaultTransaction("create");

        if (featureSource instanceof SimpleFeatureStore) {
            SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;

            //构建测试数据，创建要素集合
            SimpleFeatureCollection collection = buildTestData(schema);
            featureStore.setTransaction(transaction);
            try {
                //写入shape中
                featureStore.addFeatures(collection);
                transaction.commit();
            } catch (Exception e) {
                e.printStackTrace();
                transaction.rollback();
            } finally {
                transaction.close();
            }
        } else {
            System.out.println("写入失败");
        }
    }

    //构建测试数据
    private SimpleFeatureCollection buildTestData(SimpleFeatureType schema) {
        //创建一条数据 feature
        List<SimpleFeature> features = new ArrayList<>();

        //创建要素
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(schema);
        Point point = geometryFactory.createPoint(new Coordinate(106.69563874, 29.563694210810283));
        featureBuilder.set("the_geom", point);
        featureBuilder.set("id", "id1");
        featureBuilder.set("name", "学校");
        featureBuilder.set("number", 100);
        featureBuilder.set("time", new Date());
        SimpleFeature feature = featureBuilder.buildFeature(null);

        features.add(feature);

        //创建要素集合
        SimpleFeatureCollection collection = new ListFeatureCollection(schema, features);
        return collection;
    }

    private void createEmpty_shapefile(String shapefilePath) throws IOException {
        FileDataStoreFactorySpi factory = new ShapefileDataStoreFactory();

        //新建文件
        File file = new File(shapefilePath);
        //datastore
        Map<String, Object> params = new HashMap<>();
        params.put("url", file.toURI().toURL());
        params.put("create spatial index", Boolean.TRUE);
        DataStore dataStore = factory.createNewDataStore(params);


        //Feature数据定义
        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        //图层名
        typeBuilder.setName("myLayer");
        //空间坐标
        typeBuilder.setCRS(DefaultGeographicCRS.WGS84);
        typeBuilder.add("the_geom", Point.class);
        //普通属性字段
        typeBuilder.add("id", String.class);
        typeBuilder.length(10).add("name", String.class);//字段长度
        typeBuilder.add("number", Integer.class);
        typeBuilder.add("double", Double.class);
        typeBuilder.add("time", Date.class);
        SimpleFeatureType featureType = typeBuilder.buildFeatureType();
        //创建feature定义到shape
        dataStore.createSchema(featureType);
    }

}

