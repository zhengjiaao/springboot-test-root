package com.zja.shapefile.SpatialAnalysis;

import org.geotools.data.*;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.CRS;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.operation.buffer.BufferOp;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory2;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhengja
 * @Date: 2024-07-11 10:54
 */
public class VectorBufferAnalysisTest {

    private static final String SHP_PATH = "D:\\temp\\shp\\";

    /**
     * 缓冲区分析 Buffer
     */
    @Test
    public void bufferAnalysis_test1() throws IOException, FactoryException, TransformException {
        // 输入输出文件路径
        // URL inputURL = Paths.get(SHP_PATH, "test", "多几何类型图层.shp").toUri().toURL();
        // URL outputURL = Paths.get("target", "多几何类型图层_缓冲区_1000米.shp").toUri().toURL();

        // 点 缓冲区（todo 未通过，还是源来的坐标系）
        URL inputURL = Paths.get(SHP_PATH, "test", "上海润和总部园_北门_点图层.shp").toUri().toURL();
        URL outputURL = Paths.get("target", "上海润和总部园_北门_点图层_缓冲区_1000米.shp").toUri().toURL();

        // 线 缓冲区（通过）
        // URL inputURL = Paths.get(SHP_PATH, "test", "张衡路.shp").toUri().toURL();
        // URL outputURL = Paths.get("target", "张衡路_缓冲区_1000米.shp").toUri().toURL();

        // 面 缓冲区（通过）
        // URL inputURL = Paths.get(SHP_PATH, "test", "上海测试中心.shp").toUri().toURL();
        // URL outputURL = Paths.get("target", "上海测试中心_缓冲区_1000米.shp").toUri().toURL();

        // 缓冲区距离（单位：米）
        double bufferDistance = 1000;

        // 目标投影
        CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:3857");

        // 打开输入数据存储
        ShapefileDataStore inputStore = new ShapefileDataStore(inputURL);
        inputStore.setCharset(StandardCharsets.UTF_8);

        // 获取数据源
        SimpleFeatureSource featureSource = inputStore.getFeatureSource();
        SimpleFeatureCollection features = featureSource.getFeatures();

        // 创建输出特征集合
        ListFeatureCollection outputFeatures = new ListFeatureCollection(featureSource.getSchema());

        try (FeatureIterator<SimpleFeature> iterator = features.features()) {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                Geometry geom = (Geometry) feature.getDefaultGeometry();
                CoordinateReferenceSystem sourceCRS = feature.getFeatureType().getCoordinateReferenceSystem();
                // 转换坐标系
                MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS, true);
                geom = JTS.transform(geom, transform);

                // 创建缓冲区
                Geometry bufferedGeom = geom.buffer(bufferDistance);

                // 反向转换坐标系回原坐标系
                MathTransform reverseTransform = CRS.findMathTransform(targetCRS, sourceCRS, true);
                bufferedGeom = JTS.transform(bufferedGeom, reverseTransform);

                // 更新特征的几何属性 todo 不能避免点缓冲区坐标问题
                feature.setAttribute(feature.getDefaultGeometryProperty().getName().getLocalPart(), bufferedGeom);
                feature.setDefaultGeometry(bufferedGeom);
                outputFeatures.add(feature);

                // 创建新要素 方式2  todo 不能避免点缓冲区坐标问题
                /*SimpleFeatureType featureType = feature.getFeatureType();
                SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);
                featureBuilder.set(featureType.getGeometryDescriptor().getName().getLocalPart(), bufferedGeom);
                for (AttributeDescriptor descriptor : featureType.getAttributeDescriptors()) {
                    if (!descriptor.getName().equals(featureType.getGeometryDescriptor().getName())) {
                        featureBuilder.set(descriptor.getLocalName(), feature.getAttribute(descriptor.getLocalName()));
                    }
                }
                SimpleFeature newFeature = featureBuilder.buildFeature(feature.getID());
                outputFeatures.add(newFeature);*/
            }
        }

        // 写入输出文件
        Map<String, Object> params = new HashMap<>();
        params.put("url", outputURL);
        params.put("create spatial index", Boolean.TRUE);
        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
        ShapefileDataStore outputStore = (ShapefileDataStore) dataStoreFactory.createDataStore(params);
        outputStore.setCharset(StandardCharsets.UTF_8);
        outputStore.createSchema(outputFeatures.getSchema());

        // 开始输出数据
        String outputTypeName = outputStore.getTypeNames()[0];
        SimpleFeatureStore outputFeatureStore = (SimpleFeatureStore) outputStore.getFeatureSource(outputTypeName);
        outputFeatureStore.addFeatures(outputFeatures);

        inputStore.dispose();
        outputStore.dispose();
    }

    /**
     * 缓冲区分析 Buffer：缓冲区分析是将一个或多个要素缓冲成一个新的要素。
     * <p>
     * 这段代码能够处理包含点、线、面等多种几何类型的图层，并为每种几何类|型创建缓冲区。
     * 当图层中包含多种几何类型（如点、线、面）时，创建缓冲区的逻辑基本相同，但需要额外注意的是不同几何类型的缓冲效果可能不同。
     */
    @Test
    public void bufferAnalysis_test2() throws IOException, FactoryException {

        // 点 缓冲区（todo 未通过，还是源来的坐标系）
        URL inputURL = Paths.get(SHP_PATH, "test", "上海润和总部园食堂.shp").toUri().toURL();
        URL outputURL = Paths.get("target", "上海润和总部园食堂_缓冲区_1000米.shp").toUri().toURL();

        // 线 缓冲区（通过）
        // URL inputURL = Paths.get(SHP_PATH, "test", "张衡路.shp").toUri().toURL();
        // URL outputURL = Paths.get("target", "张衡路_缓冲区_1000米.shp").toUri().toURL();

        // 面 缓冲区（通过）
        // URL inputURL = Paths.get(SHP_PATH, "test", "上海测试中心.shp").toUri().toURL();
        // URL outputURL = Paths.get("target", "上海测试中心_缓冲区_1000米.shp").toUri().toURL();

        // 缓冲区距离（单位：米） 使用笛卡尔坐标测量(米)，矩形是以边界进行缓冲的，每个边界线距离缓冲距离都是1000米
        double bufferDistance = 1000; // 默认：EPSG:4326 单位：度  -- 投影 --> EPSG:3857 单位：米

        // 目标投影
        CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:3857");

        // 打开输入数据存储
        ShapefileDataStore inputStore = new ShapefileDataStore(inputURL);
        inputStore.setCharset(StandardCharsets.UTF_8);
        String typeName = inputStore.getTypeNames()[0];
        SimpleFeatureSource inputFeatureSource = inputStore.getFeatureSource(typeName);
        SimpleFeatureType schema = inputFeatureSource.getSchema();

        // 获取输入数据的坐标参考系统
        CoordinateReferenceSystem sourceCRS = inputFeatureSource.getSchema().getCoordinateReferenceSystem();
        System.out.println("sourceCRS=\n" + sourceCRS);

        // 进行重投影-->缓冲区距离
        MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
        List<SimpleFeature> simpleFeatures = coordinateTransformAndBufferDistance(inputFeatureSource, transform, bufferDistance);

        // 创建输出数据存储
        ShapefileDataStore outputStore = new ShapefileDataStore(outputURL);
        outputStore.setCharset(StandardCharsets.UTF_8);

        // 设置输出数据的CRS
        SimpleFeatureType retypeSchema = SimpleFeatureTypeBuilder.retype(schema, targetCRS);
        outputStore.createSchema(retypeSchema);
        System.out.println("outputCRS=\n" + targetCRS);

        // 开始输出数据
        String outputTypeName = outputStore.getTypeNames()[0];
        SimpleFeatureStore outputFeatureStore = (SimpleFeatureStore) outputStore.getFeatureSource(outputTypeName);
        outputFeatureStore.addFeatures(new ListFeatureCollection(schema, simpleFeatures));

        inputStore.dispose();
        outputStore.dispose();
    }

    // 过程：重投影(转换坐标系)--> 缓冲区距离
    private static List<SimpleFeature> coordinateTransformAndBufferDistance(SimpleFeatureSource source, MathTransform transform, double bufferDistance) throws IOException {
        List<SimpleFeature> features = new ArrayList<>();

        try (SimpleFeatureIterator iterator = source.getFeatures().features()) {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                Geometry geom = (Geometry) feature.getDefaultGeometry();

                if (geom != null) {
                    // 转换坐标系
                    Geometry reprojected = JTS.transform(geom, transform);

                    // 创建缓冲区
                    Geometry buffer = reprojected.buffer(bufferDistance);

                    feature.setDefaultGeometry(buffer); // 设置默认坐标系
                }

                SimpleFeature copyFeature = SimpleFeatureBuilder.copy(feature);
                features.add(copyFeature);
            }
        } catch (TransformException e) {
            throw new RuntimeException(e);
        }

        return features;
    }

    /**
     * 缓冲区分析
     */
    @Test
    public void bufferAnalysis_test3() throws IOException, FactoryException, TransformException {
        // 点 缓冲区（todo 未通过，还是源来的坐标系）
        // URL inputURL = Paths.get(SHP_PATH, "test", "上海润和总部园_北门_点图层.shp").toUri().toURL();
        // URL outputURL = Paths.get("target", "上海润和总部园_北门_点图层_缓冲区_1000米.shp").toUri().toURL();

        // 线 缓冲区（通过）
        // URL inputURL = Paths.get(SHP_PATH, "test", "张衡路.shp").toUri().toURL();
        // URL outputURL = Paths.get("target", "张衡路_缓冲区_1000米.shp").toUri().toURL();

        // 面 缓冲区（通过）
        URL inputURL = Paths.get(SHP_PATH, "test", "上海测试中心.shp").toUri().toURL();
        URL outputURL = Paths.get("target", "上海测试中心_缓冲区_1000米.shp").toUri().toURL();

        // 缓冲区距离（单位：米） 使用笛卡尔坐标测量(米)，矩形是以边界进行缓冲的，每个边界线距离缓冲距离都是1000米
        double bufferDistance = 1000; // 默认：EPSG:4326 单位：度  -- 投影 --> EPSG:3857 单位：米

        // 从 shapefile 中读取要素
        FileDataStore store = FileDataStoreFinder.getDataStore(inputURL);
        SimpleFeatureCollection features = store.getFeatureSource().getFeatures();

        // 获取输入 shapefile 的坐标系
        CoordinateReferenceSystem inputCRS = store.getSchema().getCoordinateReferenceSystem();

        // 创建 EPSG:3857 坐标系
        CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:3857");

        MathTransform transformTargetCRS = CRS.findMathTransform(inputCRS, targetCRS);
        MathTransform transformInputCRS = CRS.findMathTransform(targetCRS, inputCRS);

        // 创建 geometry 工厂
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

        // 创建过滤器工厂
        FilterFactory2 filterFactory = CommonFactoryFinder.getFilterFactory2();

        // 创建新的 shapefile 数据存储
        Map<String, Serializable> params = new HashMap<>();
        params.put(ShapefileDataStoreFactory.URLP.key, outputURL);
        ShapefileDataStore newStore = (ShapefileDataStore) new ShapefileDataStoreFactory().createNewDataStore(params);
        newStore.createSchema(features.getSchema());

        // 获取要素存储
        SimpleFeatureStore featureStore = (SimpleFeatureStore) newStore.getFeatureSource();
        Transaction transaction = new DefaultTransaction("create");
        featureStore.setTransaction(transaction);

        // 遍历要素并创建缓冲区
        SimpleFeatureIterator iterator = features.features();
        List<SimpleFeature> newFeatures = new ArrayList<>();
        while (iterator.hasNext()) {
            SimpleFeature feature = iterator.next();
            Geometry geometry = (Geometry) feature.getDefaultGeometry();
            if (geometry == null) {
                continue;
            }

            // 转换坐标系
            Geometry transformedGeometry = JTS.transform(geometry, transformTargetCRS);

            // 创建缓冲区
            BufferOp bufferOp = new BufferOp(transformedGeometry);
            // bufferOp.setEndCapStyle(BufferParameters.CAP_ROUND);
            // bufferOp.setQuadrantSegments(BufferParameters.DEFAULT_QUADRANT_SEGMENTS);

            // 创建缓冲区
            // BufferParameters bufferParams = new BufferParameters();
            // bufferParams.setEndCapStyle(BufferParameters.CAP_ROUND); // 默认1，设置了缓冲区的端点样式为圆形(CAP_ROUND)。这意味着当缓冲区结束时，端点会呈现为圆形而不是尖锐的角或扁平的边缘。
            // bufferParams.setJoinStyle(BufferParameters.JOIN_ROUND); // 默认1，设置了缓冲区线段连接处的样式为圆形(JOIN_ROUND)。这表示当两条线段相交形成缓冲区时，连接处会是圆滑的过渡，而不是尖锐的角度。
            // bufferParams.setQuadrantSegments(BufferParameters.DEFAULT_QUADRANT_SEGMENTS); // 默认8，设置了缓冲区的象限段数，用于近似曲线的段数，这决定了缓冲区拐角的平滑度。
            // BufferOp bufferOp = new BufferOp(transformedGeometry, bufferParams);

            Geometry buffer = bufferOp.getResultGeometry(bufferDistance); // 创建缓冲区,单位：米

            // 转换坐标系
            Geometry bufferGeometry = JTS.transform(buffer, transformInputCRS);

            // 创建新的要素
            SimpleFeature newFeature = DataUtilities.template(features.getSchema());
            newFeature.setDefaultGeometry(bufferGeometry);
            newFeatures.add(newFeature);
        }
        iterator.close();

        // 将新要素添加到要素存储
        featureStore.addFeatures(DataUtilities.collection(newFeatures));

        // 提交事务并关闭连接
        transaction.commit();
        transaction.close();
        newStore.dispose();
    }
}
