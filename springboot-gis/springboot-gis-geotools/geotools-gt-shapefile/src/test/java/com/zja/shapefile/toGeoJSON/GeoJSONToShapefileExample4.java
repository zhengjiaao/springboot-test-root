/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-23 14:08
 * @Since:
 */
package com.zja.shapefile.toGeoJSON;

import com.zja.shapefile.util.ResourceUtil;
import com.zja.shapefile.util.TargetPathUtil;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureTypeImpl;
import org.geotools.feature.type.GeometryDescriptorImpl;
import org.geotools.feature.type.GeometryTypeImpl;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.junit.jupiter.api.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: zhengja
 * @since: 2023/10/23 14:08
 */
@Deprecated
public class GeoJSONToShapefileExample4 {

    @Test
    public void test_GeoJSONToShapefileConverter() throws Exception {
        String geoJSONFilePath = ResourceUtil.getResourceFilePath("310000_full.json");

        String shapefilePath = TargetPathUtil.getTempFilePath("310000_full.shp");

        convertGeoJSONToShapefile(geoJSONFilePath, shapefilePath);
    }

    /**
     * 将GeoJSON转换为Shapefile
     *
     * @throws Exception 转换过程中的异常
     */
    public void convertGeoJSONToShapefile(String geoJSONFilePath, String shapefilePath) throws Exception {
        try {
            // 读取GeoJSON文件并解析为FeatureCollection
            InputStream in = Files.newInputStream(Paths.get(geoJSONFilePath));
            GeometryJSON gjson = new GeometryJSON();
            FeatureJSON fjson = new FeatureJSON(gjson);
            FeatureCollection<SimpleFeatureType, SimpleFeature> features = fjson.readFeatureCollection(in);

            // 提取FeatureCollection中的Schema
            SimpleFeatureType schema = extractSchemaFromFeatures(features);

            // 将Features转换为指定Schema的Features
            List<SimpleFeature> outFeatures = convertFeaturesToSchema(features, schema);

            // 将转换后的Features保存为Shapefile
            saveFeaturesToShapefile(outFeatures, schema, shapefilePath);
        } catch (IOException e) {
            throw new Exception("Failed to convert GeoJSON to Shapefile.", e);
        }
    }

    /**
     * 从FeatureCollection中提取Schema
     *
     * @param features FeatureCollection对象
     * @return 提取出的Schema
     */
    private SimpleFeatureType extractSchemaFromFeatures(FeatureCollection<SimpleFeatureType, SimpleFeature> features) {
        // 获取FeatureCollection中的第一个Feature的Schema作为基准
        SimpleFeatureType baseSchema = features.getSchema();
        // 创建一个新的SimpleFeatureTypeImpl对象，用于存储提取出的Schema
        SimpleFeatureTypeImpl schema = new SimpleFeatureTypeImpl(
                new NameImpl(baseSchema.getName().getNamespaceURI(), baseSchema.getName().getLocalPart()),
                baseSchema.getAttributeDescriptors(),
                new GeometryDescriptorImpl(
                        new GeometryTypeImpl(
                                baseSchema.getGeometryDescriptor().getType().getName(),
                                baseSchema.getGeometryDescriptor().getType().getBinding(),
                                baseSchema.getGeometryDescriptor().getType().getCoordinateReferenceSystem(),
                                baseSchema.getGeometryDescriptor().getType().isIdentified(),
                                baseSchema.getGeometryDescriptor().getType().isAbstract(),
                                baseSchema.getGeometryDescriptor().getType().getRestrictions(),
                                baseSchema.getGeometryDescriptor().getType().getSuper(),
                                baseSchema.getGeometryDescriptor().getType().getDescription()
                        ),
                        baseSchema.getGeometryDescriptor().getName(),
                        baseSchema.getGeometryDescriptor().getMinOccurs(),
                        baseSchema.getGeometryDescriptor().getMaxOccurs(),
                        baseSchema.getGeometryDescriptor().isNillable(),
                        baseSchema.getGeometryDescriptor().getDefaultValue()
                ),
                baseSchema.isAbstract(),
                baseSchema.getRestrictions(),
                baseSchema.getSuper(),
                baseSchema.getDescription()
        );
        return schema;
    }

    /**
     * 将Features转换为指定Schema的Features
     *
     * @param features 原始Features
     * @param schema   指定的Schema
     * @return 转换后的Features
     */
    private List<SimpleFeature> convertFeaturesToSchema(FeatureCollection<SimpleFeatureType, SimpleFeature> features, SimpleFeatureType schema) {
        List<SimpleFeature> outFeatures = new ArrayList<>();
        try (FeatureIterator<SimpleFeature> features2 = features.features()) {
            while (features2.hasNext()) {
                SimpleFeature f = features2.next();
                // 使用DataUtilities.reType方法将Feature转换为指定Schema的Feature
                SimpleFeature reType = DataUtilities.reType(schema, f, true);

                // 将原始Feature的几何属性复制到转换后的Feature中
                reType.setAttribute(schema.getGeometryDescriptor().getName(),
                        f.getAttribute(schema.getGeometryDescriptor().getName()));

                outFeatures.add(reType);
            }
        }
        return outFeatures;
    }

    /**
     * 将Features保存为Shapefile
     *
     * @param features Features
     * @param schema   Schema
     * @param shpPath  Shapefile保存路径
     * @throws IOException 保存过程中的异常
     */
    private void saveFeaturesToShapefile(List<SimpleFeature> features, SimpleFeatureType schema, String shpPath) throws IOException {
        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
        File shpFile = new File(shpPath);
        Map<String, Serializable> params = new HashMap<>();
        params.put("url", shpFile.toURI().toURL());
        params.put("create spatial index", Boolean.TRUE);

        ShapefileDataStore newDataStore = (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);
        newDataStore.setCharset(StandardCharsets.UTF_8);

        newDataStore.createSchema(schema);

        Transaction transaction = new DefaultTransaction("create");
        String typeName = newDataStore.getTypeNames()[0];
        SimpleFeatureSource featureSource = newDataStore.getFeatureSource(typeName);

        if (featureSource instanceof SimpleFeatureStore) {
            SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
            SimpleFeatureCollection collection = new ListFeatureCollection(schema, features);
            featureStore.setTransaction(transaction);
            try {
                featureStore.addFeatures(collection);
                generateCpgFile(shpPath, StandardCharsets.UTF_8);
                transaction.commit();
            } catch (Exception problem) {
                transaction.rollback();
                throw new IOException("Failed to save features to Shapefile.", problem);
            } finally {
                transaction.close();
            }
        } else {
            throw new IOException(typeName + " does not support read/write access");
        }
    }

    /**
     * 生成Cpg文件
     *
     * @param filePath 文件路径
     * @param charset  字符编码
     * @throws IOException 生成过程中的异常
     */
    private void generateCpgFile(String filePath, Charset charset) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + filePath);
        }
        String tempPath = file.getPath();
        int index = tempPath.lastIndexOf('.');
        String name = tempPath.substring(0, index);
        String cpgFilePath = name + ".cpg";
        File cpgFile = new File(cpgFilePath);
        if (cpgFile.exists()) {
            return;
        }
        boolean newFile = cpgFile.createNewFile();
        if (newFile) {
            Files.write(cpgFile.toPath(), charset.toString().getBytes(charset));
        }
    }
}
