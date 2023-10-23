/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-19 10:55
 * @Since:
 */
package com.zja.shapefile.toGeoJSON;

import com.zja.shapefile.util.ResourceUtil;
import com.zja.shapefile.util.TargetPathUtil;
import org.geotools.data.DataUtilities;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.MultiPolygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;

import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * todo 未成功：geometry 转换后数据不全，丢失了一部分。
 * <p>
 * GeoJSON To Shapefile
 *
 * @author: zhengja
 * @since: 2023/10/19 10:55
 */
@Deprecated
public class GeoJSONToShapefileExample2 {

    @Test
    public void test_GeoJSONToShapefileConverter() {
        String geojsonPath = ResourceUtil.getResourceFilePath("310000_full.json"); // 替换为输入的GeoJSON文件路径
        String shapefilePath = TargetPathUtil.getTempFilePath("310000_full"); // 替换为输出的Shapefile文件路径

        try {
            // 加载GeoJSON文件
            File geojsonFile = new File(geojsonPath);
            JSONParser parser = new JSONParser();
            JSONObject geojsonObject = (JSONObject) parser.parse(new FileReader(geojsonFile));
            JSONArray featuresArray = (JSONArray) geojsonObject.get("features");

            // 获取第一个特征的属性
            JSONObject firstFeature = (JSONObject) featuresArray.get(0);

            JSONObject firstFeatureProperties = (JSONObject) firstFeature.get("properties");

            // 创建Shapefile数据存储
            File shapefile = new File(shapefilePath + ".shp");
            Map<String, Object> shapefileMap = new HashMap<>();
            shapefileMap.put(ShapefileDataStoreFactory.URLP.key, shapefile.toURI().toURL());
            //shapefileMap.put("create spatial index", Boolean.TRUE); // 添加参数以创建空间索引
            ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
            ShapefileDataStore shapeDataStore = (ShapefileDataStore) dataStoreFactory.createNewDataStore(shapefileMap);

            // 设置编码以支持中文字符
            shapeDataStore.setCharset(StandardCharsets.UTF_8);

            // 创建Shapefile的FeatureType
            SimpleFeatureType featureType = createFeatureType(firstFeatureProperties);

            //todo 测试: 校验存在几何属性
            GeometryDescriptor geometryDescriptor = featureType.getGeometryDescriptor();
            if (geometryDescriptor != null) {
                Class<?> binding = geometryDescriptor.getType().getBinding();
                // 处理绑定类型
                System.out.println("有几何属性");
            } else {
                // 没有几何属性
                System.out.println("没有几何属性");
            }

            // 创建模式并获取SimpleFeatureStore
            shapeDataStore.createSchema(featureType);
            // 获取Shapefile的FeatureStore
            String typeName = shapeDataStore.getTypeNames()[0];
            SimpleFeatureSource featureSource = shapeDataStore.getFeatureSource(typeName);
            SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;

            // 开始事务
            Transaction transaction = featureStore.getTransaction();
            featureStore.setTransaction(transaction);

            // 将GeoJSON中的特征转换为Shapefile的Feature并写入
            FeatureJSON featureJSON = new FeatureJSON(new GeometryJSON());

            List<SimpleFeature> features = new ArrayList<>();
            for (Object featureObj : featuresArray) {
                System.out.println(featureObj);
                Map<?, ?> featureObjMap = (Map<?, ?>) featureObj;
                SimpleFeature feature = featureJSON.readFeature(featureObjMap.toString());
                features.add(feature);
            }
            featureStore.addFeatures(DataUtilities.collection(features));

            transaction.commit();
            transaction.close();

            System.out.println("GeoJSON converted to Shapefile successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private static SimpleFeatureType createFeatureType(Map<String, Object> attributes) {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("Shapefile");
        builder.setCRS(DefaultGeographicCRS.WGS84);

        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            String attributeName = entry.getKey();
            Object attributeValue = entry.getValue();
            System.out.println(attributeName + "=" + attributeValue);
            Class<?> attributeClass = attributeValue != null ? attributeValue.getClass() : String.class;
            builder.add(attributeName, attributeClass);
        }

        // 添加几何属性 一个 Shapefile 中，每个图层只能包含一种类型的几何对象，例如点、线或多边形。
        builder.add("geometry", MultiPolygon.class);

        return builder.buildFeatureType();
    }*/

    private static SimpleFeatureType createFeatureType(JSONObject attributes) {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("Shapefile");
        builder.setCRS(DefaultGeographicCRS.WGS84);

        // 添加其他属性
        for (Object attributeObj : attributes.keySet()) {
            String attributeName = (String) attributeObj;
            builder.add(attributeName, String.class);
        }

        // 添加几何属性 一个 Shapefile 中，每个图层只能包含一种类型的几何对象，例如点、线或多边形。
        //builder.add("geometry", Geometry.class); //报错：没有几何属性，需要把Geometry替换为具体的集合属性类型：点、线、面
        builder.add("geometry", MultiPolygon.class);

        return builder.buildFeatureType();
    }
}
