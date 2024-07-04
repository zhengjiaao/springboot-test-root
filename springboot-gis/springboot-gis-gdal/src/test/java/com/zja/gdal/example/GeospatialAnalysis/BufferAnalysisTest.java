package com.zja.gdal.example.GeospatialAnalysis;

import org.gdal.gdal.gdal;
import org.gdal.ogr.*;
import org.gdal.osr.CoordinateTransformation;
import org.gdal.osr.SpatialReference;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 空间分析：缓冲区分析
 *
 * @Author: zhengja
 * @Date: 2024-07-03 15:47
 */
public class BufferAnalysisTest {

    String shp_path = "D:\\temp\\shp\\";

    // 使用Java GDAL进行空间分析

    /**
     * 缓冲区分析: 将点图层缓冲到一定距离内，并输出结果
     */
    @Test
    public void BufferAnalysis_test1() {
        ogr.RegisterAll();
        gdal.AllRegister();

        // 输入的点图层文件路径
        // String pointLayerPath = "path_to_point_layer.shp";
        // 输入的多边形图层文件路径
        // String polygonLayerPath = "path_to_polygon_layer.shp";

        String pointLayerPath = Paths.get(shp_path, "test", "上海润和总部园食堂.shp").toString();
        String polygonLayerPath = Paths.get(shp_path, "test", "园区及公司图层.shp").toString();

        // String pointLayerPath = Paths.get(shp_path, "test", "上海润和总部园食堂-重投影.shp").toString();
        // String polygonLayerPath = Paths.get(shp_path, "test", "园区及公司图层-重投影.shp").toString();

        // 定义目标投影坐标系（例如，使用UTM投影坐标系）
        SpatialReference targetSRS = new SpatialReference();
        // targetSRS.SetUTM(10, true); // 设置为您希望使用的UTM投影带 单位：米
        targetSRS.ImportFromEPSG(6487); // 目标坐标系 EPSG:6487 单位：米

        // 打开点图层数据集
        DataSource pointDataset = ogr.Open(pointLayerPath);
        Layer pointLayer = pointDataset.GetLayer(0);

        // 打开多边形图层数据集
        DataSource polygonDataset = ogr.Open(polygonLayerPath);
        Layer polygonLayer = polygonDataset.GetLayer(0);

        // 获取点图层和多边形图层的坐标系
        SpatialReference pointSRS = pointLayer.GetSpatialRef();
        SpatialReference polygonSRS = polygonLayer.GetSpatialRef();

        // 遍历点图层中的每个要素并进行坐标转换
        Feature pointFeature;
        while ((pointFeature = pointLayer.GetNextFeature()) != null) {
            Geometry pointGeometry = pointFeature.GetGeometryRef();
            if (pointGeometry != null) {

                // 重投影 (转换坐标系) 如果点图层的坐标系不是目标投影坐标系，进行坐标转换
                if (pointSRS.IsSame(targetSRS) == 0) {
                    CoordinateTransformation pointTransformation = new CoordinateTransformation(pointSRS, targetSRS);

                    pointGeometry.Transform(pointTransformation);
                }

                // 设置点图层缓冲区的距离
                double distance = 100; // 缓冲区的距离，单位：米
                Geometry bufferGeometry = pointGeometry.Buffer(distance);

                // 遍历点图层中的每个要素并进行坐标转换
                Feature polygonFeature;
                while ((polygonFeature = polygonLayer.GetNextFeature()) != null) {
                    Geometry polygonGeometry = polygonFeature.GetGeometryRef();
                    if (polygonGeometry != null) {

                        // 重投影 (转换坐标系) 如果多边形图层的坐标系不是目标投影坐标系，进行坐标转换
                        if (polygonSRS.IsSame(targetSRS) == 0) {
                            CoordinateTransformation polygonTransformation = new CoordinateTransformation(polygonSRS, targetSRS);
                            polygonGeometry.Transform(polygonTransformation);
                        }

                        // 实现1：计算交集(todo 其实没必要先进行缓存区，再计算缓存区与多边形的交集，可以直接计算校验点到多边形的距离)
                       /* Geometry intersection = polygonFeature.GetGeometryRef().Intersection(bufferGeometry);
                        System.out.println(intersection); // 打印交集

                        if (!intersection.IsEmpty()) {
                            System.out.println("name: " + polygonFeature.GetFieldAsString("name") + "计算点到多边形或交集的距离：" + pointGeometry.Distance(polygonGeometry));
                        }
                        intersection.delete();*/

                        // 实现2：计算点图层到多边形或交集的距离
                        if (pointGeometry.Distance(polygonGeometry) < distance) {
                            System.out.println("name: " + polygonFeature.GetFieldAsString("name") + "计算点到多边形或交集的距离：" + pointGeometry.Distance(polygonGeometry));
                        }

                        polygonGeometry.delete();
                    }
                    polygonFeature.delete();
                }
                bufferGeometry.delete();
            }
            pointFeature.delete();
        }

        // 释放资源-关闭数据源
        pointDataset.delete();
        polygonDataset.delete();
    }

