/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-20 15:13
 * @Since:
 */
package com.zja.shapefile.Example;

import com.zja.shapefile.util.ResourceUtil;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import java.io.File;

/**
 * Shapefile 与 GeoTools 的 CRS 功能集成，进行投影操作
 *
 * @author: zhengja
 * @since: 2023/10/20 15:13
 */
public class ProjectionExample {
    public static void main(String[] args) {
        String shapefilePath = ResourceUtil.getResourceFilePath("310000_full/310000_full.shp");  // 替换为输入的Shapefile文件路径
        String sourceCRSCode = "EPSG:4326"; // 替换为源投影的EPSG代码
        String targetCRSCode = "EPSG:3857"; // 替换为目标投影的EPSG代码

        try {
            // 打开 Shapefile 数据源
            File shapefile = new File(shapefilePath);
            ShapefileDataStore dataStore = new ShapefileDataStore(shapefile.toURI().toURL());

            // 获取 Shapefile 的 FeatureCollection
            SimpleFeatureType featureType = dataStore.getSchema();
            SimpleFeatureCollection featureCollection = dataStore.getFeatureSource().getFeatures();

            // 获取源CRS和目标CRS
            // 获取 Shapefile 的 CRS
            CoordinateReferenceSystem sourceCRS = featureCollection.getSchema().getCoordinateReferenceSystem();
//            CoordinateReferenceSystem sourceCRS = CRS.decode(sourceCRSCode);  //报错：Latitude 121°27.4'N is too close to a pole.
            CoordinateReferenceSystem targetCRS = CRS.decode(targetCRSCode);

            // 创建坐标转换
            MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS, true);

            // 遍历 FeatureCollection 进行投影转换
            try (SimpleFeatureIterator iterator = featureCollection.features()) {
                while (iterator.hasNext()) {
                    SimpleFeature feature = iterator.next();
                    Geometry geometry = (Geometry) feature.getDefaultGeometry();

                    // 进行投影转换
                    Geometry projectedGeometry = JTS.transform(geometry, transform);

                    // 在此处处理投影后的几何对象
                    // ...

                    System.out.println("Projected Geometry: " + projectedGeometry);
                }
            }

            // 关闭数据源
            dataStore.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
