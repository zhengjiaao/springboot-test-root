package com.zja.gdal.example.GeospatialAnalysis;

import org.gdal.ogr.*;
import org.gdal.osr.SpatialReference;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.Vector;

/**
 * 空间分析：矢量叠加分析
 *
 * @Author: zhengja
 * @Date: 2024-07-03 15:45
 */
public class VectorOverlayAnalysisTest {

    String shp_path = "D:\\temp\\shp\\";

    /**
     * 矢量叠加分析：进行要素间的交集分析
     */
    @Test
    public void VectorOverlay_test1() {
        // 初始化GDAL
        ogr.RegisterAll();
        // gdal.SetConfigOption("SHAPE_ENCODING", "UTF-8"); // 设置为空时可自动识别编码，GBK和UTF-8都可以识别
        // System.setProperty("SHAPE_ENCODING", "UTF-8"); // 告诉OGR读取Shapefile时使用UTF-8编码

        String inputLayer1File = Paths.get(shp_path, "test", "上海润和总部园.shp").toString();
        String inputLayer2File = Paths.get(shp_path, "test", "银联商务28栋.shp").toString();
        String outputLayerFile = Paths.get(shp_path, "test", "上海润和总部园_28_交集图层.shp").toString();

        // 打开数据源
        DataSource inputDS1 = ogr.Open(inputLayer1File, false);
        if (inputDS1 == null) {
            System.out.println("Open failed.");
            return;
        }
        DataSource inputDS2 = ogr.Open(inputLayer2File, false);
        if (inputDS2 == null) {
            System.out.println("Open failed.");
            return;
        }

        // 获取图层
        Layer inputLayer1 = inputDS1.GetLayer(0); // 假设只有一个图层
        if (inputLayer1 == null) {
            System.out.println("GetLayer failed.");
            return;
        }
        // 假设我们有另一个图层需要与之叠加分析
        Layer inputLayer2 = inputDS2.GetLayer(0);
        if (inputLayer2 == null) {
            System.out.println("GetLayer failed.");
            return;
        }

        // 创建一个新的数据源来保存结果
        DataSource resultDataSource = ogr.GetDriverByName("ESRI Shapefile").CreateDataSource(outputLayerFile, null);

        Vector<String> vector = new Vector<String>();
        vector.add("ENCODING=UTF-8"); // 设置编码, 默认为ASCII 避免图层属性值中文乱码
        Layer resultLayer = resultDataSource.CreateLayer("result", inputLayer1.GetSpatialRef(), ogr.wkbPolygon, vector);

        // 复制图层定义以保持属性结构一致
        // 将输入图层1的属性表结构复制到输出图层
        FeatureDefn inputLayer1Defn = inputLayer1.GetLayerDefn();
        for (int j = 0; j < inputLayer1Defn.GetFieldCount(); j++) {
            FieldDefn fieldDefn = inputLayer1Defn.GetFieldDefn(j);
            resultLayer.CreateField(fieldDefn);
        }

        // 将输入图层2的属性表结构复制到输出图层
       /* FeatureDefn inputLayer2Defn = inputLayer2.GetLayerDefn();
        for (int j = 0; j < inputLayer2Defn.GetFieldCount(); j++) {
            FieldDefn fieldDefn = inputLayer2Defn.GetFieldDefn(j);
            resultLayer.CreateField(fieldDefn); //与输入图层1相同的字段名称，会重命名为 fieldName --> fieldName_1
        }*/
        //
        // inputLayer1.Intersection()

        // 假设我们对第一个图层的每个要素进行交集分析
        for (int i = 0; i < inputLayer1.GetFeatureCount(); i++) {
            Feature feature1 = inputLayer1.GetNextFeature();
            if (feature1 != null) {
                Geometry geometry = feature1.GetGeometryRef();

                // 对第二个图层进行循环，找到交集
                for (int j = 0; j < inputLayer2.GetFeatureCount(); j++) {
                    Feature feature2 = inputLayer2.GetNextFeature();
                    if (feature2 != null) {
                        Geometry anotherGeometry = feature2.GetGeometryRef();

                        // 计算交集
                        Geometry intersection = geometry.Intersection(anotherGeometry);

                        if (!intersection.IsEmpty()) {
                            // 如果有交集，创建新的要素并添加到结果图层
                            Feature newFeature = new Feature(resultLayer.GetLayerDefn());
                            newFeature.SetGeometry(intersection);

                            // 复制属性（这里简化处理，实际情况可能需要更复杂的逻辑来处理属性）
                            for (int k = 0; k < feature1.GetFieldCount(); k++) {
                                newFeature.SetField(k, feature1.GetFieldAsString(k));
                            }

                            resultLayer.CreateFeature(newFeature);
                            newFeature.delete();
                        }

                        feature2.delete();
                    }
                }

                feature1.delete();
            }
        }

        // 清理资源
        resultLayer.delete();
        resultDataSource.delete();
        inputLayer1.delete();
        inputLayer2.delete();
    }

