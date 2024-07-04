package com.zja.gdal.example.GeospatialAnalysis;

import org.gdal.ogr.*;
import org.junit.Test;

import java.nio.file.Paths;

/**
 * 空间分析：设置空间过滤器
 *
 * @Author: zhengja
 * @Date: 2024-07-03 15:52
 */
public class SetSpatialFilterTest {

    String shp_path = "D:\\temp\\shp\\";

    /**
     * 设置空间过滤器: 只保留在矩形范围内的要素
     */
    @Test
    public void SetSpatialFilter_test() {
        // 初始化GDAL
        ogr.RegisterAll();

        String rectangle_shapefile_path = Paths.get(shp_path, "test", "上海润和总部园.shp").toString();
        String shapefile_path = Paths.get(shp_path, "test", "园区及公司图层.shp").toString();

        // 打开源数据
        DataSource rectangleDS = ogr.Open(rectangle_shapefile_path, 0);
        if (rectangleDS == null) {
            System.err.println("无法打开矩形数据源");
            return;
        }
        DataSource dataSource = ogr.Open(shapefile_path, 0);
        if (dataSource == null) {
            System.err.println("无法打开数据源");
            return;
        }

        // 获取图层
        Layer rectangle_layer = rectangleDS.GetLayer(0);
        if (rectangle_layer == null) {
            System.err.println("无法获取矩形图层");
            return;
        }
        Layer layer = dataSource.GetLayer(0);
        if (layer == null) {
            System.err.println("无法获取图层");
            return;
        }

        // 多边形（矩形）过滤器
        // String wkt = "MultiPolygon (((121.60047541707274377 31.19211162001495197, 121.60347808153410654 31.1925347960716941, 121.60378870199562584 31.18981856335916092, 121.60201701491881465 31.18952331597569483, 121.60141878291882733 31.18973983081363244, 121.60047541707274377 31.19211162001495197)))";
        // Geometry rectangle_geometry = GeoJSONUtil.wktToGeometry(wkt);

        Feature rectangle_feature = rectangle_layer.GetFeature(0);
        Geometry rectangle_geometry = rectangle_feature.GetGeometryRef();

        // 设置空间过滤器
        layer.SetSpatialFilter(rectangle_geometry);

        // 遍历并打印符合条件的要素
        Feature feature;
        while ((feature = layer.GetNextFeature()) != null) {
            System.out.println("Feature ID: " + feature.GetFID());
            // 可以添加更多对要素的操作
            System.out.println("Name: " + feature.GetFieldAsString("name"));
        }

        // 清除过滤器（可选）
        layer.SetSpatialFilter(null);

        // 关闭数据源
        dataSource.delete();
    }
}
