/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-11 11:08
 * @Since:
 */
package com.zja.gtmain.shapefile;

import com.zja.gtmain.TestUtil;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhengja
 * @since: 2023/10/11 11:08
 */
public class ShapefileReadExample {

    @Test
    public void test() throws IOException {
        // Shapefile 文件路径
        String shapefilePath = TestUtil.getTempFilePath("shapefile", "1_shapefile.shp");
        Map<String, Serializable> params = new HashMap<>();
        params.put(ShapefileDataStoreFactory.URLP.key, new File(shapefilePath).toURI().toURL());
        params.put(ShapefileDataStoreFactory.CREATE_SPATIAL_INDEX.key, true);

        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
        ShapefileDataStore dataStore = (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);

        // 读取 Shapefile

        // 设置字符编码（如果 Shapefile 使用非 UTF-8 编码）
        dataStore.setCharset(Charset.forName("UTF-8"));

        // 获取 Shapefile 的 FeatureCollection
        SimpleFeatureCollection featureCollection = dataStore.getFeatureSource().getFeatures();

        // 获取 FeatureCollection 的特征类型
        SimpleFeatureType featureType = featureCollection.getSchema();

        // 输出 FeatureCollection 的属性信息
        System.out.println("Feature Type: " + featureType.getTypeName());
        System.out.println("Attributes: ");

        // 输出 FeatureCollection 的要素数量
        System.out.println("Number of Features: " + featureCollection.size());

        // 迭代处理每个要素
        try (SimpleFeatureIterator featureIterator = featureCollection.features()) {
            while (featureIterator.hasNext()) {
                SimpleFeature feature = featureIterator.next();

                // 获取要素的几何对象
                Geometry geometry = (Geometry) feature.getDefaultGeometry();

                // 判断几何对象类型
                if (geometry instanceof Point) {
                    Point point = (Point) geometry;
                    Coordinate coordinate = point.getCoordinate();
                    System.out.println("Point: " + coordinate.x + ", " + coordinate.y);
                } else {
                    // 其他几何对象类型的处理
                    // ...
                }

                // 获取要素的属性值
                for (Property property : feature.getProperties()) {
                    String propertyName = property.getName().toString();
                    Object propertyValue = property.getValue();
                    System.out.println(propertyName + ": " + propertyValue);
                }

                // 其他要素处理逻辑
                // ...
            }
        }

    }
}
