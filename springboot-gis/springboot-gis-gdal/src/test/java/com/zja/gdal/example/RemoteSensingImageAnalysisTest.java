package com.zja.gdal.example;

import com.sun.xml.internal.messaging.saaj.soap.Envelope;
import org.gdal.gdal.*;
import org.gdal.gdalconst.gdalconst;
import org.gdal.gdalconst.gdalconstConstants;
import org.gdal.ogr.Geometry;
import org.gdal.ogr.ogr;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Paths;
import java.util.Vector;

/**
 * 遥感影像分析
 * <p>
 * 使用Java GDAL进行遥感影像分析, 包括图像分类、图像分割、图像匹配等。
 *
 * @Author: zhengja
 * @Date: 2024-06-18 16:12
 */
@Deprecated
public class RemoteSensingImageAnalysisTest {

    String tif_path = "D:\\temp\\tif\\";

    /**
     * 影像分类：将遥感影像数据分类为不同的地物类型，例如土地覆盖分类。
     * <p>
     * 注：由于GDAL本身并不直接支持影像分类算法，而Java生态中也没有现成的、广泛使用的GDAL绑定直接包含高级机器学习功能，因此一个完整的从头到尾的实例代码会涉及到GDAL用于数据读取与写入，以及另一个机器学习库（如Weka、deeplearning4j等）进行分类。
     */
    @Test
    @Deprecated // todo GDAL本身并不直接支持影像分类算法
    public void ImageClassification_test() {
        gdal.AllRegister(); // 注册所有支持的栅格数据格式

        // String imageFile = tif_path + "path/to/image.tif";
        String imageFile = Paths.get(tif_path, "test", "image.tif").toString();
        Dataset dataset = gdal.Open(imageFile);

        int width = dataset.getRasterXSize();
        int height = dataset.getRasterYSize();
        int bandCount = dataset.getRasterCount();

        // 读取每个波段的像素值
        Band band;
        for (int i = 1; i <= bandCount; i++) {
            band = dataset.GetRasterBand(i);

            int[] pixels = new int[width * height];
            band.ReadRaster(0, 0, width, height, pixels);

            // 在此处可以执行像素值的分类操作，将不同地物类型分配给不同的像素值
            // for (int j = 0; j < pixels.length; j++) {
            //     // 对像素值进行分类操作
            //     if (pixels[j] < 100) {
            //         pixels[j] = 0;
            //     } else if (pixels[j] >= 100 && pixels[j] < 200) {
            //         pixels[j] = 1;
            //     } else {
            //         pixels[j] = 2;
            //     }
            // }
            // band.WriteRaster(0, 0, width, height, pixels);
            // band.FlushCache();
            // band.SetNoDataValue(0);
            // band.SetDescription("Classification");
            // band.SetMetadataItem("STATISTICS_MINIMUM", String.valueOf(band.GetMinimum()));

            // 释放资源
            band.delete();
        }

        // 关闭数据集
        dataset.delete();
    }

    /**
     * 影像增强：对遥感影像进行增强处理，例如直方图均衡化、拉伸等
     */
    @Test
    @Deprecated
    public void ImageEnhancement_test() {
        gdal.AllRegister(); // 注册所有支持的栅格数据格式

        // String imageFile = "path/to/image.tif";
        String imageFile = Paths.get(tif_path, "test", "image2.tif").toString();
        Dataset dataset = gdal.Open(imageFile, gdalconst.GA_Update);

        if (dataset == null) {
            System.out.println("无法打开文件：" + imageFile);
            return;
        }

        int width = dataset.getRasterXSize();
        int height = dataset.getRasterYSize();
        int bandCount = dataset.getRasterCount();

        // 对每个波段执行增强处理
        Band band;
        double[] minMax = new double[2]; // 用于存储最小值和最大值
        for (int i = 1; i <= bandCount; i++) {
            band = dataset.GetRasterBand(i);

            band.ComputeRasterMinMax(minMax, 0); // 第二个参数为approx_ok，设为false避免使用近似值

            double minValue = minMax[0];
            double maxValue = minMax[1];

            // 清除颜色表，如果有
            // band.SetColorTable(null); // 不支持多样本TIFF文件

            // 设置无效值（No Data Value）
            band.SetNoDataValue(0);

            // 现在你有了minValue和maxValue，可以进行后续的图像处理操作...
            // 在此处可以执行其他的影像增强操作，如直方图均衡化、拉伸、对比度调整等
            // 注意，GDAL本身并不直接提供直方图均衡化的API，你可能需要自己实现这个功能
            // 例如，打印最小值和最大值
            System.out.println("Band " + i + " Min: " + minValue + ", Max: " + maxValue);

            // 释放资源
            band.delete();
        }

        // 关闭数据集
        dataset.delete();
    }