    /**
     * 矢量叠加分析：进行要素间的交集分析
     */
    @Test
    public void VectorOverlay_test2() {
        // 初始化GDAL
        ogr.RegisterAll();
        // gdal.SetConfigOption("SHAPE_ENCODING", "UTF-8"); // 设置为空时可自动识别编码，GBK和UTF-8都可以识别
        // System.setProperty("SHAPE_ENCODING", "UTF-8"); // 告诉OGR读取Shapefile时使用UTF-8编码

        String inputLayer1File = Paths.get(shp_path, "test", "上海润和总部园.shp").toString();
        String inputLayer2File = Paths.get(shp_path, "test", "银联商务28栋.shp").toString();
        String outputLayerFile = Paths.get(shp_path, "test", "上海润和总部园_28_交集图层4.shp").toString();

        // 打开数据源
        DataSource inputDS1 = ogr.Open(inputLayer1File, false);
        if (inputDS1 == null) {
            System.out.println("Open failed.");
            return;
        }
        DataSource inputDS2 = ogr.Open(inputLayer2File, false);
        if (inputDS2 == null) {
            System.out.println("Open failed.");
            return;
        }

        // 获取图层
        Layer inputLayer1 = inputDS1.GetLayer(0); // 假设只有一个图层
        if (inputLayer1 == null) {
            System.out.println("GetLayer failed.");
            return;
        }
        // 假设我们有另一个图层需要与之叠加分析
        Layer inputLayer2 = inputDS2.GetLayer(0);
        if (inputLayer2 == null) {
            System.out.println("GetLayer failed.");
            return;
        }

        // 创建一个新的数据源来保存结果
        DataSource resultDataSource = ogr.GetDriverByName("ESRI Shapefile").CreateDataSource(outputLayerFile, null);
        Vector<String> vector = new Vector<String>();
        vector.add("ENCODING=UTF-8"); // 设置编码, 默认为ASCII 避免图层属性值中文乱码
        Layer resultLayer = resultDataSource.CreateLayer("result", inputLayer1.GetSpatialRef(), ogr.wkbPolygon, vector);

        // 复制图层定义以保持属性结构一致
        // 将输入图层1的属性表结构复制到输出图层
        /*FeatureDefn inputLayer1Defn = inputLayer1.GetLayerDefn();
        for (int j = 0; j < inputLayer1Defn.GetFieldCount(); j++) {
            FieldDefn fieldDefn = inputLayer1Defn.GetFieldDefn(j);
            resultLayer.CreateField(fieldDefn);
        }*/

        // 将输入图层2的属性表结构复制到输出图层
        /*FeatureDefn inputLayer2Defn = inputLayer2.GetLayerDefn();
        for (int j = 0; j < inputLayer2Defn.GetFieldCount(); j++) {
            FieldDefn fieldDefn = inputLayer2Defn.GetFieldDefn(j);
            resultLayer.CreateField(fieldDefn); // 与输入图层1相同的字段名称，会重命名为 fieldName --> fieldName_1 ，导致图层2的属性值为null
        }*/

        // 相交分析 todo 待解决，如何自定义图层2的字段名称，及属性赋值
        int intersectioned = inputLayer1.Intersection(inputLayer2, resultLayer); // 若不把属性表结构复制到输出图层，则默认添加图层1+图层2的属性表结构,但是字段会被改变
        System.out.println("intersectioned: " + intersectioned);

        // 清理资源
        resultLayer.delete();
        resultDataSource.delete();
        inputLayer1.delete();
        inputLayer2.delete();
    }

    /**
     * 矢量叠加分析：将两个或多个矢量数据进行叠加分析(将两个矢量数据集进行叠加分析，并生成新的矢量数据集）
     */
    @Test
    public void VectorOverlay_test3() {
        ogr.RegisterAll(); // 注册所有的矢量数据驱动
        // gdal.SetConfigOption("SHAPE_ENCODING", "UTF-8"); // 设置为空时可自动识别编码，GBK和UTF-8都可以识别
        // System.setProperty("SHAPE_ENCODING", "UTF-8"); // 告诉OGR读取Shapefile时使用UTF-8编码

        // String inputLayer1File = "path/to/input_layer1.shp";
        // String inputLayer2File = "path/to/input_layer2.shp";
        // String outputLayerFile = "path/to/output_layer.shp";

        String inputLayer1File = Paths.get(shp_path, "test", "上海润和总部园.shp").toString();
        String inputLayer2File = Paths.get(shp_path, "test", "银联商务28栋.shp").toString();
        String outputLayerFile = Paths.get(shp_path, "test", "上海润和总部园_28_合并图层.shp").toString();

        DataSource inputDS1 = ogr.Open(inputLayer1File, false);
        DataSource inputDS2 = ogr.Open(inputLayer2File, false);

        Driver driver = ogr.GetDriverByName("ESRI Shapefile");
        DataSource outputDS = driver.CreateDataSource(outputLayerFile);

        // 获取输入图层
        int layerCount1 = inputDS1.GetLayerCount();
        int layerCount2 = inputDS2.GetLayerCount();

        if (layerCount1 > 0 && layerCount2 > 0) {
            for (int i = 0; i < layerCount1; i++) {
                Layer inputLayer1 = inputDS1.GetLayer(i);
                Layer inputLayer2 = inputDS2.GetLayer(i);

                // 获取输入图层的空间参考信息
                SpatialReference inputSR1 = inputLayer1.GetSpatialRef();
                SpatialReference inputSR2 = inputLayer2.GetSpatialRef();

                // 创建输出图层
                Vector<String> vector = new Vector<String>();
                vector.add("ENCODING=UTF-8"); // 设置编码, 默认为ASCII 避免图层属性值中文乱码
                Layer outputLayer = outputDS.CreateLayer(inputLayer1.GetName(), inputSR1, inputLayer1.GetGeomType(), vector);

                // 将输入图层1的属性表结构复制到输出图层
                FeatureDefn inputLayerDefn = inputLayer1.GetLayerDefn();
                for (int j = 0; j < inputLayerDefn.GetFieldCount(); j++) {
                    FieldDefn fieldDefn = inputLayerDefn.GetFieldDefn(j);
                    outputLayer.CreateField(fieldDefn);
                }

                // 进行叠加分析
                // 添加输入图层1的要素到输出图层
                Feature inputFeature1 = null;
                while ((inputFeature1 = inputLayer1.GetNextFeature()) != null) {
                    outputLayer.CreateFeature(inputFeature1);
                    inputFeature1.delete();
                }

                outputLayer.SyncToDisk();
            }
        }

        // 关闭数据源
        inputDS1.delete();
        inputDS2.delete();
        outputDS.delete();
    }

