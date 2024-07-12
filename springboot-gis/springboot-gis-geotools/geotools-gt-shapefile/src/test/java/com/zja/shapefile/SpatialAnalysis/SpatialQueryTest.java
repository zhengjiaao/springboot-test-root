package com.zja.shapefile.SpatialAnalysis;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 空间查询
 * <p>
 * 1.基础的属性查询
 * 2.基于空间关系的查询
 * 3.组合查询
 * 4.使用SQL查询
 *
 * @Author: zhengja
 * @Date: 2024-07-08 9:53
 */
public class SpatialQueryTest {

    /**
     * 基础的属性查询
     */
    @Test
    public void query() throws IOException {
        // 指定Shapefile文件路径
        File dataFile = new File("path/to/your/shapefile.shp");

        // 设置参数以打开数据源
        Map<String, Object> params = new HashMap<>();
        params.put("url", dataFile.toURI().toURL());

        // 打开数据源
        DataStore dataStore = DataStoreFinder.getDataStore(params);

        // 获取数据集名称
        String typeName = dataStore.getTypeNames()[0];

        // 获取FeatureSource
        SimpleFeatureSource featureSource = dataStore.getFeatureSource(typeName);

        // 创建FilterFactory2实例
        FilterFactory2 filterFactory = CommonFactoryFinder.getFilterFactory2(null);

        // 构建查询条件，例如选择所有属性名为"name"且值为"example"的要素
        Filter filter = filterFactory.equals(filterFactory.property("name"), filterFactory.literal("example"));

        // 创建查询对象
        Query query = new Query(typeName, filter);

        // 执行查询
        SimpleFeatureCollection features = featureSource.getFeatures(query);

        // 遍历查询结果
        try (SimpleFeatureIterator iterator = features.features()) {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                System.out.println(feature);
            }
        }