    /**
     * 缓冲区分析
     * <p>
     * 创建一个Java程序来读取点和多边形图层数据，将它们重投影到EPSG:6487坐标系（假设适用于你的数据区域），然后筛选出距离指定点1公里内的多边形要素。
     */
    @Test
    public void BufferAnalysis_test2() {
        try {
            // 初始化GDAL
            ogr.RegisterAll();

            String pointLayerPath = Paths.get(shp_path, "test", "上海润和总部园北门和南门-多点图层.shp").toString();
            String polygonLayerPath = Paths.get(shp_path, "test", "园区及公司图层.shp").toString();

            // 读取点图层数据
            // DataSource pointDs = ogr.Open("path/to/your/point_layer.shp", 0);
            DataSource pointDs = ogr.Open(pointLayerPath, 0);
            // Layer pointLayer = pointDs.GetLayerByName(pointDs.GetDescription());
            Layer pointLayer = pointDs.GetLayer(0);
            if (pointLayer == null) {
                System.out.println("无法打开点图层");
                return;
            }

            // 读取多边形图层数据
            // DataSource polyDs = ogr.Open("path/to/your/polygon_layer.shp", 0);
            DataSource polyDs = ogr.Open(polygonLayerPath, 0);
            // Layer polyLayer = polyDs.GetLayerByName(polyDs.GetDescription());
            Layer polyLayer = polyDs.GetLayer(0);
            if (polyLayer == null) {
                System.out.println("无法打开多边形图层");
                return;
            }

            // 假定我们要查询的第一个点
            long count = pointLayer.GetFeatureCount();
            System.out.println(count);

            List<Geometry> polyGeoms = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                Geometry pointGeom = pointLayer.GetFeature(i).GetGeometryRef();
                System.out.println(pointLayer.GetFeature(i).GetFieldAsString("name"));
                if (pointGeom == null) {
                    System.out.println("无法获取点要素的几何体");
                    return;
                }

                // 重投影到EPSG:6487
                Geometry pointGeomEPSG6487 = reprojectToEPSG6487(pointGeom);

                // 筛选距离内多边形
                for (int j = 0; j < polyLayer.GetFeatureCount(); j++) {
                    Geometry polyGeom = polyLayer.GetFeature(j).GetGeometryRef();
                    Geometry polyGeomEPSG6487 = reprojectToEPSG6487(polyGeom);

                    // 计算米为单位的距离
                    double distanceInMeters = pointGeomEPSG6487.Distance(polyGeomEPSG6487);

                    if (distanceInMeters <= 1000) { // 1000米即1公里
                        System.out.println("name=" + polyLayer.GetFeature(j).GetFieldAsString("name") + ", 找到了符合条件的多边形，距离：" + distanceInMeters + " 米");
                        if (!polyGeoms.contains(polyGeom)) {
                            polyGeoms.add(polyGeom);
                        }
                        // 这里可以进一步处理找到的多边形，比如输出其属性等
                        // System.out.println("name=" + polyLayer.GetFeature(i).GetFieldAsString("name"));
                    }
                }
            }
            System.out.println("polyGeoms.size()=" + polyGeoms.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Geometry reprojectToEPSG6487(Geometry geom) {
        SpatialReference wgs84 = new SpatialReference();
        wgs84.ImportFromEPSG(4326);

        SpatialReference epsg6487 = new SpatialReference();
        epsg6487.ImportFromEPSG(6487);

        Geometry transformedGeom = geom.Clone();
        transformedGeom.TransformTo(epsg6487);
        return transformedGeom;
    }
}