    /*@Test
    public void ImageEnhancement_test2() {
        gdal.AllRegister();

        String inputFile = "path/to/your/image.tif";
        String outputFile = "path/to/output/image_stretched.tif";

        // 打开原始影像
        Dataset inputDataset = gdal.Open(inputFile, gdalconstConstants.GA_ReadOnly);
        if (inputDataset == null) {
            System.err.println("无法打开文件: " + inputFile);
            return;
        }

        // 创建一个新的数据集作为输出
        Dataset outputDataset = gdal.GetDriverByName("GTiff").CreateCopy(outputFile, inputDataset, 0);
        if (outputDataset == null) {
            System.err.println("创建输出文件失败: " + outputFile);
            return;
        }

        // 对每个波段进行线性拉伸
        for (int bandIndex = 1; bandIndex <= inputDataset.GetRasterCount(); bandIndex++) {
            Band inputBand = inputDataset.GetRasterBand(bandIndex);
            Band outputBand = outputDataset.GetRasterBand(bandIndex);

            // 获取当前波段的最小值和最大值
            double[] minMax = new double[2];
            inputBand.ComputeRasterMinMax(minMax);
            double min = minMax[0];
            double max = minMax[1];


            int dataType = inputBand.getDataType();
            int pixelSize = GDALGetDataTypeSizeBytes(dataType); // 假设存在这样一个方法获取数据类型字节数，实际中需要根据GDAL的API调整

            // 确保使用正确的字节顺序
            ByteBuffer buffer = ByteBuffer.allocateDirect(inputDataset.getRasterXSize() * pixelSize).order(ByteOrder.nativeOrder());

            // 线性拉伸到0-255范围（假设是8位无符号整型）
            for (int i = 0; i < inputDataset.getRasterYSize(); i++) {
                double[] scanline = new double[inputDataset.getRasterXSize()];
                // inputBand.ReadRaster(0, i, inputDataset.getRasterXSize(), 1, scanline, inputDataset.getRasterXSize(), 1, 0, 0);
                // 读取扫描线
                inputBand.ReadRaster(0, i, inputDataset.getRasterXSize(), 1, buffer, inputDataset.getRasterXSize(), pixelSize, 0, 0);
                for (int j = 0; j < scanline.length; j++) {
                    scanline[j] = ((scanline[j] - min) / (max - min)) * 255; // 线性拉伸公式
                }
                outputBand.WriteRaster(0, i, inputDataset.getRasterXSize(), 1, scanline, inputDataset.getRasterXSize(), 1, 0, 0);
            }
        }

        // 清理并关闭数据集
        inputDataset.delete();
        outputDataset.delete();

        System.out.println("影像增强完成，结果保存至: " + outputFile);
    }*/

    /**
     * 影像裁剪：根据给定的边界框对遥感影像进行裁剪。
     */
    @Test
    @Deprecated
    public void ImageClipping_test() {
        gdal.AllRegister(); // 注册所有支持的栅格数据格式

        String inputImageFile = "path/to/input_image.tif";
        String outputImageFile = "path/to/output_image.tif";
        String boundingBoxWKT = "POLYGON ((xmin ymin, xmax ymin, xmax ymax, xmin ymax, xmin ymin))";

        Dataset inputDataset = gdal.Open(inputImageFile);
        Dataset outputDataset = gdal.GetDriverByName("GTiff").Create(outputImageFile, inputDataset.getRasterXSize(), inputDataset.getRasterYSize(), inputDataset.getRasterCount());

        Geometry boundingBox = ogr.CreateGeometryFromWkt(boundingBoxWKT);
        inputDataset.SetProjection(outputDataset.GetProjection());
        outputDataset.SetProjection(inputDataset.GetProjection());
        outputDataset.SetGeoTransform(inputDataset.GetGeoTransform());


        // todo 未完成
        // Vector<String> options = new Vector<>();
        // gdal.Warp(outputDataset, inputDataset, new Dataset[]{},new WarpOptions(options));

        // 关闭数据集
        inputDataset.delete();
        outputDataset.delete();
    }