        // 关闭数据源
        dataStore.dispose();
    }

    @Test
    public void query21() throws IOException, CQLException {
        // 指定Shapefile文件路径
        File dataFile = new File("path/to/your/shapefile.shp");

        // 设置参数以打开数据源
        Map<String, Object> params = new HashMap<>();
        params.put("url", dataFile.toURI().toURL());

        // 打开数据源
        DataStore dataStore = DataStoreFinder.getDataStore(params);

        // 获取数据集名称
        String typeName = dataStore.getTypeNames()[0];

        // 获取FeatureSource
        SimpleFeatureSource featureSource = dataStore.getFeatureSource(typeName);

        // 创建FilterFactory2实例
        FilterFactory2 filterFactory = CommonFactoryFinder.getFilterFactory2(null);

        // 构建查询条件，例如选择所有属性名为"name"且值为"example"的要素
        Filter filter = filterFactory.equals(
                filterFactory.property("name"),
                filterFactory.literal("example")
        );

        // 或者使用ECQL（Elastic Common Query Language）构建过滤器
        Filter ecqlFilter = ECQL.toFilter("name = 'example'");

        // 创建Query对象
        Query query = new Query(typeName);

        // 设置过滤器
        query.setFilter(filter); // 使用filterFactory构建的过滤器
        // 或者
        // query.setFilter(ecqlFilter); // 使用ECQL构建的过滤器

        // 执行查询
        SimpleFeatureCollection features = featureSource.getFeatures(query);

        // 遍历查询结果
        try (SimpleFeatureIterator iterator = features.features()) {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                System.out.println(feature);
            }
        }

        // 关闭数据源
        dataStore.dispose();
    }

    /**
     * 基于空间关系的查询: 创建一个空间过滤器，用于查询与给定点相交的要素。
     * <p>
     * 假设我们要找出所有与给定点相交的要素：
     */
    @Test
    public void query2() throws IOException, TransformException, FactoryException {

        // 指定Shapefile文件路径
        File dataFile = new File("path/to/your/shapefile.shp");

        // 设置参数以打开数据源
        Map<String, Object> params = new HashMap<>();
        params.put("url", dataFile.toURI().toURL());

        // 打开数据源
        DataStore dataStore = DataStoreFinder.getDataStore(params);

        // 获取数据集名称
        String typeName = dataStore.getTypeNames()[0];

        // 获取FeatureSource
        SimpleFeatureSource featureSource = dataStore.getFeatureSource(typeName);

        // 创建FilterFactory2实例
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

        // ... 上面的代码省略 ...

        // 创建点的坐标
        double x = 10.0;
        double y = 20.0;
        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate(x, y));

        // 设置坐标参考系统
        CoordinateReferenceSystem targetCRS = featureSource.getBounds().getCoordinateReferenceSystem();

        // 获取坐标变换
        CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:4326"); // WGS84 单位：度
        MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);

        // 将点转换到数据集的坐标系统

        Geometry geom = JTS.transform(point, transform);

        // 创建空间过滤器
        Filter filter = ff.intersects(ff.property("the_geom"), ff.literal(geom));

        // 创建查询
        Query query = new Query(typeName, filter);

        // 执行查询
        SimpleFeatureCollection features = featureSource.getFeatures(query);

        // 遍历查询结果
        try (SimpleFeatureIterator iterator = features.features()) {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                System.out.println(feature);
            }
        }
    }

    /**
     * 组合查询: 创建一个组合过滤器，将两个过滤器组合在一起。结合属性和空间条件进行查询.
     */
    @Test
    public void query3() throws IOException, FactoryException, TransformException {
        // 指定Shapefile文件路径
        File dataFile = new File("path/to/your/shapefile.shp");

        // 设置参数以打开数据源
        Map<String, Object> params = new HashMap<>();
        params.put("url", dataFile.toURI().toURL());

        // 打开数据源
        DataStore dataStore = DataStoreFinder.getDataStore(params);

        // 获取数据集名称
        String typeName = dataStore.getTypeNames()[0];

        // 获取FeatureSource
        SimpleFeatureSource featureSource = dataStore.getFeatureSource(typeName);

        // 创建FilterFactory2实例
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

        // ... 上面的代码省略 ...

        // 创建点的坐标
        double x = 10.0;
        double y = 20.0;
        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate(x, y));

        // 设置坐标参考系统
        CoordinateReferenceSystem targetCRS = featureSource.getBounds().getCoordinateReferenceSystem();

        // 获取坐标变换
        CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:4326"); // WGS84 单位：度
        MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);

        // 将点转换到数据集的坐标系统

        Geometry geom = JTS.transform(point, transform);

        // 创建属性过滤器
        Filter nameFilter = ff.equals(ff.property("name"), ff.literal("example"));

        // 创建空间过滤器
        Filter intersectsFilter = ff.intersects(ff.property("the_geom"), ff.literal(geom));

        // 组合过滤器
        Filter combinedFilter = ff.and(nameFilter, intersectsFilter);

        // 创建查询
        Query query = new Query(typeName, combinedFilter);

        // 执行查询
        SimpleFeatureCollection features = featureSource.getFeatures(query);

        // 遍历查询结果
        try (SimpleFeatureIterator iterator = features.features()) {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                System.out.println(feature);
            }
        }
    }

    @Test
    public void query32() throws IOException, CQLException {
        // 指定Shapefile文件路径
        File dataFile = new File("path/to/your/shapefile.shp");

        // 设置参数以打开数据源
        Map<String, Object> params = new HashMap<>();
        params.put("url", dataFile.toURI().toURL());

        // 打开数据源
        DataStore dataStore = DataStoreFinder.getDataStore(params);

        // 获取数据集名称
        String typeName = dataStore.getTypeNames()[0];

        // 获取FeatureSource
        SimpleFeatureSource featureSource = dataStore.getFeatureSource(typeName);

        // 创建FilterFactory2实例
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

        // ... 上面的代码省略 ...

        // 创建FilterFactory2实例
        FilterFactory2 filterFactory = CommonFactoryFinder.getFilterFactory2(null);

        // 构建查询条件，例如选择所有属性名为"name"且值为"example"的要素
        Filter filter = filterFactory.equals(
                filterFactory.property("name"),
                filterFactory.literal("example")
        );

        // 或者使用ECQL（Elastic Common Query Language）构建过滤器
        Filter ecqlFilter = ECQL.toFilter("name = 'example'");

        // 创建Query对象
        Query query = new Query(typeName);

        // 设置过滤器
        query.setFilter(filter); // 使用filterFactory构建的过滤器
        // 或者
        query.setFilter(ecqlFilter); // 使用ECQL构建的过滤器

        // 执行查询
        // SimpleFeatureSource featureSource = dataStore.getFeatureSource(typeName);
        SimpleFeatureCollection features = featureSource.getFeatures(query);

        // 遍历查询结果
        try (SimpleFeatureIterator iterator = features.features()) {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                System.out.println(feature);
            }
        }
    }

    /**
     * SQL查询: 使用SQL语句进行查询。
     */
    @Test
    public void query4() throws IOException {
        // 指定Shapefile文件路径
        File dataFile = new File("path/to/your/shapefile.shp");

        // 设置参数以打开数据源
        Map<String, Object> params = new HashMap<>();
        params.put("url", dataFile.toURI().toURL());

        // 打开数据源
        DataStore dataStore = DataStoreFinder.getDataStore(params);

        // 获取数据集名称
        String typeName = dataStore.getTypeNames()[0];

        // 获取FeatureSource
        SimpleFeatureSource featureSource = dataStore.getFeatureSource(typeName);

        // 创建FilterFactory2实例
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

        // ... 上面的代码省略 ...

        // 创建SQL查询
        String sql = "SELECT * FROM " + typeName + " WHERE name='example' AND the_geom && 'POINT(10 20)'";

        // 创建查询
        Query query = new Query(typeName, Filter.INCLUDE);
        // query.setStatement(sql); // todo 不存在的方法

        // 执行查询
        SimpleFeatureCollection features = featureSource.getFeatures(query);

        // 遍历查询结果
        try (SimpleFeatureIterator iterator = features.features()) {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                System.out.println(feature);
            }
        }
    }
}
