/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-19 10:43
 * @Since:
 */
package com.zja.shapefile.toGeoJSON;

import com.zja.shapefile.util.ResourceUtil;
import com.zja.shapefile.util.TargetPathUtil;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.geojson.feature.FeatureJSON;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Shapefile To GeoJSON
 *
 * @author: zhengja
 * @since: 2023/10/19 10:43
 */
public class ShapefileToGeoJSONExample {

    public static void main(String[] args) {
//        String shapefilePath = "path/to/your/shapefile.shp"; // 替换为实际的Shapefile路径
//        String geojsonPath = "path/to/your/output.geojson"; // 替换为输出的GeoJSON文件路径

        String shapefilePath = ResourceUtil.getResourceFilePath("310000_full/310000_full.shp"); // 替换为实际的Shapefile路径
        String geojsonPath = TargetPathUtil.getTempFilePath("310000_full.json"); // 替换为输出的GeoJSON文件路径

        try {
            // 加载Shapefile数据源
            File shapefile = new File(shapefilePath);
            Map<String, Object> shapefileMap = new HashMap<>();
            shapefileMap.put("url", shapefile.toURI().toURL());
            DataStore dataStore = DataStoreFinder.getDataStore(shapefileMap);

            // 获取默认图层(LayerName)
            String typeName = dataStore.getTypeNames()[0];
            FeatureSource<?, ?> featureSource = dataStore.getFeatureSource(typeName);
            SimpleFeatureCollection features = (SimpleFeatureCollection) featureSource.getFeatures();

            // 创建GeoJSON文件
            File geojsonFile = new File(geojsonPath);
            OutputStream outputStream = new FileOutputStream(geojsonFile);

            // 将Shapefile特征集转换为GeoJSON
            FeatureJSON featureJSON = new FeatureJSON();
            featureJSON.writeFeatureCollection(features, outputStream);

            outputStream.flush();
            outputStream.close();

            System.out.println("Shapefile converted to GeoJSON successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
