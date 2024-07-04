package com.zja.gdal.example;

import org.gdal.gdal.gdal;
import org.gdal.ogr.*;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Vector;

/**
 * 矢量数据文件,包括 点、线、面等
 * <p>
 * 矢量叠加分析通常涉及空间关系查询，如交集、并集、差异等。OGR支持通过SQL查询或直接遍历要素进行这类分析。
 *
 * @Author: zhengja
 * @Date: 2024-06-20 15:51
 */
public class GDALVectorTest {

    String shp_path = "D:\\temp\\shp\\";

    // 创建空的矢量数据文件
    @Test
    public void GDALEmptyVector_test() {
        // 初始化GDAL
        ogr.RegisterAll();

        // 创建矢量数据文件
        String vectorFile = "target/vector1.shp";

        Driver vectorDriver = ogr.GetDriverByName("ESRI Shapefile");
        DataSource vectorDataSource = vectorDriver.CreateDataSource(vectorFile);

        // 创建矢量图层
        String layerName = "layer1";
        int geometryType = ogr.wkbPoint;

        Layer vectorLayer = vectorDataSource.CreateLayer(layerName, null, geometryType);
        // vectorLayer.CreateField(new FieldDefn("name", ogr.OFTString));
        // vectorLayer.CreateField(new FieldDefn("age", ogr.OFTInteger));

        // 关闭矢量数据文件
        vectorDataSource.delete();
    }

    // 创建矢量数据文件
    @Test
    public void GDALVector_test() {
        // 初始化GDAL
        ogr.RegisterAll();
        // gdal.SetConfigOption("SHAPE_ENCODING", "UTF-8"); // 设置为空时可自动识别编码，GBK和UTF-8都可以识别
        // System.setProperty("SHAPE_ENCODING", "UTF-8"); // 告诉OGR读取Shapefile时使用UTF-8编码

        // 属性表支持中文(linux要求为UTF-8编码) // "CP936" "UTF-8" "GBK" "GB2312"
        // gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8", "YES"); // 支持中文路径
        // gdal.SetConfigOption("SHAPE_ENCODING", "CP936"); // 支持中文字段
        // gdal.SetConfigOption("FILEGDB_ENCODING", "UTF-8");

        System.out.println(gdal.GetConfigOption("GDAL_FILENAME_IS_UTF8"));
        System.out.println(gdal.GetConfigOption("SHAPE_ENCODING"));
        System.out.println(gdal.GetConfigOption("FILEGDB_ENCODING"));

        // 创建矢量数据文件
        String vectorFile = "target/vector2.shp";

        Driver vectorDriver = ogr.GetDriverByName("ESRI Shapefile");
        DataSource vectorDataSource = vectorDriver.CreateDataSource(vectorFile);

        // 创建矢量图层
        String layerName = "layer1";
        int geometryType = ogr.wkbPoint;

        Vector<String> vector = new Vector<String>();
        vector.add("ENCODING=UTF-8");

        // Layer vectorLayer = vectorDataSource.CreateLayer(layerName, null, geometryType);
        Layer vectorLayer = vectorDataSource.CreateLayer(layerName, null, geometryType,vector);

        // 添加属性字段
        String fieldName = "name";
        int fieldType = ogr.OFTString;
        int fieldWidth = 50;

        String fieldName2 = "名称";
        int fieldType2 = ogr.OFTString;
        int fieldWidth2 = 50;

        FieldDefn fieldDefn = new FieldDefn(fieldName, fieldType);
        fieldDefn.SetWidth(fieldWidth);

        FieldDefn fieldDefn2 = new FieldDefn(fieldName2, fieldType2);
        fieldDefn2.SetWidth(fieldWidth2);

        vectorLayer.CreateField(fieldDefn);
        vectorLayer.CreateField(fieldDefn2);

        // 创建要素并添加到矢量图层
        Feature feature = new Feature(vectorLayer.GetLayerDefn());

        Geometry geometry = ogr.CreateGeometryFromWkt("POINT (0 0)"); // 创建一个点几何对象（示例中的点位于经度0、纬度0）
        feature.SetGeometry(geometry);

        String attributeValue = "Feature 1 名称"; // 属性值
        String attributeValue2 = "Feature 2 名称"; // 属性值
        feature.SetField(fieldName, attributeValue);
        feature.SetField(fieldName2, attributeValue2);

        vectorLayer.CreateFeature(feature);

        // 关闭矢量数据文件
        vectorDataSource.delete();
    }


    // 读取矢量数据文件
    @Test
    public void Open_test() {
        // 初始化GDAL
        ogr.RegisterAll();

        // 构建跨平台的路径
        String vectorFile = Paths.get(shp_path, "test", "上海润和总部园_28_合并图层.shp").toString();

        // 读取矢量数据文件
        DataSource vectorDataSource = ogr.Open(vectorFile, 0);
        if (vectorDataSource == null) {
            System.err.println("无法打开数据源: " + vectorFile);
            return;
        }

        // 获取矢量图层
        Layer vectorLayer = vectorDataSource.GetLayer(0);
        if (vectorLayer == null) {
            System.err.println("无法获取图层");
            return;
        }

        // 遍历矢量图层中的要素
        Feature feature;
        while ((feature = vectorLayer.GetNextFeature()) != null) {
            Geometry geometry = feature.GetGeometryRef();
            if (geometry != null) {
                System.out.println(geometry.ExportToWkt());
            }

            System.out.println(feature.GetFieldAsString("name"));
        }

        // 关闭数据源
        vectorDataSource.Close();
    }
}
