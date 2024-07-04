package com.zja.gdal.example.GeospatialAnalysis;

import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconst;
import org.gdal.osr.SpatialReference;
import org.junit.Test;

/**
 * 空间分析：栅格分析
 *
 * @Author: zhengja
 * @Date: 2024-07-03 16:18
 */
public class RasterAnalysisTest {

    String tif_path = "D:\\temp\\tif\\";


    /**
     * 读取和显示栅格数据
     */
    @Test
    public void RasterAnalysis_test() {
        gdal.AllRegister(); // 注册所有支持的栅格数据格式

        String rasterFile = tif_path + "测试.tif";
        // String rasterFile = "path/to/raster.tif";
        Dataset dataset = gdal.Open(rasterFile);

        int width = dataset.getRasterXSize();
        int height = dataset.getRasterYSize();
        int bandCount = dataset.getRasterCount();
        System.out.println("栅格数据信息：");
        System.out.println("宽度：" + width);
        System.out.println("高度：" + height);
        System.out.println("波段数：" + bandCount);
        System.out.println("波段信息：");
        for (int i = 1; i <= bandCount; i++) {
            Band band = dataset.GetRasterBand(i);
            int dataType = band.GetRasterDataType();
            System.out.println("波段" + i + "的数据类型：" + dataType);
        }
        SpatialReference spatialReference = dataset.GetSpatialRef();
        System.out.println("投影信息：" + spatialReference.ExportToWkt());

        // 在此处可以执行更多的栅格数据分析操作，如读取像素值、计算统计信息等
        // 读取像素值
        double[] pixelValue = new double[bandCount];
        for (int i = 1; i <= bandCount; i++) {
            Band band = dataset.GetRasterBand(i);

            int xOff = 0; // 读取起始的列偏移
            int yOff = 0; // 读取起始的行偏移
            int xSize = width; // 读取宽度（以像素计）
            int ySize = height; // 读取高度（以像素计）
            double[] buffer = new double[width * height]; // 用于存储读取数据的缓冲区，类型需与GDT_Float64匹配
            int bufXSize = width; // 缓冲区宽度
            int bufYSize = height; // 缓冲区高度
            int bufType = gdalconst.GDT_Float64; // 数据类型常量

            band.ReadRaster(xOff, yOff, xSize, ySize, bufXSize, bufYSize, bufType, buffer);
            pixelValue[i - 1] = buffer[0];
            System.out.println("波段" + i + "的第一个像素值：" + pixelValue[i - 1]);
            // for (int j = 0; j < width * height; j++) {
            //     System.out.println("像素值：" + buffer[j]);
            // }
            // band.delete();
        }

        // 计算统计信息
        // 初始化四个数组分别存放最小值、最大值、均值和标准差
        double[] minStat = new double[1];
        double[] maxStat = new double[1];
        double[] meanStat = new double[1];
        double[] stddevStat = new double[1];
        for (int i = 1; i <= bandCount; i++) {
            Band band = dataset.GetRasterBand(i);

            // 调用自定义方法计算统计信息，要求精确统计，不使用额外参数，不使用进度回调
            band.ComputeStatistics(false, minStat, maxStat, meanStat, stddevStat); // false: 表示需要精确统计，不接受近似值。true: 允许计算时忽略无效的像素（NoData）值。

            // 输出统计结果
            System.out.printf("Minimum Value: %.2f%n", minStat[0]);
            System.out.printf("Maximum Value: %.2f%n", maxStat[0]);
            System.out.printf("Mean: %.2f%n", meanStat[0]);
            System.out.printf("Standard Deviation: %.2f%n", stddevStat[0]);
        }

        // 关闭数据集
        dataset.delete();
        // gdal.GDALDestroyDriverManager();
    }

    /**
     * 空间统计分析：栅格数据的统计分析
     */
    @Test
    public void SpatialStatistics_test() {
        gdal.AllRegister();

        String rasterFilePath = tif_path + "测试.tif";
        // String rasterFilePath = "path_to_raster_file.tif";

        Dataset dataset = gdal.Open(rasterFilePath, gdalconst.GA_ReadOnly);
        int bandCount = dataset.getRasterCount();

        // 计算统计信息
        // 初始化四个数组分别存放最小值、最大值、均值和标准差
        double[] minStat = new double[1];
        double[] maxStat = new double[1];
        double[] meanStat = new double[1];
        double[] stddevStat = new double[1];
        for (int i = 1; i <= bandCount; i++) {
            Band band = dataset.GetRasterBand(i);

            // 调用自定义方法计算统计信息，要求精确统计，不使用额外参数，不使用进度回调
            band.ComputeStatistics(false, minStat, maxStat, meanStat, stddevStat); // false: 表示需要精确统计，不接受近似值。true: 允许计算时忽略无效的像素（NoData）值。

            // 输出统计结果
            System.out.printf("Minimum Value: %.2f%n", minStat[0]);
            System.out.printf("Maximum Value: %.2f%n", maxStat[0]);
            System.out.printf("Mean: %.2f%n", meanStat[0]);
            System.out.printf("Standard Deviation: %.2f%n", stddevStat[0]);
        }

        dataset.delete();
    }
}
