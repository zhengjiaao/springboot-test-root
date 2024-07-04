package com.zja.gdal.example;

import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconst;
import org.gdal.gdalconst.gdalconstConstants;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.Arrays;

/**
 * 栅格数据文件,包括GeoTIFF、TIFF、PNG、JPEG、BMP、HFA、ECW、ECRG、ECRG_TAB、DIMAP、DIPEx、DODS、DOQ1、DOQ2、DTED、EHdr、EEDA、ENVI、EPSI、ERS、FITS、GSC、GRIB、GTM、GXF、HDF4等
 *
 * @Author: zhengja
 * @Date: 2024-06-20 15:51
 */
public class GDALRasterTest {

    String tif_path = "D:\\temp\\tif\\";

    /**
     * 创建一个空的栅格数据文件
     */
    @Test
    public void CreateEmptyRaster_test() {
        // 初始化GDAL
        gdal.AllRegister();

        // 创建栅格数据文件
        String rasterFile = "target/empty_raster_image1.tif";
        String driverName = "GTiff"; // GeoTIFF格式
        int width = 100; // 宽度
        int height = 100; // 高度
        int bands = 1; // 波段数
        int dataType = gdalconst.GDT_Float32;

        Dataset rasterDataset = gdal.GetDriverByName(driverName).Create(rasterFile, width, height, bands, dataType);

        // 设置栅格数据的空间参考和地理转换参数
        String targetSRS = "EPSG:4326"; // WGS84坐标系
        rasterDataset.SetProjection(targetSRS);

        double[] geoTransform = new double[6];
        // 设置地理转换参数（示例中将图像左上角的像素中心置于经度0、纬度0的位置）
        geoTransform[0] = -180.0; // 左上角经度
        geoTransform[1] = 360.0 / width; // 像素宽度
        geoTransform[2] = 0.0; // 旋转参数
        geoTransform[3] = 90.0; // 左上角纬度
        geoTransform[4] = 0.0; // 旋转参数
        geoTransform[5] = -180.0 / height; // 像素高度（负值表示纬度递减）

        rasterDataset.SetGeoTransform(geoTransform);

        // 关闭栅格数据文件
        rasterDataset.delete();
    }

    @Test
    public void CreateEmptyRaster_test2() {
        gdal.AllRegister();

        // 定义新影像的参数
        String format = "GTiff"; // GeoTIFF格式
        int xSize = 200; // 宽度
        int ySize = 200; // 高度
        int bands = 3; // 波段数，例如RGB影像为3
        int dataType = gdalconstConstants.GDT_Byte; // 数据类型，这里是8位无符号整型

        // 获取驱动
        Driver driver = gdal.GetDriverByName(format);
        if (driver == null) {
            System.err.println("无法找到驱动: " + format);
            return;
        }

        // 创建新的数据集
        String outputFile = "target/empty_raster_image2.tif";
        Dataset dataset = driver.Create(outputFile, xSize, ySize, bands, dataType);
        if (dataset == null) {
            System.err.println("创建文件失败: " + outputFile);
            return;
        }

        // 设置地理变换（这里设置为空，实际应用中应根据需求设置）
        double[] geoTransform = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, -1.0};
        dataset.SetGeoTransform(geoTransform);

        // 设置投影信息（这里设置为WGS84，实际应用中根据需求设置）
        String wkt = "GEOGCS[\"WGS 84\",DATUM[\"WGS_1984\",SPHEROID[\"WGS 84\",6378137,298.257223563,AUTHORITY[\"EPSG\",\"7030\"]],AUTHORITY[\"EPSG\",\"6326\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9122\"]],AXIS[\"Latitude\",NORTH],AXIS[\"Longitude\",EAST],AUTHORITY[\"EPSG\",\"4326\"]]";
        dataset.SetProjection(wkt);

        // 清理并关闭数据集
        dataset.delete();
        dataset.delete();

