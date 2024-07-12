package com.zja.shapefile.SpatialAnalysis;

import org.geotools.data.*;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.operation.union.CascadedPolygonUnion;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.*;

/**
 * 矢量叠加分析
 * <p>
 * 定义：矢量叠加分析是指通过将多个矢量相加来得到一个合成矢量的过程。它是将多个矢量按照一定规则组合成一个矢量的方法。矢量叠加分析是一种应用矢量分析的方法。
 * 研究内容：矢量叠加分析主要关注多个矢量的组合和叠加规则。常见的矢量叠加规则包括平行四边形法则、三角法则和分量法则等，用于确定合成矢量的大小和方向。
 * <p>
 * 常见操作：
 * 1.交集（Intersection）：计算两个或多个矢量数据集之间的交集，生成仅包含同时存在于所有输入数据集中的地理要素的新数据集。
 * 2.联合(并集)（Union）：融合操作将多个矢量数据集合并为一个新的数据集，包含了原始数据集中的所有地理要素。融合操作生成的结果数据集将包含原始数据集的几何和属性信息的组合。
 * 3.差集（Difference）: 差集是将两个矢量数据集相减，生成仅包含原始数据集中不包含在另一个数据集中的矢量。
 * 4.对称差集（Symmetric Difference）：对称差集是将两个矢量数据集相减，并保留结果中不包含在原始数据集中的矢量。
 * 5.融合(合并)（Merge）：合并操作是将多个矢量合并成一个矢量，其中矢量之间的位置关系保持不变。合并操作可以用于将多个矢量合并成一个矢量，以简化数据管理。
 * 6.裁剪（Clip）：裁剪操作通过使用一个多边形边界对一个矢量数据集进行裁剪，生成一个新的数据集，其中只包含在边界内部的地理要素。裁剪操作可以用于提取感兴趣区域内的特定数据。
 * 7.擦除（Erase）：擦除操作是裁剪操作的相反过程。它通过使用一个多边形边界从一个矢量数据集中移除位于边界内部的地理要素，生成一个新的数据集。擦除操作可以用于从数据集中去除特定区域的地理要素。
 *
 * <p>
 * 九交分析：
 * 内部（Inside）：一个对象完全位于另一个对象的内部。
 * 包含（Contains）：一个对象完全包含另一个对象。
 * 相交（Intersects）：两个对象有部分重叠。
 * 接触（Touches）：两个对象在某些位置接触，但没有相交。
 * 重叠（Overlaps）：两个对象部分重叠，但没有完全包含。
 * 离散（Disjoint）：两个对象没有交集，彼此之间没有任何交叉。
 *
 * @Author: zhengja 空间分析
 * @Date: 2024-07-08 10:32
 */
public class VectorOverlayAnalysisTest {

    private static final GeometryFactory geometryFactory = new GeometryFactory();

    private static final String SHP_PATH = "D:\\temp\\shp\\";

    /**
     * 交集分析 - Intersection
     */
    @Test
    public void Intersection_test() throws IOException {
        // String first_shapefile = Paths.get(shp_path, "test", "上海测试中心.shp").toString();
        String first_shapefile = Paths.get(SHP_PATH, "test", "上海测试中心.shp").toString();
        String second_shapefile = Paths.get(SHP_PATH, "test", "上海润和总部园.shp").toString();

        // 创建新的Shapefile路径
        File intersect_outputFile = new File(Paths.get("target", "intersected_3.shp").toString());

        File fileA = new File(first_shapefile);
        File fileB = new File(second_shapefile);

        ShapefileDataStore storeA = new ShapefileDataStore(fileA.toURI().toURL());
        storeA.setCharset(StandardCharsets.UTF_8); // 避免读取属性值乱码
        ShapefileDataStore storeB = new ShapefileDataStore(fileB.toURI().toURL());
        storeB.setCharset(StandardCharsets.UTF_8);

        CoordinateReferenceSystem crsA = storeA.getSchema().getCoordinateReferenceSystem();
        CoordinateReferenceSystem crsB = storeB.getSchema().getCoordinateReferenceSystem();

        System.out.println("坐标系：\ncrsA=\n" + crsA + "\ncrsB=\n" + crsB);
        if (!crsA.equals(crsB)) {
            // 如果CRS不同，需要进行投影变换
            // 这里省略了投影变换的代码
            System.out.println("投影变换：" + crsA + "-->" + crsB);
        }

        SimpleFeatureCollection featuresA = storeA.getFeatureSource().getFeatures();
        SimpleFeatureCollection featuresB = storeB.getFeatureSource().getFeatures();

        List<SimpleFeature> intersectFeatures = new ArrayList<>();

        try (SimpleFeatureIterator iteratorA = featuresA.features(); SimpleFeatureIterator iteratorB = featuresB.features()) {

            while (iteratorA.hasNext()) {
                SimpleFeature featureA = iteratorA.next();
                Geometry geomA = (Geometry) featureA.getDefaultGeometry();

                while (iteratorB.hasNext()) {
                    SimpleFeature featureB = iteratorB.next();
                    Geometry geomB = (Geometry) featureB.getDefaultGeometry();

                    System.out.println(featureA);
                    System.out.println(featureB);

                    if (geomB == null || geomA == null) {
                        continue;
                    }

                    if (geomA.intersects(geomB)) {
                        // 相交分析
                        Geometry intersection = geomA.intersection(geomB);
                        // 多要素相交分析
                        // Geometry intersection = geomA.intersection(geomB).intersection(geomC);
                        if (!intersection.isEmpty()) {
                            System.out.println("相交: 【" + featureA.getAttribute("name") + "】与【" + featureB.getAttribute("name") + "】");
                            SimpleFeature intersectFeature = createFeatureFromGeometry(intersection, featureA);
                            intersectFeatures.add(intersectFeature);
                        }
                    }
                }
            }
        }

        // 处理intersectFeatures列表...
        System.out.println("相交数量:" + intersectFeatures.size());
        if (!intersectFeatures.isEmpty()) {
            for (SimpleFeature feature : intersectFeatures) {
                System.out.println("intersect name=" + feature.getAttribute("name"));

                // 获取 SimpleFeatureType 的描述符列表
                SimpleFeatureType featureType = feature.getFeatureType();
                List<AttributeDescriptor> attributeDescriptors = featureType.getAttributeDescriptors();
                // 遍历所有属性描述符并打印键值对
                for (AttributeDescriptor descriptor : attributeDescriptors) {
                    String attributeName = descriptor.getName().getLocalPart();
                    Object attributeValue = feature.getAttribute(attributeName);
                    System.out.println("intersect attribute " + attributeName + "=" + attributeValue);
                }
            }

            // 输出相交结果到shp文件
            writerData_shapefile(intersectFeatures, intersect_outputFile);
        }
    }