    /**
     * 矢量叠加分析：将两个或多个矢量数据进行叠加分析(将两个矢量数据集进行叠加分析，并生成新的矢量数据集）
     *
     * @throws Exception
     */
    @Test
    public void VectorOverlay_test4() throws Exception {
        ogr.RegisterAll(); // 注册所有的矢量数据驱动

        // String inputLayer1File = "path/to/input_layer1.shp";
        // String inputLayer2File = "path/to/input_layer2.shp";
        // String outputLayerFile = "path/to/output_layer.shp";

        String inputLayer1File = Paths.get(shp_path, "test", "上海润和总部园.shp").toString();
        String inputLayer2File = Paths.get(shp_path, "test", "银联商务28栋.shp").toString();
        String outputLayerFile = Paths.get(shp_path, "test", "上海润和总部园_28_合并图层2.shp").toString();


        DataSource inputDS1 = ogr.Open(inputLayer1File, 0);
        DataSource inputDS2 = ogr.Open(inputLayer2File, 0);

        if (inputDS1 == null || inputDS2 == null) {
            throw new Exception("Failed to open input datasets.");
        }

        Driver driver = ogr.GetDriverByName("ESRI Shapefile");
        if (driver == null) {
            throw new Exception("ESRI Shapefile driver not found.");
        }

        // if (driver.Exists(outputLayerFile)) {
        //     driver.DeleteDataSource(outputLayerFile);
        // }

        DataSource outputDS = driver.CreateDataSource(outputLayerFile);
        if (outputDS == null) {
            throw new Exception("Failed to create output dataset.");
        }

        int layerCount1 = inputDS1.GetLayerCount();
        int layerCount2 = inputDS2.GetLayerCount();

        if (layerCount1 > 0 && layerCount2 > 0) {
            for (int i = 0; i < layerCount1; i++) {
                Layer inputLayer1 = inputDS1.GetLayer(i);
                Layer inputLayer2 = inputDS2.GetLayer(i);

                // 获取输入图层的空间参考信息
                SpatialReference inputSR1 = inputLayer1.GetSpatialRef();
                if (inputSR1 == null) {
                    continue; // 跳过没有空间参考的图层
                }

                // 创建输出图层
                Vector<String> vector = new Vector<String>();
                vector.add("ENCODING=UTF-8"); // 设置编码, 默认为ASCII 避免图层属性值中文乱码
                Layer outputLayer = outputDS.CreateLayer(inputLayer1.GetName(), inputSR1, inputLayer1.GetGeomType(), vector);

                // 将输入图层1的属性表结构复制到输出图层
                FeatureDefn inputLayerDefn = inputLayer1.GetLayerDefn();
                for (int j = 0; j < inputLayerDefn.GetFieldCount(); j++) {
                    FieldDefn fieldDefn = inputLayerDefn.GetFieldDefn(j);
                    outputLayer.CreateField(fieldDefn);
                }

                // 添加输入图层1的要素到输出图层
                Feature inputFeature1 = null;
                while ((inputFeature1 = inputLayer1.GetNextFeature()) != null) {
                    Feature outputFeature = new Feature(inputLayerDefn);
                    outputFeature.SetFrom(inputFeature1);
                    outputLayer.CreateFeature(outputFeature);
                    inputFeature1.delete();
                    outputFeature.delete();
                }

                // 这里未完成真正的叠加分析逻辑，只是简单复制了要素
                // 实际叠加分析可能需要根据空间关系筛选要素等复杂操作

                outputLayer.SyncToDisk();
            }
        }

        // 关闭数据源
        inputDS1.delete();
        inputDS2.delete();
        outputDS.delete();
    }
}