    @Test
    @Deprecated
    public void ImageClipping_test2() {
        gdal.AllRegister();

        // 输入和输出文件路径
        // String inputFile = "path/to/input/image.tif";
        // String outputFile = "path/to/output/clipped_image.tif";

        String inputFile = Paths.get(tif_path, "test", "image.tif").toString();
        String outputFile = Paths.get(tif_path, "test", "image_to_clipped_image.tif").toString();

        // 打开输入数据集
        Dataset inputDataset = gdal.Open(inputFile, gdalconstConstants.GA_ReadOnly);
        if (inputDataset == null) {
            System.err.println("无法打开输入文件: " + inputFile);
            return;
        }

        // 创建输出数据集（仅用于指定输出路径，实际裁剪操作不需要预先创建）
        Dataset outputDataset = null; // 实际上，这里不需要预先创建输出数据集

        // 定义裁剪区域，这里以像素坐标为例
        double minX = 100; // 左下角X坐标
        double minY = 100; // 左下角Y坐标
        double maxX = 200; // 右上角X坐标
        double maxY = 200; // 右上角Y坐标

        // 构建WarpOptions，注意Java绑定中可能没有直接对应的类和方法
        // Vector<String> options = new Vector<>();
        // WarpOptions warpOptions = new WarpOptions(options); // 假设的构造方式
        // warpOptions.setCutlineGeometry(new Envelope(minX, minY, maxX, maxY)); // 假设的设置裁剪区域方法

        // 执行Warp操作，注意Java中可能没有直接的Warp方法
        // 这里使用Translate方法作为替代，因为它也可以实现裁剪功能
        // "options: -projwin " + minX + " " + maxY + " " + maxX + " " + minY
        Vector<String> options = new Vector<>();
        options.add("-projwin " + minX + " " + maxY + " " + maxX + " " + minY);  // 符合GDAL的坐标顺序
        TranslateOptions translateOptions = new TranslateOptions(options);
        gdal.Translate(outputFile, inputDataset, translateOptions);

        // 清理
        if (inputDataset != null) {
            inputDataset.delete();
        }
        // 输出数据集不需要删除，因为Translate内部已处理
        System.out.println("影像裁剪完成，结果保存至: " + outputFile);
    }


    /**
     * 影像融合：将多个遥感影像融合成单个影像。
     */
    @Test
    public void ImageMerging_test() {
        gdal.AllRegister(); // 注册所有支持的栅格数据格式

        String[] inputImageFiles = {"path/to/input_image1.tif", "path/to/input_image2.tif"};
        String outputImageFile = "path/to/output_image.tif";

        Dataset[] inputDatasets = new Dataset[inputImageFiles.length];
        for (int i = 0; i < inputImageFiles.length; i++) {
            inputDatasets[i] = gdal.Open(inputImageFiles[i]);
        }

        int width = inputDatasets[0].getRasterXSize();
        int height = inputDatasets[0].getRasterYSize();
        int bandCount = inputDatasets[0].getRasterCount();

        Dataset outputDataset = gdal.GetDriverByName("GTiff").Create(outputImageFile, width, height, bandCount);

        for (int i = 0; i < bandCount; i++) {
            Band[] inputBands = new Band[inputDatasets.length];
            Band outputBand = outputDataset.GetRasterBand(i + 1);

            for (int j = 0; j < inputDatasets.length; j++) {
                inputBands[j] = inputDatasets[j].GetRasterBand(i + 1);
                // todo 未完成
                // inputBands[j].ReadRaster(0, 0, width, height, outputBand.ReadRaster(0, 0, width, height));
            }

            // 在此处可以执行影像融合算法，如平均法、波段加权融合等

            outputBand.FlushCache();
        }

        // 关闭数据集
        for (Dataset dataset : inputDatasets) {
            dataset.delete();
        }
        outputDataset.delete();
    }