    private static SimpleFeature createFeatureFromGeometry(Geometry intersection, SimpleFeature originalFeature) {
        Geometry geom = (Geometry) originalFeature.getDefaultGeometry();
        GeometryFactory factory = new GeometryFactory(new PrecisionModel(), geom.getSRID());
        Geometry newGeometry = factory.createGeometry(intersection);

        SimpleFeatureType type = originalFeature.getType();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(type);

        // Add the geometry attribute first 首先添加几何体属性
        featureBuilder.add(newGeometry);

        int attributeCount = type.getAttributeCount();
        System.out.println("originalFeature Attribute count: " + attributeCount);

        /*for (int i = 0; i < attributeCount; i++) {
            System.out.println("Attribute " + i + ": " + type.getDescriptor(i).getType().getBinding());
        }*/

        // Add all non-geometry attributes 添加所有非几何体属性
        List<Object> attributes = originalFeature.getAttributes();
        for (int i = 1; i < Math.min(attributeCount, attributes.size()); i++) { // Skip the first attribute which is geometry 跳过作为几何体的第一个属性
            featureBuilder.add(attributes.get(i));
        }

        return featureBuilder.buildFeature(null);
    }

    private static void writerData_shapefile(List<SimpleFeature> intersectFeatures, File outputFile) throws IOException {
        // 创建新的ShapefileDataStore
        ShapefileDataStore outputStore = new ShapefileDataStore(outputFile.toURI().toURL());
        outputStore.setCharset(StandardCharsets.UTF_8);

        // 设置新的Shapefile的Schema
        SimpleFeatureType schema = intersectFeatures.get(0).getFeatureType();
        outputStore.createSchema(schema);

        // 获取SimpleFeatureSource用于写入数据
        SimpleFeatureSource featureSource = outputStore.getFeatureSource(outputStore.getTypeNames()[0]);
        SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
        // 开始写入数据
        SimpleFeatureCollection collection = new ListFeatureCollection(schema, intersectFeatures);
        featureStore.addFeatures(collection);

        outputStore.dispose();
    }

    /**
     * 合并(Merge) 定义: 合并操作是将几何要素的几何部分合并为一个新的几何要素,保留原有的属性信息。
     * <p>
     *     操作：A + B ，把A、B两个shp文件合并成一个新的shp文件
     */
    @Test
    public void Merge_test() throws IOException {

        File fileA = new File(Paths.get(SHP_PATH, "test", "上海润和总部园.shp").toString());
        File fileB = new File(Paths.get(SHP_PATH, "test", "园区及公司图层.shp").toString());
        // 创建新的Shapefile路径
        File merge_outputFile = new File(Paths.get("target", "Merge_1_4.shp").toString());

        ShapefileDataStore storeA = new ShapefileDataStore(fileA.toURI().toURL());
        storeA.setCharset(StandardCharsets.UTF_8); // 避免读取属性值乱码
        ShapefileDataStore storeB = new ShapefileDataStore(fileB.toURI().toURL());
        storeB.setCharset(StandardCharsets.UTF_8);

        CoordinateReferenceSystem crsA = storeA.getSchema().getCoordinateReferenceSystem();
        CoordinateReferenceSystem crsB = storeB.getSchema().getCoordinateReferenceSystem();

        System.out.println("坐标系：\ncrsA=\n" + crsA + "\ncrsB=\n" + crsB);
        if (!crsA.equals(crsB)) {
            // 如果CRS不同，需要进行投影变换
            // 这里省略了投影变换的代码
            System.out.println("投影变换：" + crsA + "-->" + crsB);
        }

        SimpleFeatureCollection featuresA = storeA.getFeatureSource().getFeatures();
        SimpleFeatureCollection featuresB = storeB.getFeatureSource().getFeatures();

        // 合并两个FeatureCollection
        List<SimpleFeature> allFeatures = new ArrayList<>();
        try (SimpleFeatureIterator iteratorA = featuresA.features(); SimpleFeatureIterator iteratorB = featuresB.features()) {
            while (iteratorA.hasNext()) {
                allFeatures.add(iteratorA.next());
            }
            while (iteratorB.hasNext()) {
                allFeatures.add(iteratorB.next());
            }
        }

        // 将Geometry按照面积排序，小的Geometry位于顶层，不至于被压盖到下面，无法通过qgis查看到
        List<SimpleFeature> allSimpleFeatures = sortSmallBeTopFloor(allFeatures);

        SimpleFeatureType schema = featuresA.getSchema();

        // 创建新的ShapefileDataStore，不推荐
        // 注：此方式，不支持设置编码，会遇到属性值乱码
        // FileDataStore newDataStore = FileDataStoreFinder.getDataStore(union_outputFile);
        // newDataStore.createSchema(featureType);

        // 创建新的ShapefileDataStore
        ShapefileDataStore mergeDataStore = new ShapefileDataStore(merge_outputFile.toURI().toURL());
        mergeDataStore.setCharset(StandardCharsets.UTF_8); // 避免写入属性值乱码
        mergeDataStore.createSchema(schema);

        // 将合并后的FeatureCollection写入新的Shapefile
        SimpleFeatureSource featureSource = mergeDataStore.getFeatureSource();
        SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;

        // 使用相同的FeatureType创建新的FeatureCollection

        SimpleFeatureCollection mergedFeatures = new ListFeatureCollection(schema, allSimpleFeatures);
        featureStore.addFeatures(mergedFeatures);

        System.out.println("merged_size=" + allSimpleFeatures.size());
        if (!allSimpleFeatures.isEmpty()) {
            for (SimpleFeature feature : allSimpleFeatures) {
                System.out.println("intersect name=" + feature.getAttribute("name"));
                // 获取 SimpleFeatureType 的描述符列表
                SimpleFeatureType type = feature.getFeatureType();
                List<AttributeDescriptor> attributeDescriptors = type.getAttributeDescriptors();
                // 遍历所有属性描述符并打印键值对
                for (AttributeDescriptor descriptor : attributeDescriptors) {
                    String attributeName = descriptor.getName().getLocalPart();
                    Object attributeValue = feature.getAttribute(attributeName);
                    System.out.println("intersect attribute " + attributeName + "=" + attributeValue);
                }
            }
        }

        // 关闭数据源
        storeA.dispose();
        storeB.dispose();
        mergeDataStore.dispose();
    }

