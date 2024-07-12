package com.zja.shapefile.util;

import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTS;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2024-07-09 15:40
 */
public class ShapefileDataStoreUtil {

    /**
     * 写入矢量文件 shp
     *
     * @param newFeatures
     * @param outputFile
     * @throws IOException
     */
    public static void write(List<SimpleFeature> newFeatures, File outputFile) throws IOException {
        if (newFeatures.isEmpty()) {
            System.out.println("No features to write.");
            return;
        }
        // 创建新的ShapefileDataStore
        ShapefileDataStore newDataStore = new ShapefileDataStore(outputFile.toURI().toURL());
        newDataStore.setCharset(StandardCharsets.UTF_8);

        // 设置新的Shapefile的Schema
        SimpleFeatureType schema = newFeatures.get(0).getFeatureType();
        newDataStore.createSchema(schema);

        // 获取SimpleFeatureSource用于写入数据
        SimpleFeatureSource featureSource = newDataStore.getFeatureSource(newDataStore.getTypeNames()[0]);
        SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;

        // 开始写入数据
        SimpleFeatureCollection collection = new ListFeatureCollection(schema, newFeatures);
        featureStore.addFeatures(collection);

        newDataStore.dispose();
    }

    public static List<SimpleFeature> coordinateTransform(SimpleFeatureSource source, MathTransform transform) throws IOException {
        return coordinateTransformAndBufferDistance(source, transform, 0.0);
    }

    public static List<SimpleFeature> coordinateTransformAndBuffer(SimpleFeatureSource source, MathTransform transform, double bufferDistance) throws IOException {
        return coordinateTransformAndBufferDistance(source, transform, bufferDistance);
    }

    public static List<SimpleFeature> buffer(SimpleFeatureSource source, double bufferDistance) throws IOException {
        return coordinateTransformAndBufferDistance(source, null, bufferDistance);
    }

    private static List<SimpleFeature> coordinateTransformAndBufferDistance(SimpleFeatureSource source, MathTransform transform, double bufferDistance) throws IOException {
        List<SimpleFeature> features = new ArrayList<>();

        try (SimpleFeatureIterator iterator = source.getFeatures().features()) {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                Geometry geom = (Geometry) feature.getDefaultGeometry();
                if (geom != null) {
                    if (transform != null && bufferDistance != 0.0) {
                        Geometry reprojected = JTS.transform(geom, transform); // 重投影
                        Geometry buffer = reprojected.buffer(bufferDistance);  // 缓冲区
                        feature.setDefaultGeometry(buffer);
                    } else if (transform != null) {
                        Geometry reprojected = JTS.transform(geom, transform); // 重投影
                        feature.setDefaultGeometry(reprojected);
                    } else if (bufferDistance != 0.0) {
                        Geometry buffer = geom.buffer(bufferDistance);  // 缓冲区
                        feature.setDefaultGeometry(buffer);
                    } else {
                        throw new RuntimeException("[transform is null] and [bufferDistance is 0.0]");
                    }
                }
                features.add(SimpleFeatureBuilder.copy(feature));
            }
        } catch (TransformException e) {
            throw new RuntimeException(e);
        }

        return features;
    }

}
