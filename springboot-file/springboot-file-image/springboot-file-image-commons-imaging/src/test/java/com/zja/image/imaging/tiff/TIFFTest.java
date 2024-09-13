package com.zja.image.imaging.tiff;

import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.ImagingException;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants.*;

/**
 * @Author: zhengja
 * @Date: 2024-09-13 9:49
 */
public class TIFFTest {

    // 多页 2页
    static final String image_path = Paths.get("D:\\temp\\images\\test", "4.tif").toString();
    // 单页
//    static final String image_path = Paths.get("D:\\temp\\images\\test", "5.tif").toString();

    // 读取 TIFF 图像文件
    @Test
    public void test_1() {
        // 读取 TIFF 图像文件
        File tiffFile = new File(image_path);
        try {
            TiffImageMetadata metadata = (TiffImageMetadata) Imaging.getMetadata(tiffFile);

            // 输出 TIFF 元数据信息
            if (metadata != null) {
                System.out.println("Image Metadata:" + metadata.toString());

                metadata.getAllFields().forEach(field -> {
                    try {
                        System.out.println("Field: " + field.getTagName() + ", Value: " + field.getValue());
                    } catch (ImagingException e) {
                        throw new RuntimeException(e);
                    }
                });

                /*for (ImageMetadata.ImageMetadataItem item : metadata.getItems()) {
                    System.out.println(item.getKeyword() + ": " + item.getStringValue());
                }*/
            }

            // 编辑和写入 TIFF 图像文件
//            TiffOutputSet outputSet = new TiffOutputSet();
            // 在此处添加编辑图像的代码
            // 示例：设置新的分辨率
//            outputSet.setResolution(300, 300); // 设置为 300 DPI
//            outputSet.setGpsInDegrees();

            // 将编辑后的图像写入新的 TIFF 文件
//            File outputFile = new File("output.tif");
//            Imaging.writeImage(outputSet, outputFile, ImageWriteException.class);
//            Imaging.writeImage(outputSet, outputFile);

            System.out.println("TIFF 图像处理完成！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 读取 TIFF 图像文件
    @Test
    public void test_2() {
        // 读取 TIFF 图像文件
        File tiffFile = new File(image_path);
        try {
            // 读取 TIFF 图像文件到内存中
            BufferedImage image = Imaging.getBufferedImage(tiffFile);

            ImageFormat imageFormat = Imaging.guessFormat(tiffFile);

            // 写入 TIFF 图像文件
            File outputTiffFile = new File("output.tif");
            Imaging.writeImage(image, outputTiffFile, imageFormat);

            System.out.println("TIFF 图像文件读取和写入完成！");

            // 解析 TIFF 元数据
            TiffImageMetadata metadata = (TiffImageMetadata) Imaging.getMetadata(tiffFile);
            int width = metadata.findField(TIFF_TAG_IMAGE_WIDTH).getIntValue();
            int height = metadata.findField(TIFF_TAG_IMAGE_LENGTH).getIntValue();
            System.out.println("Image Size: " + width + "x" + height);

//            RationalNumber xResolution = metadata.findField(TIFF_TAG_XRESOLUTION).getRationalNumber();
//            RationalNumber yResolution = metadata.findField(TIFF_TAG_YRESOLUTION).getRationalNumber();

//            RationalNumber xResolution = RationalNumber.valueOf(metadata.findField(TIFF_TAG_XRESOLUTION).getDoubleValue());
//            RationalNumber yResolution = RationalNumber.valueOf(metadata.findField(TIFF_TAG_YRESOLUTION).getDoubleValue());

//            double xPosition = metadata.findField(TIFF_TAG_XPOSITION).getDoubleValue();
//            double yPosition = metadata.findField(TIFF_TAG_YPOSITION).getDoubleValue();
//
//            System.out.println("X Position: " + xPosition + " dpi");
//            System.out.println("Y yPosition: " + yPosition + " dpi");

            // 编辑和转换 TIFF 图像
            // 在这里可以进行裁剪、旋转、缩放、调整亮度和对比度等操作

            // 处理 TIFF 图像的色彩信息
            // 在这里可以处理色彩信息，如转换色彩模式、应用滤镜等

            // 处理 TIFF 图像的压缩
            // 可以对 TIFF 图像进行压缩和解压缩操作

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 写入 TIFF 图像文件
    @Test
    public void test_3() {
        // 读取 TIFF 图像文件
        File tiffFile = new File(image_path);
        try {
            // 编辑和转换 TIFF 图像
            // 在这里可以进行裁剪、旋转、缩放、调整亮度和对比度等操作
            // 这里仅展示了对图像进行水平翻转的操作
            BufferedImage image = Imaging.getBufferedImage(tiffFile);
            image = flipImageHorizontal(image);
            ImageFormat imageFormat = Imaging.guessFormat(tiffFile);

            // 处理 TIFF 图像的色彩信息
            // 在这里可以处理色彩信息，如转换色彩模式、应用滤镜等

            // 处理 TIFF 图像的压缩
            // 可以对 TIFF 图像进行压缩和解压缩操作
//            byte[] imageData = Imaging.w(image, null);
            File compressedTiffFile = new File("output_flipImage.tif");

//            BufferedImage bufferedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
//            bufferedImage.copyData(image.getRaster());

            Imaging.writeImage(image, compressedTiffFile, imageFormat);

            System.out.println("TIFF 图像处理完成！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 水平翻转图像的方法
    private static BufferedImage flipImageHorizontal(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage flippedImage = new BufferedImage(width, height, image.getType());

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                flippedImage.setRGB(width - x - 1, y, image.getRGB(x, y));
            }
        }

        return flippedImage;
    }

    // 压缩
    @Test
    @Deprecated
    public void test_4() {
        // 读取 TIFF 图像文件
        File tiffFile = new File(image_path);
        try {
            // 获取 TIFF 图像文件的元数据
            TiffImageMetadata metadata = (TiffImageMetadata) Imaging.getMetadata(tiffFile);
            BufferedImage bufferedImage = Imaging.getBufferedImage(tiffFile);
            ImageFormat imageFormat = Imaging.guessFormat(tiffFile);
            if (metadata != null) {
                // 获取压缩方式标记
                int compression = metadata.findField(TIFF_TAG_COMPRESSION).getIntValue();
                System.out.println("Compression Type: " + compression);

                // 解压缩 TIFF 图像文件
                if (compression != COMPRESSION_VALUE_UNCOMPRESSED) {
                    TiffOutputSet outputSet = new TiffOutputSet(metadata.getOutputSet().byteOrder);

                    // 移除压缩方式标记
                    outputSet.removeField(TIFF_TAG_COMPRESSION);

                    // 将解压缩后的图像写入新的 TIFF 文件
                    File outputFile = new File("output.tif");

                    // 将解压缩后的图像写入新的 TIFF 文件
//                    outputSet.addRootDirectory();

                    Imaging.writeImage(bufferedImage, outputFile, imageFormat);
//                    Imaging.writeImage(outputSet, outputFile, null);

                    System.out.println("TIFF 图像解压缩完成！");
                } else {
                    System.out.println("TIFF 图像无需解压缩！");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