    /**
     * 将Geometry按照面积排序，小的Geometry位于顶层
     */
    private static List<SimpleFeature> sortSmallBeTopFloor(List<SimpleFeature> allFeatures) {
        // 根据几何体的面积排序要素
        // allFeatures.sort(Comparator.comparingDouble(feature -> ((Geometry) feature.getDefaultGeometry()).getArea()));
        allFeatures.sort(Comparator.comparingDouble(feature -> {
            Geometry geom = (Geometry) feature.getDefaultGeometry();
            if (geom != null) {
                return geom.getArea();
            }
            return 0.0;
        }));

        // 反转列表，以确保小的要素位于顶层
        List<SimpleFeature> sortedFeatures = new ArrayList<>(allFeatures);
        Collections.reverse(sortedFeatures);
        return sortedFeatures;
    }

    /**
     * 并集(Union)定义: 并集操作将两个或更多几何要素合并为一个单一的几何要素,包含所有输入要素的点、线和面。
     * <p>
     *     并集操作会合并所有重叠和相邻的几何要素,生成一个包含所有输入几何要素的新几何要素。
     *     A 与 B 的并集 = A + B - (A 与 B 的交集)
     * </p>
     */
    @Test
    public void Union_test() throws IOException {

        // 指定Shapefile文件路径
        File fileA = new File(Paths.get(SHP_PATH, "test", "上海润和总部园.shp").toString());
        File fileB = new File(Paths.get(SHP_PATH, "test", "园区及公司图层.shp").toString());

        // 创建ShapefileDataStore
        ShapefileDataStore dataStore1 = new ShapefileDataStore(fileA.toURI().toURL());
        ShapefileDataStore dataStore2 = new ShapefileDataStore(fileB.toURI().toURL());

        // 获取SimpleFeatureSource
        SimpleFeatureSource featureSource1 = dataStore1.getFeatureSource();
        SimpleFeatureSource featureSource2 = dataStore2.getFeatureSource();

        // 获取特征集合
        SimpleFeatureCollection features1 = featureSource1.getFeatures();
        SimpleFeatureCollection features2 = featureSource2.getFeatures();

        // 将特征集合转换为Geometry列表
        List<Geometry> geometries = new ArrayList<>();
        addGeometriesToList(features1, geometries);
        addGeometriesToList(features2, geometries);

        // 使用CascadedPolygonUnion计算并集
        Geometry unionGeometry = new CascadedPolygonUnion(geometries).union();

        // 输出结果
        System.out.println("Union Geometry: " + unionGeometry);
    }