    /**
     * 影像变换：对遥感影像进行几何或辐射校正，例如几何校正、大气校正等。
     */
    @Test
    public void ImageTransformation_test() {
        gdal.AllRegister(); // 注册所有支持的栅格数据格式

        String inputImageFile = "path/to/input_image.tif";
        String outputImageFile = "path/to/output_image.tif";

        Dataset inputDataset = gdal.Open(inputImageFile);
        Dataset outputDataset = gdal.GetDriverByName("GTiff").Create(outputImageFile, inputDataset.getRasterXSize(), inputDataset.getRasterYSize(), inputDataset.getRasterCount());

        // todo 未完成
        // gdal.Warp(outputDataset, inputDataset, new gdal.WarpOptions().setFormat("GTiff").setDstSRS("EPSG:4326").setSrcSRS(inputDataset.GetProjection()).setWarpOptions(new String[]{"-tps"}));

        // 关闭数据集
        inputDataset.delete();
        outputDataset.delete();
    }

    /**
     * 影像分割：将遥感影像分割成具有相似特征的区域，以便进行进一步的分析
     */
    @Test
    public void ImageSegmentation_test() {
        gdal.AllRegister(); // 注册所有支持的栅格数据格式

        String inputImageFile = "path/to/input_image.tif";
        String outputShapefile = "path/to/output_shapefile.shp";

        Dataset inputDataset = gdal.Open(inputImageFile);
        int width = inputDataset.getRasterXSize();
        int height = inputDataset.getRasterYSize();

        Band band = inputDataset.GetRasterBand(1);
        byte[] pixels = new byte[width * height];
        band.ReadRaster(0, 0, width, height, pixels);

        // todo 未完成
        // gdal.Polygonize(new Band[]{band}, null, gdal.GetDriverByName("ESRI Shapefile"), outputShapefile, 0, new String[]{},null);

        // 关闭数据集
        inputDataset.delete();
    }

    /**
     * 影像分类评估：评估影像分类的准确性和精度，例如计算混淆矩阵、生产制图精度评定等。
     */
    @Test
    public void ImageClassificationEvaluation_test() {
        gdal.AllRegister(); // 注册所有支持的栅格数据格式

        String classifiedImageFile = "path/to/classified_image.tif";
        String referenceImageFile = "path/to/reference_image.tif";

        Dataset classifiedDataset = gdal.Open(classifiedImageFile);
        Dataset referenceDataset = gdal.Open(referenceImageFile);

        int width = classifiedDataset.getRasterXSize();
        int height = classifiedDataset.getRasterYSize();
        int bandCount = classifiedDataset.getRasterCount();

        int[] classifiedPixels = new int[width * height];
        int[] referencePixels = new int[width * height];

        Band classifiedBand = classifiedDataset.GetRasterBand(1);
        Band referenceBand = referenceDataset.GetRasterBand(1);

        classifiedBand.ReadRaster(0, 0, width, height, classifiedPixels);
        referenceBand.ReadRaster(0, 0, width, height, referencePixels);

        int truePositive = 0;
        int falsePositive = 0;
        int falseNegative = 0;
        int trueNegative = 0;

        for (int i = 0; i < width * height; i++) {
            if (classifiedPixels[i] == 1 && referencePixels[i] == 1) {
                truePositive++;
            } else if (classifiedPixels[i] == 1 && referencePixels[i] == 0) {
                falsePositive++;
            } else if (classifiedPixels[i] == 0 && referencePixels[i] == 1) {
                falseNegative++;
            } else if (classifiedPixels[i] == 0 && referencePixels[i] == 0) {
                trueNegative++;
            }
        }

        double accuracy = (double) (truePositive + trueNegative) / (width * height);
        double precision = (double) truePositive / (truePositive + falsePositive);
        double recall = (double) truePositive / (truePositive + falseNegative);
        double f1Score = 2 * (precision * recall) / (precision + recall);

        System.out.println("Accuracy: " + accuracy);
        System.out.println("Precision: " + precision);
        System.out.println("Recall: " + recall);
        System.out.println("F1 Score: " + f1Score);

        // 关闭数据集
        classifiedDataset.delete();
        referenceDataset.delete();
    }

