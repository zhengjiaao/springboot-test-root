/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-11 12:40
 * @Since:
 */
package com.zja.shapefile.toGeoJSON;

import com.zja.shapefile.util.ResourceUtil;
import com.zja.shapefile.util.TargetPathUtil;
import org.geotools.data.Transaction;
import org.geotools.data.geojson.GeoJSONReader;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.CRS;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.MultiPolygon;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * todo 未成功：geometry 转换后数据不全，丢失了一部分。
 *
 * @author: zhengja
 * @since: 2023/10/11 12:40
 */
@Deprecated
public class GeoJSONToShapefileExample3 {

    @Test
    public void test_GeoJSONToShapefileConverter() throws IOException, FactoryException {
        String geoJSONFilePath = ResourceUtil.getResourceFilePath("310000_full.json");

        String shapefilePath = TargetPathUtil.getTempFilePath("310000_full");

        geoJSONToShapefileConverter(geoJSONFilePath, shapefilePath);
    }

    private static void geoJSONToShapefileConverter(String geoJSONFilePath, String shapefilePath) throws IOException, FactoryException {
        //读取GeoJSON数据
        GeoJSONReader reader = new GeoJSONReader(Files.newInputStream(Paths.get(geoJSONFilePath)));
        SimpleFeatureCollection featureCollection = reader.getFeatures();
        SimpleFeatureType schema = featureCollection.getSchema();

        List<Property> firstFeatureProperties = getPropertiesFromFirstFeature(featureCollection.features());

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
        // 创建模式并获取SimpleFeatureStore
        shapeDataStore.createSchema(featureType);

        // 获取Shapefile的FeatureStore
        String typeName = shapeDataStore.getTypeNames()[0];
        System.out.println(typeName);
        SimpleFeatureSource featureSource = shapeDataStore.getFeatureSource(typeName);
        SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;

        // 开始事务
        Transaction transaction = featureStore.getTransaction();
        featureStore.setTransaction(transaction);

        //转换存储
        featureStore.addFeatures(featureCollection);

        transaction.commit();
        transaction.close();
    }


    private static SimpleFeatureType createFeatureType(List<Property> firstFeatureProperties) throws FactoryException {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("Shapefile");
//        builder.setCRS(DefaultGeographicCRS.WGS84);
//        builder.setCRS(CRS.decode("EPSG:3857"));
        // 设置 WGS84 坐标系
        String wkt = "GEOGCS[\"WGS84\",DATUM[\"WGS_1984\",SPHEROID[\"WGS 84\",6378137,298.257223563]],PRIMEM[\"Greenwich\",0],UNIT[\"degree\",0.017453292519943295]]";

        builder.setCRS(CRS.parseWKT(wkt));

        // 添加其他属性
        for (Property property : firstFeatureProperties) {
            String propertyName = property.getName().toString();
            builder.add(propertyName, String.class);
        }

        // 添加几何属性 一个 Shapefile 中，每个图层只能包含一种类型的几何对象，例如点、线或多边形。
        //builder.add("geometry", Geometry.class); //报错：没有几何属性，需要把Geometry替换为具体的集合属性类型：点、线、面
//        builder.add("geometry", MultiPolygon.class);
        builder.add("the_geom", MultiPolygon.class);
//        builder.add("the_geom", Polygon.class);

        return builder.buildFeatureType();
    }

    private static List<Property> getPropertiesFromFirstFeature(SimpleFeatureIterator iterator) {
        List<Property> properties = new ArrayList<>();

        try {
            if (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                properties.addAll(feature.getProperties());
            }
        } finally {
            iterator.close();
        }

        return properties;
    }
}
