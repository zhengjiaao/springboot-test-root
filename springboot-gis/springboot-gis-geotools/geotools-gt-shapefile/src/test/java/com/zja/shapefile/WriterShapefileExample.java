/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-11 12:14
 * @Since:
 */
package com.zja.shapefile;

import com.zja.shapefile.util.TargetPathUtil;
import org.geotools.data.*;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.FeatureCollection;
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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.*;

/**
 * 创建空Shapefile文件，并写入数据到Shapefile中
 *
 * @author: zhengja
 * @since: 2023/10/11 12:14
 */
public class WriterShapefileExample {

    private static final String SHP_PATH = "D:\\temp\\shp\\";

    // 创建 SimpleFeatureType (创建表结构)
    private SimpleFeatureType buildFeatureType() {
        // Feature数据定义
        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        typeBuilder.setName("myLayer"); // 图层名
        typeBuilder.setCRS(DefaultGeographicCRS.WGS84);  // 设置空间坐标系
        typeBuilder.add("the_geom", Point.class);  // 设置坐标类型
        // 普通属性字段
        typeBuilder.add("id", String.class);
        typeBuilder.length(80).add("name", String.class);  // 设置字段长度
        typeBuilder.add("time", Date.class);

        return typeBuilder.buildFeatureType();
    }

    // 创建 FeatureCollection 集合（构建数据）
    private ListFeatureCollection buildFeatureCollection(SimpleFeatureType schema) {
        GeometryFactory geomFactory = JTSFactoryFinder.getGeometryFactory();
        Point point = geomFactory.createPoint(new Coordinate(121.60203030443612704, 31.19239503727569129));  // 上海润和总部园_北门_点图层

        // 方式1 不区分属性顺序
        SimpleFeature feature = new SimpleFeatureBuilder(schema).buildFeature(null);
        feature.setAttribute("the_geom", point); // 设置 the_geom ， 或者 feature.setDefaultGeometry(point);
        feature.setAttribute("id", "1");
        feature.setAttribute("name", "上海润和总部园_北门");
        feature.setAttribute("time", new Date());

        // 方式2
        /*SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(schema);
        featureBuilder.set("the_geom", point);
        featureBuilder.set("id", "1");
        featureBuilder.set("name", "上海润和总部园_北门");
        featureBuilder.set("time", new Date());
        SimpleFeature feature = featureBuilder.buildFeature(null);*/

        // 方式3
        /*Object[] values = {};
        SimpleFeature feature = SimpleFeatureBuilder.build(schema, values, "id");
        feature.setAttribute("the_geom", point); // 设置 the_geom ， 或者 feature.setDefaultGeometry(point);
        feature.setAttribute("id", "1");
        feature.setAttribute("name", "上海润和总部园_北门");
        feature.setAttribute("time", new Date());*/

        // List<SimpleFeature> featureList = new ArrayList<>();
        // featureList.add(feature);
        // return new ListFeatureCollection(schema, featureList);

        return new ListFeatureCollection(schema, feature);
    }

    /**
     * 使用 SimpleFeatureStore 写入 shp文件
     */
    // private static void writerFeatureCollection(SimpleFeatureCollection featureCollection, ShapefileDataStore shapefileDataStore) throws IOException {
    private static void writerFeatureCollection(ListFeatureCollection featureCollection, ShapefileDataStore shapefileDataStore) throws IOException {
        SimpleFeatureSource featureSource = shapefileDataStore.getFeatureSource(shapefileDataStore.getTypeNames()[0]);
        if (featureSource instanceof SimpleFeatureStore) {
            SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
            featureStore.addFeatures(featureCollection);
            // featureStore.addFeatures(new ListFeatureCollection(schema, feature));
        }
    }

    /**
     * 使用 FeatureWriter 写入 shp文件
     */
    // private static void writerFeature(ListFeatureCollection featureCollection, ShapefileDataStore shapefileDataStore) throws IOException {
    private static void writerFeature(SimpleFeatureCollection featureCollection, ShapefileDataStore shapefileDataStore) throws IOException {
        FeatureWriter<SimpleFeatureType, SimpleFeature> featureWriter = shapefileDataStore.getFeatureWriterAppend(Transaction.AUTO_COMMIT);
        // SimpleFeatureIterator simpleFeatureIterator = featureCollection.features();
        SimpleFeatureIterator simpleFeatureIterator = featureCollection.features();
        while (simpleFeatureIterator.hasNext()) {
            SimpleFeature simpleFeature = simpleFeatureIterator.next();
            SimpleFeature simpleFeature1 = featureWriter.next();
            simpleFeature1.setAttributes(simpleFeature.getAttributes());
        }
        featureWriter.write();
        featureWriter.close();
        simpleFeatureIterator.close();
    }