    private static void addGeometriesToList(SimpleFeatureCollection features, List<Geometry> geometries) {
        try (FeatureIterator<SimpleFeature> iterator = features.features()) {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                Geometry geometry = (Geometry) feature.getDefaultGeometry();
                if (geometry != null) {
                    geometries.add(geometry);
                }
            }
        }
    }

    /**
     * 并集(Union)定义: 并集操作将两个或更多几何要素合并为一个单一的几何要素,包含所有输入要素的点、线和面。
     * <p>
     *     并集操作会合并所有重叠和相邻的几何要素,生成一个包含所有输入几何要素的新几何要素。
     *     A 与 B 的并集 = A + B - (A 与 B 的交集)
     * </p>
     */
    @Test
    public void Union_test2() throws IOException {

        // 指定Shapefile文件路径
        File fileA = new File(Paths.get(SHP_PATH, "test", "上海润和总部园.shp").toString());
        File fileB = new File(Paths.get(SHP_PATH, "test", "园区及公司图层.shp").toString());

        File outputFile = new File(Paths.get("target", "Union_2_1.shp").toString());

        // 创建ShapefileDataStore
        ShapefileDataStore dataStore1 = new ShapefileDataStore(fileA.toURI().toURL());
        ShapefileDataStore dataStore2 = new ShapefileDataStore(fileB.toURI().toURL());
        // 设置读取编码
        dataStore1.setCharset(StandardCharsets.UTF_8);
        dataStore2.setCharset(StandardCharsets.UTF_8);

        SimpleFeatureSource featureSource1 = dataStore1.getFeatureSource();
        SimpleFeatureSource featureSource2 = dataStore2.getFeatureSource();

        SimpleFeatureCollection features1 = featureSource1.getFeatures();
        SimpleFeatureCollection features2 = featureSource2.getFeatures();

        List<Geometry> geometries = new ArrayList<>();
        List<SimpleFeature> features = new ArrayList<>();
        addGeometriesAndFeaturesToList(features1, geometries, features);
        addGeometriesAndFeaturesToList(features2, geometries, features);

        // 使用CascadedPolygonUnion计算并集
        Geometry unionGeometry = new CascadedPolygonUnion(geometries).union();

        // 创建新的ShapefileDataStore
        ShapefileDataStore outputDataStore = new ShapefileDataStore(outputFile.toURI().toURL());
        outputDataStore.setCharset(StandardCharsets.UTF_8);
        outputDataStore.setIndexed(true);
        // 设置Schema
        SimpleFeatureType schema = featureSource1.getSchema();
        outputDataStore.createSchema(schema);

        ContentFeatureSource featureSource = outputDataStore.getFeatureSource();
        SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;

        // 将联合后的几何体和属性添加到新的Shapefile中
        SimpleFeature feature = new SimpleFeatureBuilder(schema).buildFeature(null);
        // 复制属性
        for (AttributeDescriptor attribute : schema.getAttributeDescriptors()) {
            if (!(attribute instanceof GeometryDescriptor)) {
                feature.setAttribute(attribute.getLocalName(), features.get(0).getAttribute(attribute.getLocalName()));
            }
        }
        feature.setDefaultGeometry(unionGeometry);

        // 开始写入数据
        SimpleFeatureCollection collection = new ListFeatureCollection(schema, Collections.singletonList(feature));
        featureStore.addFeatures(collection);

        dataStore1.dispose();
        dataStore2.dispose();
        outputDataStore.dispose();
    }

    private static void addGeometriesAndFeaturesToList(SimpleFeatureCollection features, List<Geometry> geometries, List<SimpleFeature> featureList) {
        try (FeatureIterator<SimpleFeature> iterator = features.features()) {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                Geometry geometry = (Geometry) feature.getDefaultGeometry();
                if (geometry != null) {
                    geometries.add(geometry);
                }
                featureList.add(feature);
            }
        }
    }

    // todo 聚合
    @Test
    public void Aggregate_test() throws IOException {

    }

    /**
     * 差集分析 - Difference
     * <p>
     *     差集分析，即 A - B，即A中不在B中的要素。
     */
    @Test
    public void Difference_test() throws IOException {
        File fileA = new File(Paths.get(SHP_PATH, "test", "上海测试中心.shp").toString());
        File fileB = new File(Paths.get(SHP_PATH, "test", "上海润和总部园.shp").toString());
        // 创建新的Shapefile路径
        File difference_outputFile = new File(Paths.get("target", "Difference_1.shp").toString());

        ShapefileDataStore storeA = new ShapefileDataStore(fileA.toURI().toURL());
        storeA.setCharset(StandardCharsets.UTF_8); // 避免读取属性值乱码
        ShapefileDataStore storeB = new ShapefileDataStore(fileB.toURI().toURL());
        storeB.setCharset(StandardCharsets.UTF_8);

        // ShapefileDataStore newDataStore = new ShapefileDataStore(difference_outputFile.toURI().toURL());
        // newDataStore.setCharset(StandardCharsets.UTF_8); // 避免写入属性值乱码

        CoordinateReferenceSystem crsA = storeA.getSchema().getCoordinateReferenceSystem();
        CoordinateReferenceSystem crsB = storeB.getSchema().getCoordinateReferenceSystem();

        System.out.println("坐标系：\ncrsA=\n" + crsA + "\ncrsB=\n" + crsB);
        if (!crsA.equals(crsB)) {
            // 如果CRS不同，需要进行投影变换
            // 这里省略了投影变换的代码
            System.out.println("投影变换：" + crsA + "-->" + crsB);
        }

        SimpleFeatureCollection featuresA = storeA.getFeatureSource().getFeatures();
        SimpleFeatureCollection featuresB = storeB.getFeatureSource().getFeatures();

        List<SimpleFeature> differenceFeatures = new ArrayList<>();

        try (SimpleFeatureIterator iteratorA = featuresA.features(); SimpleFeatureIterator iteratorB = featuresB.features()) {

            while (iteratorB.hasNext()) {
                SimpleFeature featureB = iteratorB.next();
                Geometry geomB = (Geometry) featureB.getDefaultGeometry();

                while (iteratorA.hasNext()) {
                    SimpleFeature featureA = iteratorA.next();
                    Geometry geomA = (Geometry) featureA.getDefaultGeometry();

                    // 计算差集
                    Geometry differenceGeometry = geomA.difference(geomB);
                    System.out.println("differenceGeometry=" + differenceGeometry);

                    // 如果差集不为空，则更新特征的几何属性
                    if (!differenceGeometry.isEmpty()) {
                        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(featuresA.getSchema());
                        builder.addAll(featureA.getAttributes());
                        builder.set(0, differenceGeometry);
                        differenceFeatures.add(builder.buildFeature(featureA.getID()));
                    }
                }
            }
        }

        // 使用相同的FeatureType创建新的FeatureCollection
        SimpleFeatureType featureType = featuresA.getSchema();
        SimpleFeatureCollection differenceFeatureCollection = new ListFeatureCollection(featureType, differenceFeatures);

        // 创建新的ShapefileDataStore
        ShapefileDataStore newDataStore = new ShapefileDataStore(difference_outputFile.toURI().toURL());
        newDataStore.setCharset(StandardCharsets.UTF_8); // 避免写入属性值乱码
        newDataStore.createSchema(featureType);

        // 将合并后的FeatureCollection写入新的Shapefile
        SimpleFeatureSource featureSource = newDataStore.getFeatureSource();
        SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;

        featureStore.addFeatures(differenceFeatureCollection);

        // 关闭数据源
        storeA.dispose();
        storeB.dispose();
        newDataStore.dispose();
    }

    /**
     * 对称差集分析 - Symmetric Difference
     * <p>
     *      对称差集是指两个集合中除了它们交集之外的所有元素的集合。在空间数据中，这意味着我们关注的是两个集合中独有的部分。
     *
     * <p>
     */
    @Test
    public void SymDifference_test() throws IOException {
        URL urlA = Paths.get(SHP_PATH, "test", "上海测试中心.shp").toUri().toURL();
        URL urlB = Paths.get(SHP_PATH, "test", "上海润和总部园.shp").toUri().toURL();

        ShapefileDataStore storeA = new ShapefileDataStore(urlA);
        ShapefileDataStore storeB = new ShapefileDataStore(urlB);
        // 避免读取属性值乱码
        storeA.setCharset(StandardCharsets.UTF_8);
        storeB.setCharset(StandardCharsets.UTF_8);

        SimpleFeatureCollection featuresA = storeA.getFeatureSource().getFeatures();
        SimpleFeatureCollection featuresB = storeB.getFeatureSource().getFeatures();

        CoordinateReferenceSystem crsA = storeA.getSchema().getCoordinateReferenceSystem();
        CoordinateReferenceSystem crsB = storeB.getSchema().getCoordinateReferenceSystem();

        System.out.println("坐标系：\ncrsA=\n" + crsA + "\ncrsB=\n" + crsB);
        if (!crsA.equals(crsB)) {
            // 如果CRS不同，需要进行投影变换
            // 这里省略了投影变换的代码
            System.out.println("投影变换：" + crsA + "-->" + crsB);
        }

        // 计算对称差集
        List<SimpleFeature> symDifferenceFeatures = calculateSymDifference(featuresA, featuresB);

        // 创建新的Shapefile路径
        File symDifference_outputFile = new File(Paths.get("target", "SymDifference_1.shp").toString());
        ShapefileDataStore newDataStore = new ShapefileDataStore(symDifference_outputFile.toURI().toURL());
        newDataStore.setCharset(StandardCharsets.UTF_8); // 避免写入属性值乱码

        // 使用相同的FeatureType
        SimpleFeatureType featureType = featuresA.getSchema();

        // 设置新数据源的模式
        newDataStore.createSchema(featureType);

        // 获取SimpleFeatureSource用于写入数据
        String typeName = newDataStore.getTypeNames()[0];
        SimpleFeatureSource featureSource = newDataStore.getFeatureSource(typeName);
        SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;

        // 开始写入数据,使用相同的FeatureType创建新的FeatureCollection
        featureStore.addFeatures(new ListFeatureCollection(featureType, symDifferenceFeatures));

        // 关闭数据源
        storeA.dispose();
        storeB.dispose();
        newDataStore.dispose();
    }

    // 计算对称差集：返回两个集合中独有的部分
    private List<SimpleFeature> calculateSymDifference(SimpleFeatureCollection a, SimpleFeatureCollection b) {
        // 计算第一个集合相对于第二个集合的差集
        List<SimpleFeature> diffA = calculateDiff(a, b);
        // 计算第二个集合相对于第一个集合的差集
        List<SimpleFeature> diffB = calculateDiff(b, a);

        // 合并差集
        List<SimpleFeature> result = new ArrayList<>(diffA);
        result.addAll(diffB);

        return result;
    }

    // 计算差集：返回第一个集合相对于第二个集合的差集
    private List<SimpleFeature> calculateDiff(SimpleFeatureCollection source, SimpleFeatureCollection other) {
        List<SimpleFeature> result = new ArrayList<>();
        try (SimpleFeatureIterator iterator = source.features()) {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                if (!isOverlapped(feature, other)) {
                    result.add(feature);
                }
            }
        }
        return result;
    }

    // 判断两个几何体是否重叠
    private boolean isOverlapped(SimpleFeature feature, SimpleFeatureCollection other) {
        Geometry geom = (Geometry) feature.getDefaultGeometry();

        try (SimpleFeatureIterator iterator = other.features()) {
            while (iterator.hasNext()) {
                SimpleFeature otherFeature = iterator.next();
                Geometry otherGeom = (Geometry) otherFeature.getDefaultGeometry();
                if (geom == null || otherGeom == null) {
                    continue;
                }
                if (geom.intersects(otherGeom)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 多层叠加分析,以相交为例
     * <p>
     * 希望找出这些图层之间的相交部分,并生成一个新的图层。
     */
    @Test
    public void MultiLayerOverlay_test4() throws Exception {
        // 从URL读取ShapeFile
        // URL roadsUrl = new URL("https://example.com/roads.shp"); // 道路网
        // URL riversUrl = new URL("https://example.com/rivers.shp"); // 河流
        // URL landUseUrl = new URL("https://example.com/landUse.shp"); // 土地利用区域，此示例，未使用

        URL roadsUrl = Paths.get(SHP_PATH, "test", "张衡路_华益路_张衡路_道路网.shp").toUri().toURL();
        ; // 道路网
        URL riversUrl = Paths.get(SHP_PATH, "test", "川杨河_马家浜_创新河_河流.shp").toUri().toURL();
        ; // 河流
        URL landUseUrl = new URL("https://example.com/landUse.shp"); // 土地利用区域，此示例，未使用

        Map<String, URL> layerUrls = new HashMap<>();
        layerUrls.put("roads", roadsUrl);
        layerUrls.put("rivers", riversUrl);
        layerUrls.put("landUse", landUseUrl);

        // 创建输出ShapeFile
        File outputFile = new File("target/MultiLayer_intersection.shp");

        // 执行叠加分析并输出结果
        generateIntersectionLayer(layerUrls, outputFile);
    }

    private static void generateIntersectionLayer(Map<String, URL> layerUrls, File outputFile) throws Exception {

        // 加载各个图层
        ShapefileDataStore roadsDataStore = new ShapefileDataStore(layerUrls.get("roads"));
        FeatureSource<SimpleFeatureType, SimpleFeature> roadsLayer = roadsDataStore.getFeatureSource();

        ShapefileDataStore riversDataStore = new ShapefileDataStore(layerUrls.get("rivers"));
        FeatureSource<SimpleFeatureType, SimpleFeature> riversLayer = riversDataStore.getFeatureSource();

        // ShapefileDataStore landUseDataStore = new ShapefileDataStore(layerUrls.get("landUse"));
        // FeatureSource<SimpleFeatureType, SimpleFeature> landUseLayer = landUseDataStore.getFeatureSource();

        // 执行相交分析
        List<SimpleFeature> intersectedFeatures = performIntersectionAnalysis(roadsLayer, riversLayer);

        // 创建输出数据存储
        ShapefileDataStore outputStore = new ShapefileDataStore(outputFile.toURI().toURL());
        outputStore.setCharset(StandardCharsets.UTF_8);
        outputStore.createSchema(roadsDataStore.getSchema());

        // 开始输出数据
        String outputTypeName = outputStore.getTypeNames()[0];
        SimpleFeatureStore outputFeatureStore = (SimpleFeatureStore) outputStore.getFeatureSource(outputTypeName);
        outputFeatureStore.addFeatures(new ListFeatureCollection(roadsDataStore.getSchema(), intersectedFeatures));

        outputStore.dispose();
    }

    private static List<SimpleFeature> performIntersectionAnalysis(FeatureSource<SimpleFeatureType, SimpleFeature> roadsLayer, FeatureSource<SimpleFeatureType, SimpleFeature> riversLayer) throws IOException {
        List<SimpleFeature> intersectedFeatures = new ArrayList<>();

        // 遍历道路网图层
        FeatureIterator<SimpleFeature> roadsIterator = roadsLayer.getFeatures().features();
        while (roadsIterator.hasNext()) {
            SimpleFeature roadFeature = roadsIterator.next();
            Geometry roadGeometry = (Geometry) roadFeature.getDefaultGeometry();

            // 遍历河流图层
            FeatureIterator<SimpleFeature> riversIterator = riversLayer.getFeatures().features();
            while (riversIterator.hasNext()) {
                SimpleFeature riverFeature = riversIterator.next();
                Geometry riverGeometry = (Geometry) riverFeature.getDefaultGeometry();

                // 检查道路和河流是否相交
                if (roadGeometry.intersects(riverGeometry)) {
                    // 创建一个新的要素,表示道路和河流的相交部分
                    SimpleFeature intersectedFeature = DataUtilities.template(roadsLayer.getSchema());
                    intersectedFeature.setDefaultGeometry(roadGeometry.intersection(riverGeometry));
                    intersectedFeatures.add(intersectedFeature);
                }
            }
            riversIterator.close();
        }
        roadsIterator.close();

        return intersectedFeatures;
    }

    /**
     * 条件叠加分析 - Conditional Overlay
     * <p>
     * 有时，你可能只对满足特定条件的要素进行叠加分析。例如，你可能只想找出位于特定海拔高度以上的公园。
     */
    @Test
    public void ConditionalOverlay_test2() throws IOException {
        // 指定Shapefile文件路径
        File dataFile1 = new File("path/to/your/first_shapefile.shp");
        File dataFile2 = new File("path/to/your/second_shapefile.shp");

        // 设置参数以打开数据源
        Map<String, Object> params1 = new HashMap<>();
        params1.put("url", dataFile1.toURI().toURL());

        Map<String, Object> params2 = new HashMap<>();
        params2.put("url", dataFile2.toURI().toURL());


        // 打开数据源
        DataStore dataStore1 = DataStoreFinder.getDataStore(params1);
        DataStore dataStore2 = DataStoreFinder.getDataStore(params2);

        // 获取数据集名称
        String typeName1 = dataStore1.getTypeNames()[0];
        String typeName2 = dataStore2.getTypeNames()[0];

        // 获取FeatureSource
        SimpleFeatureSource featureSource1 = dataStore1.getFeatureSource(typeName1);
        SimpleFeatureSource featureSource2 = dataStore2.getFeatureSource(typeName2);

        // 执行查询获取所有要素
        SimpleFeatureCollection features1 = featureSource1.getFeatures();
        SimpleFeatureCollection features2 = featureSource2.getFeatures();

        // 创建一个空的多边形集合用于存储最终的交集结果
        Geometry finalIntersection = null;

        // 遍历第一个数据集的所有要素
        try (SimpleFeatureIterator iterator1 = features1.features()) {
            while (iterator1.hasNext()) {
                SimpleFeature feature1 = iterator1.next();
                Geometry geom1 = (Geometry) feature1.getDefaultGeometry();

                // 检查属性条件，例如海拔高度
                Double elevation = (Double) feature1.getAttribute("elevation");
                if (elevation > 100) { // 只考虑海拔大于100米的要素
                    // 遍历第二个数据集的所有要素
                    try (SimpleFeatureIterator iterator2 = features2.features()) {
                        while (iterator2.hasNext()) {
                            SimpleFeature feature2 = iterator2.next();
                            Geometry geom2 = (Geometry) feature2.getDefaultGeometry();

                            // 进行交集分析
                            Geometry intersection = geom1.intersection(geom2);

                            // 更新最终交集结果
                            if (finalIntersection == null) {
                                finalIntersection = intersection;
                            } else {
                                finalIntersection = finalIntersection.intersection(intersection);
                            }
                        }
                    }
                }
            }
        }

        // 输出最终交集结果
        System.out.println("Final Intersection: " + finalIntersection);

        // 关闭数据源
        dataStore1.dispose();
        dataStore2.dispose();
    }

    /**
     * 聚合(Aggregation): 定义: 聚合操作是将多个几何要素根据某种条件(如属性值、空间关系等)合并为一个新的要素,并计算新要素的属性值,如面积、长度等。
     * <p>
     *     应用场景：聚合操作常用于统计分析，例如计算每个公园的面积、长度、数量等。
     *     示例：空间聚合分析可以将多个要素组合成更大的区域或类别，例如将多个小的公园合并成一个大的绿色空间。
     * </p>
     *
     */
    @Test
    public void Aggregation_test() throws IOException {
        // 指定Shapefile文件路径
        File dataFile = new File(Paths.get(SHP_PATH, "test", "园区及公司图层.shp").toString());

        ShapefileDataStore dataStore = new ShapefileDataStore(dataFile.toURI().toURL());
        dataStore.setCharset(StandardCharsets.UTF_8);

        SimpleFeatureSource featureSource = dataStore.getFeatureSource();

        // 执行查询获取所有要素
        SimpleFeatureCollection features = featureSource.getFeatures();

        // 创建一个空的多边形集合用于存储聚合结果
        Geometry aggregatedGeometry = null;

        // 遍历查询结果，将所有要素的几何体聚合在一起
        try (SimpleFeatureIterator iterator = features.features()) {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                Geometry geom = (Geometry) feature.getDefaultGeometry();
                if (geom == null) {
                    continue;
                }
                if ("公司".equals(feature.getAttribute("type"))) {
                    // 聚合几何体
                    if (aggregatedGeometry == null) {
                        aggregatedGeometry = geom;
                    } else {
                        aggregatedGeometry = aggregatedGeometry.union(geom);
                    }
                }
            }
        }

        // 输出聚合结果
        System.out.println("Aggregated Geometry: " + aggregatedGeometry);

        // 创建新的Shapefile路径
        File outputFile = new File(Paths.get("target", "Aggregated_2.shp").toString());
        ShapefileDataStore outputDataStore = new ShapefileDataStore(outputFile.toURI().toURL());
        outputDataStore.setCharset(StandardCharsets.UTF_8); // 避免写入属性值乱码

        // 使用相同的FeatureType
        SimpleFeatureType featureType = dataStore.getSchema();

        // 设置新数据源的模式
        outputDataStore.createSchema(featureType);

        // 获取SimpleFeatureSource用于写入数据
        String typeName = outputDataStore.getTypeNames()[0];
        SimpleFeatureSource outputFeatureSource = outputDataStore.getFeatureSource(typeName);
        SimpleFeatureStore outputFeatureStore = (SimpleFeatureStore) outputFeatureSource;

        SimpleFeature feature = new SimpleFeatureBuilder(featureType).buildFeature(null);
        feature.setDefaultGeometry(aggregatedGeometry);
        feature.setAttribute("type", "公司");
        // feature.setAttribute("area", aggregatedGeometry.getArea()); // Unknown attribute area

        System.out.println("统计面积=" + aggregatedGeometry.getArea());

        // 开始写入数据,使用相同的FeatureType创建新的FeatureCollection
        outputFeatureStore.addFeatures(new ListFeatureCollection(featureType, feature));

        // 关闭数据源
        dataStore.dispose();
    }

    /**
     * 线与面的叠加分析 - Line and Polygon Overlay
     * <p>
     * 在本例中，我们将分析道路网络与土地使用类型之间的关系，以找出哪些类型的土地使用被特定的道路穿过。
     */
    @Test
    public void LinePolygonOverlay_test4() throws IOException {
        // 指定Shapefile文件路径
        URL firstUrl = Paths.get(SHP_PATH, "test", "川杨河_马家浜_创新河_河流.shp").toUri().toURL();
        URL secondUrl = Paths.get(SHP_PATH, "test", "张恒公园_土地利用区域.shp").toUri().toURL();

        // 设置参数以打开数据源
        Map<String, Object> params1 = new HashMap<>();
        params1.put("url", firstUrl);

        Map<String, Object> params2 = new HashMap<>();
        params2.put("url", secondUrl);

        // 打开数据源
        DataStore dataStoreRoads = DataStoreFinder.getDataStore(params1);
        DataStore dataStoreLandUse = DataStoreFinder.getDataStore(params2);

        // 获取数据集名称
        String typeName1 = dataStoreRoads.getTypeNames()[0];
        String typeName2 = dataStoreLandUse.getTypeNames()[0];

        // 获取FeatureSource
        SimpleFeatureSource featureSourceRoads = dataStoreRoads.getFeatureSource(typeName1);
        SimpleFeatureSource featureSourceLandUse = dataStoreLandUse.getFeatureSource(typeName2);

        // 执行查询获取所有道路和土地使用的要素
        SimpleFeatureCollection roads = featureSourceRoads.getFeatures();
        SimpleFeatureCollection landUse = featureSourceLandUse.getFeatures();

        // 创建GeometryFactory实例
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 8307); // 使用数据集的SRID

        // 创建一个空的多边形集合用于存储最终的交集结果
        Geometry finalIntersections = null;

        // 遍历道路数据集的所有要素
        try (SimpleFeatureIterator iteratorRoads = roads.features()) {
            while (iteratorRoads.hasNext()) {
                SimpleFeature road = iteratorRoads.next();
                Geometry roadGeom = (Geometry) road.getDefaultGeometry();

                // 遍历土地使用数据集的所有要素
                try (SimpleFeatureIterator iteratorLandUse = landUse.features()) {
                    while (iteratorLandUse.hasNext()) {
                        SimpleFeature use = iteratorLandUse.next();
                        Geometry useGeom = (Geometry) use.getDefaultGeometry();
                        if (useGeom != null) {
                            // 进行交集分析
                            Geometry intersection = roadGeom.intersection(useGeom);
                            if (!intersection.isEmpty()) {
                                System.out.println(road.getAttribute("name") + " 相交 " + use.getAttribute("name"));
                                // 更新最终交集结果
                                if (finalIntersections == null) {
                                    finalIntersections = intersection;
                                } else {
                                    finalIntersections = finalIntersections.union(intersection);
                                }
                            }
                        }
                    }
                }
            }
        }

        // 输出最终交集结果
        System.out.println("Final Intersections: " + finalIntersections);

        // 关闭数据源
        dataStoreRoads.dispose();
        dataStoreLandUse.dispose();
    }

    /**
     * 点与面的叠加分析 - Point and Polygon Overlay
     * <p>
     * 判断一个点是否在区域内
     */
    @Test
    public void LinePolygonOverlay_test5() throws IOException {

        // 指定Shapefile文件路径
        URL firstUrl = Paths.get(SHP_PATH, "test", "上海润和总部园_北门_南门_西门_点集图层.shp").toUri().toURL();
        URL secondUrl = Paths.get(SHP_PATH, "test", "上海润和总部园.shp").toUri().toURL();

        // 设置参数以打开数据源
        Map<String, Object> params1 = new HashMap<>();
        params1.put("url", firstUrl);

        Map<String, Object> params2 = new HashMap<>();
        params2.put("url", secondUrl);

        // 打开数据源
        DataStore dataStore1 = DataStoreFinder.getDataStore(params1);
        DataStore dataStore2 = DataStoreFinder.getDataStore(params2);

        // 获取数据集名称
        String typeName1 = dataStore1.getTypeNames()[0];
        String typeName2 = dataStore2.getTypeNames()[0];

        // 获取FeatureSource
        SimpleFeatureSource featureSource1 = dataStore1.getFeatureSource(typeName1);
        SimpleFeatureSource featureSource2 = dataStore2.getFeatureSource(typeName2);

        // 执行查询获取所有犯罪事件点和街区的要素
        SimpleFeatureCollection features1 = featureSource1.getFeatures(); // 点集
        SimpleFeatureCollection features2 = featureSource2.getFeatures(); // 面

        // 创建GeometryFactory实例
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 8307); // 使用数据集的SRID

        // 创建一个空的点集合用于存储最终的交集结果
        Geometry finalPointsInPolygons = null;

        // 遍历犯罪事件数据集的所有要素
        try (SimpleFeatureIterator iterator1 = features1.features()) {
            while (iterator1.hasNext()) {
                SimpleFeature feature1 = iterator1.next();
                Geometry point = (Geometry) feature1.getDefaultGeometry();
                if (point == null) {
                    continue;
                }

                // 遍历街区数据集的所有要素
                try (SimpleFeatureIterator iterator2 = features2.features()) {
                    while (iterator2.hasNext()) {
                        SimpleFeature feature2 = iterator2.next();
                        Geometry polygon = (Geometry) feature2.getDefaultGeometry();
                        if (polygon == null) {
                            continue;
                        }

                        // 检查点是否在多边形内
                        boolean isInside = polygon.contains(point);

                        if (isInside) {
                            System.out.println(feature1.getAttribute("name") + " is inside  " + feature2.getAttribute("name"));
                            System.out.println("Point " + point + " is inside Polygon " + polygon);
                            // 如果点在多边形内，则将其添加到最终结果中
                            if (finalPointsInPolygons == null) {
                                finalPointsInPolygons = point;
                            } else {
                                finalPointsInPolygons = finalPointsInPolygons.union(point);
                            }
                        }
                    }
                }
            }
        }

        // 输出最终点集合结果
        System.out.println("Final Points in Polygons: " + finalPointsInPolygons);

        // 关闭数据源
        dataStore1.dispose();
        dataStore2.dispose();
    }

}
