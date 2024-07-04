package com.zja.gdal.example;

import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconst;
import org.gdal.gdalconst.gdalconstConstants;
import org.gdal.ogr.*;
import org.gdal.osr.CoordinateTransformation;
import org.gdal.osr.SpatialReference;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * 地理空间分析
 * <p>
 * 使用Java GDAL执行地理空间分析，可以使用其功能来处理和分析栅格和矢量数据
 *
 * @Author: zhengja
 * @Date: 2024-06-18 15:48
 */
public class GeospatialAnalysisTest {

    // 使用Java GDAL执行地理空间分析

    String tif_path = "D:\\temp\\tif\\";
    String shp_path = "D:\\temp\\shp\\";

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
     * 读取和显示矢量数据
     */
    @Test
    public void VectorAnalysis_test() {
        ogr.RegisterAll(); // 注册所有支持的矢量数据格式

        String vectorFile = shp_path + "shp/水系.shp";
        // String vectorFile = "path/to/vector.shp";
        DataSource dataSource = ogr.Open(vectorFile);

        int layerCount = dataSource.GetLayerCount();
        System.out.println("矢量数据信息：");
        System.out.println("图层数：" + layerCount);

        System.out.println("图层信息：");
        for (int i = 0; i < layerCount; i++) {
            Layer layer = dataSource.GetLayer(i);

            String layerName = layer.GetName();
            long featureCount = layer.GetFeatureCount();
            System.out.println("图层" + i + "的名称：" + layerName);
            // System.out.println("图层名称：" + dataSource.GetLayer(0).GetName());

            // 在此处可以执行更多的矢量数据分析操作，如遍历要素、计算属性值等
            System.out.println("图层" + i + "的要素数量：" + featureCount);
            for (int j = 0; j < featureCount; j++) {
                Feature feature = layer.GetFeature(j);
                Geometry geometry = feature.GetGeometryRef();
                System.out.println("要素" + j + "的几何类型：" + geometry.GetGeometryName());
                System.out.println("要素" + j + "的属性值：" + feature.GetFieldAsString(0));
            }
        }

        // 关闭数据源
        dataSource.delete();
    }