    /**
     * 创建 Shapefile 文件
     */
    @Test
    public void Writer_shp_test() throws IOException {
        // 定义输出文件的URI
        // URL shp_URL = new URL("file:/path/to/your/newshapefile.shp");
        // URL shp_URL = new URL("https://example.com/landUse.shp");
        // URL shp_URL = Paths.get(SHP_PATH, "test", "newShapefile.shp").toUri().toURL();
        URL shp_URL = new File(Paths.get("target", "newShapefile1.shp").toString()).toURI().toURL();

        // 创建ShapefileDataStore
        ShapefileDataStore dataStore = new ShapefileDataStore(shp_URL);
        dataStore.setCharset(StandardCharsets.UTF_8);
        dataStore.setIndexed(true);
        System.out.println("TypeName=" + dataStore.getTypeNames()[0]);

        // 创建Schema
        SimpleFeatureType schema = buildFeatureType();

        // 设置Schema（相当于创建表结构）
        dataStore.createSchema(schema);

        // 构建要素集合(数据)
        ListFeatureCollection featureCollection = buildFeatureCollection(schema);

        // 将特征添加到Shapefile
        writerFeatureCollection(featureCollection, dataStore);

        // 关闭数据存储
        dataStore.dispose();
    }

    /**
     * 创建 Shapefile 文件
     */
    @Test
    public void Writer_shp_test2() throws IOException {
        URL shp_URL = new File(Paths.get("target", "newShapefile2.shp").toString()).toURI().toURL();

        // 创建ShapefileDataStore
        ShapefileDataStore dataStore = new ShapefileDataStore(shp_URL);

        dataStore.setCharset(StandardCharsets.UTF_8);
        dataStore.setIndexed(true);

        SimpleFeatureType schema = buildFeatureType();

        dataStore.createSchema(schema);

        writerFeature(buildFeatureCollection(schema), dataStore);

        dataStore.dispose();
    }

    /**
     * 创建 Shapefile 文件 带 Transaction 事务处理
     */
    @Test
    public void Writer_shp_test3() throws IOException {
        // shapefile 文件路径
        String shapefilePath = TargetPathUtil.createTempFile("output", "newShapefile3.shp");

        // 创建空 shapefile(仅包含属性结构)
        // createEmpty_shapefile(shapefilePath);

        // 写数据到 shapefile文件
        writerData_shapefile(shapefilePath);
    }

    /**
     * 使用 SimpleFeatureStore 写入 shp文件
     */
    private void writerData_shapefile(String shapefilePath) throws IOException {
        // shape中feature定义
        Map<String, Object> params = new HashMap<>();
        params.put("url", new File(shapefilePath).toURI().toURL());
        DataStore dataStore = DataStoreFinder.getDataStore(params);
        String typeName = dataStore.getTypeNames()[0]; // 值为：1_shapefile  文件名

        SimpleFeatureSource featureSource = dataStore.getFeatureSource(typeName);
        // SimpleFeatureType schema = featureSource.getSchema();

        SimpleFeatureType schema = buildFeatureType();
        dataStore.createSchema(schema);

        // 事务处理
        Transaction transaction = new DefaultTransaction("create");

        if (featureSource instanceof SimpleFeatureStore) {
            SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;

            // 创建要素集合
            SimpleFeatureCollection collection = buildFeatureCollection(schema);
            featureStore.setTransaction(transaction);
            try {
                // 写入shape中
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


    /**
     * 创建空 shapefile
     */
    private void createEmpty_shapefile(String shapefilePath) throws IOException {
        FileDataStoreFactorySpi factory = new ShapefileDataStoreFactory();

        // 新建文件
        File file = new File(shapefilePath);
        // datastore
        Map<String, Object> params = new HashMap<>();
        params.put("url", file.toURI().toURL());
        params.put("create spatial index", Boolean.TRUE);
        DataStore dataStore = factory.createNewDataStore(params);

        SimpleFeatureType featureType = buildFeatureType();
        // 创建feature定义到shape
        dataStore.createSchema(featureType);
    }


}