    /**
     * 影像变化检测：比较多期遥感影像，检测和分析地物变化的位置和幅度。
     */
    @Test
    public void ImageChangeDetection_Test() {
        gdal.AllRegister(); // 注册所有支持的栅格数据格式

        String preChangeImageFile = "path/to/prechange_image.tif";
        String postChangeImageFile = "path/to/postchange_image.tif";
        String outputChangeImageFile = "path/to/change_image.tif";

        Dataset preChangeDataset = gdal.Open(preChangeImageFile);
        Dataset postChangeDataset = gdal.Open(postChangeImageFile);

        int width = preChangeDataset.getRasterXSize();
        int height = preChangeDataset.getRasterYSize();
        int bandCount = preChangeDataset.getRasterCount();

        Dataset outputChangeDataset = gdal.GetDriverByName("GTiff").Create(outputChangeImageFile, width, height, bandCount);
        outputChangeDataset.SetProjection(preChangeDataset.GetProjection());
        outputChangeDataset.SetGeoTransform(preChangeDataset.GetGeoTransform());

        for (int i = 0; i < bandCount; i++) {
            Band preChangeBand = preChangeDataset.GetRasterBand(i + 1);
            Band postChangeBand = postChangeDataset.GetRasterBand(i + 1);
            Band outputChangeBand = outputChangeDataset.GetRasterBand(i + 1);

            int[] preChangePixels = new int[width * height];
            int[] postChangePixels = new int[width * height];

            preChangeBand.ReadRaster(0, 0, width, height, preChangePixels);
            postChangeBand.ReadRaster(0, 0, width, height, postChangePixels);

            int[] changePixels = new int[width * height];

            for (int j = 0; j < width * height; j++) {
                if (Math.abs(preChangePixels[j] - postChangePixels[j]) >= 10) { // 阈值可以根据具体情况调整
                    changePixels[j] = 255;
                } else {
                    changePixels[j] = 0;
                }
            }

            outputChangeBand.WriteRaster(0, 0, width, height, changePixels);
            outputChangeBand.FlushCache();
        }

        // 关闭数据集
        preChangeDataset.delete();
        postChangeDataset.delete();
        outputChangeDataset.delete();
    }

    /**
     * 影像直方图匹配：通过直方图匹配方法将一幅影像的直方图转换为另一幅影像的直方图，实现影像间的色调和对比度调整。
     */
    @Test
    public void ImageHistogramMatching_Test() {
        gdal.AllRegister(); // 注册所有支持的栅格数据格式

        String sourceImageFile = "path/to/source_image.tif";
        String referenceImageFile = "path/to/reference_image.tif";
        String outputImageFile = "path/to/output_image.tif";

        Dataset sourceDataset = gdal.Open(sourceImageFile);
        Dataset referenceDataset = gdal.Open(referenceImageFile);

        int width = sourceDataset.getRasterXSize();
        int height = sourceDataset.getRasterYSize();
        int bandCount = sourceDataset.getRasterCount();

        Dataset outputDataset = gdal.GetDriverByName("GTiff").Create(outputImageFile, width, height, bandCount);
        outputDataset.SetProjection(sourceDataset.GetProjection());
        outputDataset.SetGeoTransform(sourceDataset.GetGeoTransform());

        for (int i = 0; i < bandCount; i++) {
            Band sourceBand = sourceDataset.GetRasterBand(i + 1);
            Band referenceBand = referenceDataset.GetRasterBand(i + 1);
            Band outputBand = outputDataset.GetRasterBand(i + 1);

            int[] sourcePixels = new int[width * height];
            int[] referencePixels = new int[width * height];

            sourceBand.ReadRaster(0, 0, width, height, sourcePixels);
            referenceBand.ReadRaster(0, 0, width, height, referencePixels);

            int[] outputPixels = new int[width * height];

            int dataType = sourceBand.getDataType();
            int[] sourceHistogram = new int[gdal.GetDataTypeSize(dataType) * 256];
            int[] referenceHistogram = new int[gdal.GetDataTypeSize(dataType) * 256];

            sourceBand.GetHistogram(0, 255, sourceHistogram);
            referenceBand.GetHistogram(0, 255, referenceHistogram);

            int[] sourceCumulativeHistogram = calculateCumulativeHistogram(sourceHistogram);
            int[] referenceCumulativeHistogram = calculateCumulativeHistogram(referenceHistogram);

            for (int j = 0; j < width * height; j++) {
                int sourcePixelValue = sourcePixels[j];
                int matchedPixelValue = matchPixelValue(sourcePixelValue, sourceCumulativeHistogram, referenceCumulativeHistogram);
                outputPixels[j] = matchedPixelValue;
            }

            outputBand.WriteRaster(0, 0, width, height, outputPixels);
            outputBand.FlushCache();
        }

        // 关闭数据集
        sourceDataset.delete();
        referenceDataset.delete();
        outputDataset.delete();
    }

