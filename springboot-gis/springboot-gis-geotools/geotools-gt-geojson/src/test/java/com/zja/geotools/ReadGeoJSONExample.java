/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-11 12:40
 * @Since:
 */
package com.zja.geotools;

import com.zja.geotools.util.ResourceUtil;
import org.geotools.data.geojson.GeoJSONReader;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author: zhengja
 * @since: 2023/10/11 12:40
 */
public class ReadGeoJSONExample {

    @Test
    public void testReadGeoJSONFile() throws IOException {
        // GeoJSON 文件路径
        //String geoJSONFilePath = TargetPathUtil.getTempFilePath("output.geojson");
        String geoJSONFilePath = ResourceUtil.getResourceFilePath("310000_full.json");
        // 读取 GeoJSON 文件
        readGeoJSON2(geoJSONFilePath);
    }

    private static void readGeoJSON2(String filePath) throws IOException {
        GeoJSONReader reader = new GeoJSONReader(Files.newInputStream(Paths.get(filePath)));
        SimpleFeatureCollection featureCollection = reader.getFeatures();
        // 遍历要素进行处理
        SimpleFeatureIterator features = featureCollection.features();
        while (features.hasNext()) {
            SimpleFeature feature = features.next();

            // 获取要素几何类型
            Class<?> binding = feature.getDefaultGeometryProperty().getType().getBinding();
            Object defaultGeometry = feature.getDefaultGeometry();

            //具体的几何类型
            System.out.println(defaultGeometry.toString());

            // 具体的几何类型判断
            if (defaultGeometry instanceof Point) {
                System.out.println(defaultGeometry.toString());
            } else if (defaultGeometry instanceof LineString) {

            } else if (defaultGeometry instanceof Polygon) {

            } else if (defaultGeometry instanceof MultiPoint) {

            } else if (defaultGeometry instanceof MultiLineString) {

            } else if (defaultGeometry instanceof MultiPolygon) {

            }

            // 遍历获取要素属性信息
            Collection<Property> properties = feature.getProperties();
            Iterator<Property> propertyIterator = properties.iterator();
            while (propertyIterator.hasNext()) {
                Property property = propertyIterator.next();
                System.out.println("property:" + property.getName().toString() + ",value:" + property.getValue());
            }

            // 获取指定属性信息
            int attributeCount = feature.getAttributeCount(); //属性个数
            Object attribute_name_value = feature.getAttribute("name"); //获取指定属性值
            System.out.println("要素id:" + feature.getID() + ",属性个数:" + attributeCount + ",name属性值:" + attribute_name_value);

            // 需要注意,获取到的属性集合中包含了几何字段
        }

        // 要素集迭代器用完记得关闭
        features.close();
    }
}
