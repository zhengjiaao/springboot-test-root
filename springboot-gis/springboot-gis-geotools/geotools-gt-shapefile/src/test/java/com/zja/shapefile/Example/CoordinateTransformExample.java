/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-20 14:47
 * @Since:
 */
package com.zja.shapefile.Example;

import com.zja.shapefile.util.ResourceUtil;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import java.io.File;

/**
 * Shapefile 与 GeoTools 的 CRS 功能集成，进行坐标转换
 *
 * @author: zhengja
 * @since: 2023/10/20 14:47
 */
public class CoordinateTransformExample {
    public static void main(String[] args) {
        String shapefilePath = ResourceUtil.getResourceFilePath("310000_full/310000_full.shp");
        String targetCRS_Str = "EPSG:4326"; // 替换为目标投影的EPSG代码

        try {
            // 打开 Shapefile 数据源
            File shapefile = new File(shapefilePath);
            ShapefileDataStore dataStore = new ShapefileDataStore(shapefile.toURI().toURL());

            // 获取 Shapefile 的 FeatureCollection
            SimpleFeatureType featureType = dataStore.getSchema();
            SimpleFeatureCollection featureCollection = dataStore.getFeatureSource().getFeatures();

            // 获取源CRS和目标CRS
            CoordinateReferenceSystem sourceCRS = featureType.getCoordinateReferenceSystem();
            CoordinateReferenceSystem targetCRS = CRS.decode(targetCRS_Str);

            // 创建坐标转换
            MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS, true);

            // 遍历 FeatureCollection 进行坐标转换
            try (SimpleFeatureIterator iterator = featureCollection.features()) {
                while (iterator.hasNext()) {
                    SimpleFeature feature = iterator.next();
                    Geometry geometry = (Geometry) feature.getDefaultGeometry();

                    // 进行坐标转换
                    Geometry transformedGeometry = JTS.transform(geometry, transform);

                    // 获取转换后的坐标
                    Coordinate[] coordinates = transformedGeometry.getCoordinates();
                    for (Coordinate coordinate : coordinates) {
                        System.out.println("Transformed Coordinate: " + coordinate);
                    }
                }
            }

            // 关闭数据源
            dataStore.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
