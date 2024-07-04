package com.zja.gdal.example.GeospatialAnalysis;

import org.gdal.ogr.*;
import org.junit.Test;

import java.nio.file.Paths;

/**
 * 空间分析：空间查询
 *
 * @Author: zhengja
 * @Date: 2024-07-03 16:14
 */
public class SpatialQueryTest {

    String shp_path = "D:\\temp\\shp\\";


    /**
     * 空间查询：在矢量数据中执行空间查询操作，例如查找与给定要素相交的要素
     */
    @Test
    public void SpatialQuery_test() {
        ogr.RegisterAll(); // 注册所有支持的矢量数据格式

        // String vectorFile = "path/to/vector.shp";
        String vectorFile = Paths.get(shp_path, "test", "上海润和总部园.shp").toString();
        DataSource dataSource = ogr.Open(vectorFile);

        Layer layer = dataSource.GetLayer(0);

        Feature queryFeature = layer.GetFeature(0); // 选择要执行空间查询的要素

        Geometry queryGeometry = queryFeature.GetGeometryRef();

        // 遍历要素
        Feature feature;
        while ((feature = layer.GetNextFeature()) != null) {
            Geometry geometry = feature.GetGeometryRef();
            if (geometry != null) {
                // 执行空间查询
                if (geometry.Intersects(queryGeometry)) {
                    // 在此处可以对满足查询条件的要素进行进一步的操作

                    System.out.println(feature.GetFieldAsString("name"));
                }
            }
            // 释放资源
            feature.delete();
        }

        // 关闭数据源
        dataSource.delete();
    }
}
