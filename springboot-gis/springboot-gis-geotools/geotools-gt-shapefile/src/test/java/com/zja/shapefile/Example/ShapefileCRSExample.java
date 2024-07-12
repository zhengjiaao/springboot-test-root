/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-20 15:21
 * @Since:
 */
package com.zja.shapefile.Example;

import com.zja.shapefile.util.ResourceUtil;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.io.File;

/**
 * 获取 Shapefile 的坐标参考系统（CRS）
 *
 * @author: zhengja
 * @since: 2023/10/20 15:21
 */
public class ShapefileCRSExample {
    public static void main(String[] args) {
        String shapefilePath = ResourceUtil.getResourceFilePath("310000_full/310000_full.shp");
        try {
            // 打开 Shapefile 数据源
            File shapefile = new File(shapefilePath);
            ShapefileDataStore dataStore = new ShapefileDataStore(shapefile.toURI().toURL());

            // 获取 Shapefile 的 FeatureCollection
            SimpleFeatureCollection featureCollection = dataStore.getFeatureSource().getFeatures();

            // 获取 Shapefile 的 CRS
            CoordinateReferenceSystem crs = featureCollection.getSchema().getCoordinateReferenceSystem();

            System.out.println("Shapefile CRS: " + crs);

            // 关闭数据源
            dataStore.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
