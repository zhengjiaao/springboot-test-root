package com.zja.shapefile.SpatialAnalysis;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * 矢量分析
 *
 * @Author: zhengja
 * @Date: 2024-07-05 14:38
 */
public class SpatialAnalysisTest {

    String shp_path = "D:\\temp\\shp\\";

    @Test
    public void ECQL_toFilter_test() throws IOException, CQLException {

        String vectorFile = Paths.get(shp_path, "test", "园区及公司图层.shp").toString();

        // 假设Shapefile路径
        // File shapefile = new File("path/to/your/shapefile.shp");
        File shapefile = new File(vectorFile);

        // 设置数据源参数
        Map<String, Object> params = new HashMap<>();
        URL url = shapefile.toURI().toURL();
        params.put("url", url);

        // 获取数据存储
        DataStore dataStore = DataStoreFinder.getDataStore(params);
        String typeName = dataStore.getTypeNames()[0];

        // 获取要素源
        SimpleFeatureSource featureSource = dataStore.getFeatureSource(typeName);

        // 定义过滤条件，例如筛选属性 人口("POPULATION")大于10000的城市
        // Filter filter = ECQL.toFilter("POPULATION > 10000");

        // 定义过滤条件：筛选属性 type='公司'
        Filter filter = ECQL.toFilter("type = '公司'");

        // 应用过滤条件
        SimpleFeatureCollection filteredFeatures = featureSource.getFeatures(filter);

        // 获取要素类型，以便了解属性名
        SimpleFeatureType featureType = featureSource.getSchema();

        // 遍历并打印符合条件的要素属性
        try (SimpleFeatureIterator iterator = filteredFeatures.features()) {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                System.out.println(feature.getID() + ": " + feature.getAttribute("name"));
            }
        }
    }

    /**
     * 设置坐标参考系统：从已存在的数据源中获取坐标系
     */
    @Test
    public void get_CoordinateReferenceSystem_test() throws IOException {
        String vectorFile = Paths.get(shp_path, "test", "园区及公司图层.shp").toString();

        // 读取Shapefile
        // File inputFile = new File("path/to/input/shapefile.shp");
        File inputFile = new File(vectorFile);
        Map<String, Object> params = new HashMap<>();
        params.put("url", inputFile.toURI().toURL());

        DataStore dataStore = DataStoreFinder.getDataStore(params);
        String typeName = dataStore.getTypeNames()[0];

        // 获取并打印坐标参考系统
        FeatureSource<SimpleFeatureType, SimpleFeature> source = dataStore.getFeatureSource(typeName);
        SimpleFeatureType schema = source.getSchema();
        CoordinateReferenceSystem crs = schema.getCoordinateReferenceSystem();
        System.out.println("Input Shapefile's CRS: " + crs.toWKT());

        // 记得关闭数据存储
        dataStore.dispose();

        // 创建并写入新的Shapefile
        writeNewShapefileWithCRS("target/new_shapefile2.shp", crs);
    }

    /**
     * 设置坐标系：创建新的坐标系
     */
    @Test
    public void creat_CoordinateReferenceSystem_test() throws IOException {
        DefaultGeographicCRS wgs84CRS = DefaultGeographicCRS.WGS84;
        // 创建并写入新的Shapefile
        writeNewShapefileWithCRS("target/new_shapefile3.shp", wgs84CRS);
    }

    private static void writeNewShapefileWithCRS(String outputFile, CoordinateReferenceSystem crs) throws IOException {
        // 构建新的FeatureType
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("MyPointFeature");
        builder.setCRS(crs); // 使用从输入Shapefile获取的CRS
        builder.add("the_geom", Point.class);
        builder.add("name", String.class);
        SimpleFeatureType featureType = builder.buildFeatureType();

        // 创建数据存储并写入数据（此处仅为示例，未实际添加特征）
        File newShapefile = new File(outputFile);
        Map<String, Object> createParams = new HashMap<>();
        createParams.put("url", newShapefile.toURI().toURL());
        DataStore newDataStore = DataStoreFinder.getDataStore(createParams);
        newDataStore.createSchema(featureType);
        newDataStore.dispose(); // 创建完Schema后关闭数据存储

        System.out.println("New Shapefile with CRS: " + crs.toWKT() + " created at: " + outputFile);
    }

}