    /**
     * 空间查询和筛选
     * <p>
     * 该方法演示了如何使用GDAL的OGR库对shapefile进行空间查询。
     * 它首先注册所有OGR数据源驱动，然后打开一个指定路径的shapefile。
     * 接着，它创建一个几何对象用于定义查询区域，并将其设置为图层的空间过滤器。
     * 最后，它遍历图层中的每个特征，打印出满足空间过滤器条件的特征的ID。
     */
    @Test
    public void SpatialQuery_test1() {
        ogr.RegisterAll();

        // 指定待查询的shapefile路径
        // String shapefilePath = "path_to_shapefile.shp";
        String shapefilePath = Paths.get(shp_path, "test", "上海润和总部园.shp").toString();

        // 打开shapefile数据源
        DataSource dataset = ogr.Open(shapefilePath);

        // 获取第一个图层
        Layer layer = dataset.GetLayer(0);

        // 创建一个点几何对象，用于定义查询区域
        // Geometry queryGeometry = ogr.CreateGeometryFromWkt("POINT (x y)");
        // 相交 的点
        Geometry queryGeometry = ogr.CreateGeometryFromWkt("Point (121.60343781591875256 31.19171304548682855)");
        // 不相交 的点
        // Geometry queryGeometry = ogr.CreateGeometryFromWkt("Point (121.60595729 31.19262829)");

        // 设置图层的空间过滤器，只返回与查询几何对象相交的特征
        layer.SetSpatialFilter(queryGeometry);

        Feature feature;
        while ((feature = layer.GetNextFeature()) != null) {
            // 处理满足查询条件的要素
            System.out.println("Feature ID: " + feature.GetFID());
            Geometry geometry = feature.GetGeometryRef();
            if (geometry != null) {
                System.out.println("Geometry: " + geometry.ExportToWkt());
            }
            // 访问要素属性等
            System.out.println("Name: " + feature.GetFieldAsString("name"));

            feature.delete();
        }

        dataset.delete();
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

    /**
     * 栅格数据的坐标系转换和重投影(坐标转换和投影变换)
     */
    @Test
    @Deprecated
    public void tif_CoordinateTransform_test() {
        // 注册驱动
        gdal.AllRegister(); // 注册所有支持的栅格数据格式驱动，让GDAL能够处理各种栅格数据。默认情况下，GDAL支持的栅格数据格式包括：GeoTIFF、VRT、HDF5、HDF5E等。
        ogr.RegisterAll(); // 注册所有支持的矢量数据格式驱动，让OGR能够处理各种矢量数据。默认情况下，GDAL支持的矢量数据格式包括：Shapefile、GeoJSON、KML、KMZ等。

        String rasterFile = "path/to/raster.tif";
        String vectorFile = "path/to/vector.shp";
        String transformedFilePath = "path/to/transformed.tif";
        String reprojectedFilePath = "path/to/reprojected.tif";

        // 打开数据
        Dataset rasterDataset = gdal.Open(rasterFile); // 打开栅格数据集
        DataSource vectorDataSource = ogr.Open(vectorFile); // 打开矢量数据集

        // 进行投影变换（坐标变换）
        Dataset transformedDataset = rasterDataset.GetDriver().CreateCopy(transformedFilePath, rasterDataset); // 创建栅格数据集的副本，保留原数据内容但准备修改其坐标系统。
        transformedDataset.SetProjection(vectorDataSource.GetLayer(0).GetSpatialRef().ExportToWkt()); // 设置副本的投影信息为第一个矢量图层的投影（通过WKT形式）。
        transformedDataset.SetGeoTransform(vectorDataSource.GetLayer(0).GetExtent()); // 使用矢量图层的范围设置副本的地理变换参数

        // 进行重投影
        Dataset reprojectedDataset = gdal.AutoCreateWarpedVRT(transformedDataset, null, "EPSG:4326", gdalconstConstants.GRA_Bilinear); // 自动创建一个虚拟重投影文件（VRT），该函数会根据指定的目标投影（本例中为WGS84，即EPSG:4326）和重采样算法（双线性插值）来准备数据重投影
        reprojectedDataset.GetDriver().CreateCopy(reprojectedFilePath, reprojectedDataset); // 基于这个虚拟的重投影数据集创建一个新的、物理存储的栅格文件 reprojected.tif

        // 关闭数据集和数据源
        rasterDataset.delete();
        transformedDataset.delete();
        reprojectedDataset.delete();
        vectorDataSource.delete();
    }

    /**
     * 栅格数据的坐标系转换和重投影(坐标转换和投影变换)
     */
    @Test
    @Deprecated
    public void tif_CoordinateTransform_test2() {
        // 注册GDAL/OGR的驱动
        gdal.AllRegister();
        ogr.RegisterAll();

        String rasterFilePath = "path/to/raster.tif";
        String vectorFilePath = "path/to/vector.shp";
        String transformedFilePath = "path/to/transformed.tif";
        String reprojectedFilePath = "path/to/reprojected.tif";

        // 打开栅格数据集，自动关闭
        Dataset rasterDataset = gdal.Open(rasterFilePath);
        // 打开矢量数据集，自动关闭
        DataSource vectorDataSource = ogr.Open(vectorFilePath);

        // 创建栅格数据集的副本并设置投影信息
        Vector<String> options = new Vector<String>();
        Dataset transformedDataset = rasterDataset.GetDriver().CreateCopy(transformedFilePath,           // 目标文件路径
                rasterDataset,                // 源数据集
                options                       // 可选的创建选项数组，可以根据需要添加特定的创建参数
        );

        transformedDataset.SetProjection(vectorDataSource.GetLayer(0).GetSpatialRef().ExportToWkt());

        // 不直接设置GeoTransform，因为通常需要根据矢量数据的实际边界和分辨率来计算，这里仅作示例，实际应用中需适当处理
        // transformedDataset.SetGeoTransform(...);

        // 自动创建重投影的虚拟文件并复制到物理文件
        Dataset reprojectedVRT = gdal.AutoCreateWarpedVRT(transformedDataset, null, "EPSG:4326", gdalconstConstants.GRA_Bilinear);
        Dataset reprojectedDataset = reprojectedVRT.GetDriver().CreateCopy(reprojectedFilePath, reprojectedVRT);

        // 关闭资源
        reprojectedDataset.delete();
        reprojectedVRT.delete();
        transformedDataset.delete();
        rasterDataset.delete();
        vectorDataSource.delete();
    }

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
