package com.zja.gdal.example.GeospatialAnalysis;

import org.gdal.gdal.gdal;
import org.gdal.ogr.*;
import org.gdal.osr.CoordinateTransformation;
import org.gdal.osr.SpatialReference;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.Vector;

/**
 * 空间分析：重投影（坐标转换）
 *
 * @Author: zhengja
 * @Date: 2024-07-03 16:20
 */
public class CoordinateTransformationTest {

    String shp_path = "D:\\temp\\shp\\";

    /**
     * shp 重投影(坐标系转换)
     * <p>
     * 举例：把一个点图层进行重投影(也就是坐标系转换，为了把单从：度-->米)
     */
    @Test
    public void shp_CoordinateTransform_test() {
        // shp 重投影

        String shpFilePath = Paths.get(shp_path, "test", "园区及公司图层.shp").toString();
        String outShpFilePath = Paths.get(shp_path, "test", "园区及公司图层_重投影.shp").toString();

        ogr.RegisterAll();
        gdal.AllRegister();

        DataSource inputDataSource = ogr.Open(shpFilePath);
        Layer inputLayer = inputDataSource.GetLayer(0);

        SpatialReference sourceSRS = inputLayer.GetSpatialRef(); // 源坐标系 EPSG:4326 单位：度
        SpatialReference targetSRS = new SpatialReference();
        targetSRS.ImportFromEPSG(6487); // 目标坐标系 EPSG:6487 单位：米

        CoordinateTransformation transformation = new CoordinateTransformation(sourceSRS, targetSRS);

        DataSource outputDataSource = ogr.GetDriverByName("ESRI Shapefile").CreateDataSource(outShpFilePath);
        Vector<String> options = new Vector<String>();
        options.add("ENCODING=UTF-8"); // 设置编码, 默认为ASCII 避免图层属性值中文乱码
        Layer outputLayer = outputDataSource.CreateLayer("reprojected", targetSRS, inputLayer.GetGeomType(), options);

        // 复制输入图层的属性字段定义到输出图层
        for (int i = 0; i < inputLayer.GetLayerDefn().GetFieldCount(); i++) {
            outputLayer.CreateField(inputLayer.GetLayerDefn().GetFieldDefn(i));
        }

        inputLayer.ResetReading();

        Feature inputFeature;
        while ((inputFeature = inputLayer.GetNextFeature()) != null) {
            Feature outputFeature = new Feature(outputLayer.GetLayerDefn());
            Geometry inputGeometry = inputFeature.GetGeometryRef();

            inputGeometry.Transform(transformation);
            outputFeature.SetGeometry(inputGeometry);

            // 复制属性值
            for (int i = 0; i < inputFeature.GetFieldCount(); i++) {
                String fieldName = inputFeature.GetFieldDefnRef(i).GetName();
                String fieldValue = inputFeature.GetFieldAsString(i);
                outputFeature.SetField(i, fieldValue);
            }

            outputLayer.CreateFeature(outputFeature);

            outputFeature.delete();
            inputFeature.delete();
        }

        inputDataSource.delete();
        outputDataSource.delete();
    }