    private static int[] calculateCumulativeHistogram(int[] histogram) {
        int[] cumulativeHistogram = new int[histogram.length];
        cumulativeHistogram[0] = histogram[0];

        for (int i = 1; i < histogram.length; i++) {
            cumulativeHistogram[i] = cumulativeHistogram[i - 1] + histogram[i];
        }

        return cumulativeHistogram;
    }

    private static int matchPixelValue(int sourceValue, int[] sourceCumulativeHistogram, int[] referenceCumulativeHistogram) {
        int matchedValue = 0;
        int sourceSum = sourceCumulativeHistogram[sourceValue];

        for (int i = 0; i < referenceCumulativeHistogram.length; i++) {
            if (referenceCumulativeHistogram[i] >= sourceSum) {
                matchedValue = i;
                break;
            }
        }

        return matchedValue;
    }

    /**
     * 影像裁剪：根据指定的地理范围裁剪遥感影像，获取感兴趣区域的子图像。
     */
    @Test
    public void ImageCropping_Test() {
        gdal.AllRegister(); // 注册所有支持的栅格数据格式

        String inputImageFile = "path/to/input_image.tif";
        String outputImageFile = "path/to/output_image.tif";

        double minX = 10.0; // 最小经度
        double minY = 20.0; // 最小纬度
        double maxX = 30.0; // 最大经度
        double maxY = 40.0; // 最大纬度

        Dataset inputDataset = gdal.Open(inputImageFile);

        double[] geoTransform = inputDataset.GetGeoTransform();
        double pixelWidth = geoTransform[1];
        double pixelHeight = geoTransform[5];

        int startX = (int) Math.floor((minX - geoTransform[0]) / pixelWidth);
        int startY = (int) Math.floor((maxY - geoTransform[3]) / pixelHeight);
        int width = (int) Math.ceil((maxX - minX) / pixelWidth);
        int height = (int) Math.ceil((maxY - minY) / pixelHeight);

        String[] options = new String[]{"-srcwin", Integer.toString(startX), Integer.toString(startY), Integer.toString(width), Integer.toString(height)};

        // 1 计算切片级别和范围
        int zoomLevel = 10;
        int tileSize = 256;
        int[] tileRange = {0, 0, 10, 10};

        // todo 未完成
        TranslateOptions translateOptions = null; // new TranslateOptionsBuilder().setOptions(options).build();
        // TranslateOptions translateOptions = new TranslateOptions(new String[]{"-of","PNG","-outsize",String.valueOf(tileSize),String.valueOf(tileSize)});
        Dataset outputDataset = gdal.Translate(outputImageFile, inputDataset, translateOptions);

        // 关闭数据集
        inputDataset.delete();
        outputDataset.delete();
    }

}