        System.out.println("空白影像文件创建成功: " + outputFile);
    }

    /**
     * 创建栅格数据文件
     */
    @Test
    public void CreateRaster_test() {
        // 初始化GDAL
        gdal.AllRegister();

        // 创建栅格数据文件
        String rasterFile = "target/raster_image.tif";
        int width = 500;
        int height = 500;
        int bands = 1;
        int dataType = gdalconst.GDT_Float32;

        Dataset rasterDataset = gdal.GetDriverByName("GTiff").Create(rasterFile, width, height, bands, dataType);

        // 写入非空的像素值
        float[] pixelValues = new float[width * height];
        for (int i = 0; i < pixelValues.length; i++) {
            pixelValues[i] = i + 1; // 设置像素值为从1递增的序列
        }
        rasterDataset.GetRasterBand(1).WriteRaster(0, 0, width, height, pixelValues);

        // 设置栅格数据的空间参考和地理转换参数
        String targetSRS = "EPSG:4326"; // WGS84坐标系
        rasterDataset.SetProjection(targetSRS);

        double[] geoTransform = new double[6];
        // 设置地理转换参数（示例中将图像左上角的像素中心置于经度0、纬度0的位置）
        geoTransform[0] = -180.0; // 左上角经度
        geoTransform[1] = 360.0 / width; // 像素宽度
        geoTransform[2] = 0.0; // 旋转参数
        geoTransform[3] = 90.0; // 左上角纬度
        geoTransform[4] = 0.0; // 旋转参数
        geoTransform[5] = -180.0 / height; // 像素高度（负值表示纬度递减）

        rasterDataset.SetGeoTransform(geoTransform);

        // 关闭栅格数据文件
        rasterDataset.delete();
    }

    /**
     * 读取 遥感影像文件
     */
    @Test
    public void readImage_test() {
        gdal.AllRegister();

        String imageFile = Paths.get(tif_path, "test", "image.tif").toString();
        // 打开数据集
        Dataset dataset = gdal.Open(imageFile, gdalconstConstants.GA_ReadOnly);
        if (dataset == null) {
            System.err.println("无法打开文件: " + imageFile);
            return;
        }

        // 获取并打印基本元数据
        int width = dataset.getRasterXSize();
        int height = dataset.getRasterYSize();
        int bandCount = dataset.getRasterCount();
        int[] pixels = new int[width * height]; // 像素
        System.out.println("驱动名称: " + dataset.GetDriver().getShortName());
        System.out.println("宽度x高度: " + width + "x" + height);
        System.out.println("波段数: " + bandCount);

        // 基本元数据：获取栅格数据的空间参考和地理转换参数
        String targetSRS = dataset.GetProjection();
        System.out.println("targetSRS=" + targetSRS);
        double[] geoTransform = new double[6];
        dataset.GetGeoTransform(geoTransform);
        System.out.println("geoTransform 仿射变换参数:"+Arrays.toString(geoTransform));
        System.out.println("geoTransform=" + geoTransform[0] + "," + geoTransform[1] + "," + geoTransform[2] + "," + geoTransform[3] + "," + geoTransform[4] + "," + geoTransform[5]);

        System.out.println("=================================================");

        // 遍历所有波段并打印基本信息
        for (int bandIndex = 1; bandIndex <= dataset.GetRasterCount(); bandIndex++) {
            Band band = dataset.GetRasterBand(bandIndex);

            // 打印波段信息
            System.out.println("波段 Band_index：" + bandIndex);
            System.out.println("像素类型  Data type: " + band.getDataType());

            // 可选：查看某个波段的统计数据 实现1（如果已计算）
            double[] min = new double[1]; // 单独的最小值数组
            double[] max = new double[1]; // 单独的最大值数组
            double[] mean = new double[1]; // 单独的平均值数组
            double[] stddev = new double[1]; // 单独的标准差数组
            // 获取波段统计信息，确保所有输出参数都不为null, 返回值：0=false, 1=true
            // if (band.GetStatistics(0, 1, min, max, mean, stddev) == gdalconstConstants.CE_None) {
            if (band.ComputeStatistics(false, min, max, mean, stddev) == gdalconstConstants.CE_None) {
                System.out.printf("波段 %d 统计: 最小值=%.3f, 最大值=%.3f, 平均=%.3f, 标准差=%.3f%n", bandIndex, min[0], max[0], mean[0], stddev[0]);
            } else {
                System.out.println("获取波段 " + bandIndex + " 统计数据失败。");
            }

            // 可选：查看某个波段的统计数据 实现2（如果已计算）
            double[] minMax = new double[2]; // 用于存储最小值和最大值
            band.ComputeRasterMinMax(minMax, 0); // 第二个参数为approx_ok，设为false避免使用近似值
            double minValue = minMax[0];
            double maxValue = minMax[1];

            System.out.println("Band " + bandIndex + " Min: " + minValue + ", Max: " + maxValue);

            // 可选：计算波段的统计数据 实现3
            double[] stats = new double[2];
            band.ComputeBandStats(stats);
            System.out.println("Band " + bandIndex + " Stats: 平均=" + stats[0] + ", 标准差=" + stats[1]);
            System.out.println("----------------------------------------------------");

        }

        // 关闭数据集
        dataset.delete();
    }

    /**
     * 读取一个TIFF文件，并将给定的地图坐标转换为该栅格图像的像素坐标
     * <p>
     * 参考代码：https://blog.csdn.net/wzw114/article/details/120616448
     */
    @Test
    public void TifCoordinateConverter_test() {
        // 初始化GDAL库
        gdal.AllRegister();

        // 打开TIFF文件
        // String tifFilePath = "path/to/your/tif/file.tif";
        String tifFilePath = Paths.get(tif_path, "test", "Demo.tif").toString();
        Dataset dataset = gdal.Open(tifFilePath, gdalconstConstants.GA_ReadOnly);
        if (dataset == null) {
            System.err.println("Could not open the file: " + tifFilePath);
            return;
        }

        String targetSRS = dataset.GetProjection();
        System.out.println("targetSRS=" + targetSRS);

        // 获取栅格数量
        int bandCount = dataset.getRasterCount();

        // 获取栅格数据的地理边界
        double[] geoTransform = new double[6];
        dataset.GetGeoTransform(geoTransform);

        // 计算栅格的左上角和右下角地理坐标
        double ULx = geoTransform[0]; // ULx 和 ULy 分别代表栅格图像左上角的经度和纬度坐标。
        double ULy = geoTransform[3] + dataset.GetRasterYSize() * geoTransform[5];
        double LRx = ULx + dataset.GetRasterXSize() * geoTransform[1]; // LRx 和 LRy 分别代表栅格图像右下角的经度和纬度坐标。
        double LRy = geoTransform[3];

        // 假设我们要转换的地理坐标（这里以经纬度为例）
        // double mapX = YOUR_LONGITUDE; // 地图经度坐标
        // double mapY = YOUR_LATITUDE;  // 地图纬度坐标
        // 注意：经纬度需要在栅格数据空间范围内
        double mapX = 86.053; // 地图经度坐标
        double mapY = 16.529;  // 地图纬度坐标

        // todo 校验不通过
        // 校验经纬度是否在栅格范围内 mapY 的检查就确保了它位于栅格图像的上下边界之间，mapX 的检查确保了它位于栅格图像的左右边界之间
        // boolean isValid = (mapX >= ULx && mapX <= LRx) && (mapY >= LRy && mapY <= ULy);
        /*boolean isValid = (mapX >= ULx && mapX <= LRx) && (mapY <= ULy && mapY >= LRy);
        if (!isValid) {
            System.out.println("ULx：" + ULx + " ，ULy：" + ULy + " ，LRx：" + LRx + " ，LRy：" + LRy);
            System.err.println("The given coordinate is out of the raster's spatial extent.");
            return;
        }*/

        // 转换地理坐标到像素坐标
        int pixel = (int) ((mapX - geoTransform[0]) / geoTransform[1]);
        int line = (int) ((mapY - geoTransform[3]) / geoTransform[5]);

        // 注意：这里的计算假设了正北方向和地图的坐标系，实际情况可能需要根据geoTransform数组调整计算逻辑

        // 输出结果 映射坐标（%f，%f）位于像素（%d，%d）
        System.out.printf("Map coordinate (%f, %f) is at pixel (%d, %d)%n", mapX, mapY, pixel, line);

        // ReadRaster 方法
        for (int bandIndex = 1; bandIndex <= bandCount; bandIndex++) {
            Band band = dataset.GetRasterBand(bandIndex);

            double[] values = new double[1];
            // xoff 想要读取的部分原点位置横图像坐标，yoff 想要读取的部分原点位置纵图像坐标
            // xsize 指定要读取部分图像的矩形边长，ysize 指定要读取部分图像的矩形边长。
            // array 用来存储读取到的数据的数组。
            // public int ReadRaster(int xoff, int yoff, int xsize, int ysize, double[] array)
            band.ReadRaster(pixel, line, 1, 1, values);
            double value = values[0]; // 读取到的像素值
            System.out.println("横坐标：" + mapX + "," + "纵坐标:" + mapY);
            System.out.println("Band" + bandIndex + " ，像素值value: " + value);
        }

        // 清理资源
        dataset.delete();
    }

}