    /**
     * shp 重投影(坐标系转换)
     * <p>
     * 举例：把一个点图层进行重投影(也就是坐标系转换，为了把单从：度-->米)
     */
    @Test
    public void shp_CoordinateTransform_test2() {
        // shp 重投影

        String shpFilePath = Paths.get(shp_path, "test", "园区及公司图层.shp").toString();
        String outShpFilePath = Paths.get(shp_path, "test", "园区及公司图层_重投影.shp").toString();

        ogr.RegisterAll();
        gdal.AllRegister();
        // gdal.SetConfigOption("SHAPE_ENCODING", ""); // 设置默认编码为空，避免属性的编码问题(转换后会乱码)

        DataSource inputDataSource = ogr.Open(shpFilePath);
        Layer inputLayer = inputDataSource.GetLayer(0);

        SpatialReference sourceSRS = inputLayer.GetSpatialRef(); // 源坐标系 EPSG:4326 单位：度
        SpatialReference targetSRS = new SpatialReference();
        targetSRS.ImportFromEPSG(6487); // 目标坐标系 EPSG:6487 单位：米

        // 坐标系转换
        CoordinateTransformation transformation = new CoordinateTransformation(sourceSRS, targetSRS);

        // 打开数据源
        DataSource outputDataSource = ogr.GetDriverByName("ESRI Shapefile").CreateDataSource(outShpFilePath);

        Vector<String> options = new Vector<String>();
        options.add("ENCODING=UTF-8"); // 设置编码, 默认为ASCII 避免图层属性值中文乱码
        Layer outputLayer = outputDataSource.CreateLayer("reprojected", targetSRS, inputLayer.GetGeomType(), options);

        // 复制输入图层的属性字段定义到输出图层
        for (int i = 0; i < inputLayer.GetLayerDefn().GetFieldCount(); i++) {
            outputLayer.CreateField(inputLayer.GetLayerDefn().GetFieldDefn(i));
        }

        inputLayer.ResetReading();

        Feature inputFeature;
        while ((inputFeature = inputLayer.GetNextFeature()) != null) {
            Feature outputFeature = new Feature(outputLayer.GetLayerDefn());
            Geometry inputGeometry = inputFeature.GetGeometryRef();

            // 复制属性值
            for (int i = 0; i < inputFeature.GetFieldCount(); i++) {
                String fieldName = inputFeature.GetFieldDefnRef(i).GetName();
                String fieldValue = inputFeature.GetFieldAsString(i);
                outputFeature.SetField(i, fieldValue);
                // 处理复杂类型的属性值
                /*int fieldTypeCode = inputFeature.GetFieldDefnRef(i).GetType();
                switch (fieldTypeCode) {
                    case ogr.OFTInteger:
                        outputFeature.SetField(i, inputFeature.GetFieldAsInteger(i));
                        break;
                    case ogr.OFTReal:
                        outputFeature.SetField(i, inputFeature.GetFieldAsDouble(i));
                        break;
                    case ogr.OFTString:
                        outputFeature.SetField(i, inputFeature.GetFieldAsString(i));
                        break;
                    // 根据实际情况添加更多类型处理
                    default:
                        // 对于其他类型，根据需要处理
                        break;
                }*/
            }

            inputGeometry.Transform(transformation);
            outputFeature.SetGeometry(inputGeometry);

            outputLayer.CreateFeature(outputFeature);

            outputFeature.delete();
            inputFeature.delete();
        }

        inputDataSource.delete();
        outputDataSource.delete();
    }

    /**
     * shp 重投影(坐标系转换)
     */
    @Test
    public void ShapefileReprojection_test() {
        // 注册所有的OGR驱动
        ogr.RegisterAll();

        String shpFilePath = Paths.get(shp_path, "test", "园区及公司图层.shp").toString();
        String outShpFilePath = Paths.get(shp_path, "test", "园区及公司图层_重投影.shp").toString();

        DataSource dataSource = ogr.Open(shpFilePath, 0);
        if (dataSource == null) {
            System.out.println("无法打开输入文件");
            return;
        }

        Layer inputLayer = dataSource.GetLayer(0);
        if (inputLayer == null) {
            System.out.println("图层获取失败");
            return;
        }

        // 获取原始坐标系
        SpatialReference sourceSRS = inputLayer.GetSpatialRef();
        if (sourceSRS == null) {
            System.out.println("无法获取原始坐标系");
            return;
        }

        // 定义目标坐标系，例如转换到WGS84 (EPSG:4326)
        SpatialReference targetSRS = new SpatialReference();
        targetSRS.ImportFromEPSG(6487); // 修改为你需要的EPSG代码

        // 创建坐标转换器
        CoordinateTransformation transform = new CoordinateTransformation(sourceSRS, targetSRS);

        // 创建输出数据源和图层
        DataSource outputDataSource = ogr.GetDriverByName("ESRI Shapefile").CreateDataSource(outShpFilePath, null);
        if (outputDataSource == null) {
            System.out.println("无法创建输出数据源");
            return;
        }

        Vector<String> vector = new Vector<String>();
        vector.add("ENCODING=UTF-8"); // 设置编码, 默认为ASCII 避免图层属性值中文乱码
        Layer outputLayer = outputDataSource.CreateLayer("output", targetSRS, inputLayer.GetGeomType(), vector);
        if (outputLayer == null) {
            System.out.println("无法创建输出图层");
            return;
        }

        // 复制字段定义
        for (int i = 0; i < inputLayer.GetLayerDefn().GetFieldCount(); i++) {
            outputLayer.CreateField(inputLayer.GetLayerDefn().GetFieldDefn(i));
        }

        // 遍历要素并转换
        Feature inputFeature;
        while ((inputFeature = inputLayer.GetNextFeature()) != null) {
            Geometry geometry = inputFeature.GetGeometryRef();
            if (geometry != null) {
                geometry.Transform(transform);
                Feature outputFeature = new Feature(outputLayer.GetLayerDefn());
                outputFeature.SetGeometry(geometry);

                // 复制属性
                for (int i = 0; i < inputFeature.GetFieldCount(); i++) {
                    outputFeature.SetField(i, inputFeature.GetFieldAsString(i));
                }

                outputLayer.CreateFeature(outputFeature);
                outputFeature.delete();
            }
            inputFeature.delete();
        }

        // 清理
        outputDataSource.delete();
    }
}
