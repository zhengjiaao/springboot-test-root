package com.zja.shapefile.SpatialAnalysis;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 坐标系转换(重投影)：使用GeoTools实现重投影（从一个坐标参考系统转换到另一个）
 *
 * @Author: zhengja
 * @Date: 2024-07-05 17:10
 */
public class CoordinateTransformTest {

    String shp_path = "D:\\temp\\shp\\";

    /**
     * 重投影（从一个坐标参考系统转换到另一个）
     * <p>
     * 例如，从WGS84 EPSG:4326 转换到 EPSG:3857，实现坐标系单位：度-->米，最后将转换后的特征写入到一个新的Shapefile中。
     */
    @Test
    public void test() throws IOException, FactoryException {

        // File inputFile = new File("path/to/input/shapefile.shp");
        // File outputFile = new File("path/to/output/reprojected_shapefile.shp");

        File inputFile = new File(Paths.get(shp_path, "test", "园区及公司图层.shp").toString());
        File outputFile = new File(Paths.get(shp_path, "test", "园区及公司图层_重投影_3857.shp").toString());

        // 读取Shapefile
        SimpleFeatureSource features = getFeatureSource(inputFile);
        SimpleFeatureType schema = features.getSchema();

        // 获取原始CRS和目标CRS
        CoordinateReferenceSystem sourceCRS = features.getSchema().getCoordinateReferenceSystem();
        // CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:4490"); // 国家2000
        CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:3857"); // EPSG:3857 实现坐标系，单位：度-->米

        // 创建转换器
        MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS, true);

        // 转换特征并写入新Shapefile
        List<SimpleFeature> simpleFeatures = coordinateTransform(features, transform);

        // 写入新的Shapefile
        writeShapefile(schema, new ListFeatureCollection(schema, simpleFeatures), outputFile, targetCRS);
    }

    private static SimpleFeatureSource getFeatureSource(File file) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("url", file.toURI().toURL());

        DataStore dataStore = DataStoreFinder.getDataStore(map);
        String typeName = dataStore.getTypeNames()[0];
        return dataStore.getFeatureSource(typeName);
    }

    // 重投影(转换坐标系)
    private static List<SimpleFeature> coordinateTransform(SimpleFeatureSource source, MathTransform transform) throws IOException {
        List<SimpleFeature> features = new ArrayList<>();

        try (SimpleFeatureIterator iterator = source.getFeatures().features()) {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                Geometry geom = (Geometry) feature.getDefaultGeometry();
                Geometry reprojected = JTS.transform(geom, transform);
                feature.setDefaultGeometry(reprojected);

                SimpleFeature copy = SimpleFeatureBuilder.copy(feature);
                features.add(copy);
            }
        } catch (TransformException e) {
            throw new RuntimeException(e);
        }

        return features;
    }

    private static void writeShapefile(SimpleFeatureType schema, SimpleFeatureCollection features, File newFile, CoordinateReferenceSystem targetCRS) throws IOException {
        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();

        Map<String, Serializable> params = new HashMap<>();
        params.put("url", newFile.toURI().toURL());
        params.put("create spatial index", Boolean.TRUE);
        params.put("charset", "UTF-8"); // 设置编码为UTF-8，避免属性值乱码

        ShapefileDataStore newDataStore = (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);

        // 设置输出Shapefile的坐标系
        schema = SimpleFeatureTypeBuilder.retype(schema, targetCRS);
        newDataStore.createSchema(schema);

        Transaction transaction = new DefaultTransaction("create");

        String typeName = newDataStore.getTypeNames()[0];
        SimpleFeatureSource featureSource = newDataStore.getFeatureSource(typeName);

        if (featureSource instanceof SimpleFeatureStore) {
            SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
            featureStore.setTransaction(transaction);
            try {
                featureStore.addFeatures(features);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw new IOException("Error occurred while storing features.", e);
            } finally {
                transaction.close();
            }
        } else {
            throw new IOException(typeName + " does not support read/write access");
        }
        newDataStore.dispose();
    }
}
