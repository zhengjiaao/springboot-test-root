package com.zja.image.imaging;

import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.ImagingException;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.RationalNumber;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffImageParser;
import org.apache.commons.imaging.formats.tiff.TiffImagingParameters;
import org.apache.commons.imaging.formats.tiff.constants.GpsTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.MicrosoftTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.Rfc2301TagConstants;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;
import org.junit.jupiter.api.Test;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Iterator;

import static org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants.*;

/**
 * 图像处理
 *
 * <p>
 * 应用示例：
 * （1）对一个照相机拍摄的图片的元数据读取；
 * （2）对一个手机拍摄的图片的元素据获取；
 * （3）读取一个截图软件生成的图片的元素据获取；
 * （4）将截图生成的图片（无元素据）写入元素据；
 * </p>
 *
 * @Author: zhengja
 * @Date: 2024-09-13 9:44
 */
public class CommonsImagingTest {

    //    static final String image_path = Paths.get("D:\\temp\\images\\test", "1.jpeg").toString();
//    static final String image_path = Paths.get("D:\\temp\\images\\test", "2.png").toString();
//    static final String image_path = Paths.get("D:\\temp\\images\\test", "3.jpg").toString();
    static final String image_path = Paths.get("D:\\temp\\images\\test", "4.tif").toString();
//    static final String image_path = Paths.get("D:\\temp\\images\\test", "5.tif").toString();

    // 读取图片
    @Test
    public void test_1() {

    }

    // 写入图片
    @Test
    public void test_2() {

    }

    // 任意图片写入属性并解析
    @Test
    public void test_3() throws IOException {
        String filePath = this.getClass().getResource("任意截图-写入数据.png").getFile();
        File jpegImageFile = new File(URLDecoder.decode(filePath, StandardCharsets.UTF_8.name()));

        TiffImagingParameters params = new TiffImagingParameters();
        TiffOutputSet outputSet = params.getOutputSet();
        if (outputSet == null) {
            outputSet = new TiffOutputSet();
        }
        TiffOutputDirectory tiffOutputFields = outputSet.addRootDirectory();
        //标准属性
        tiffOutputFields.add(MicrosoftTagConstants.EXIF_TAG_XPTITLE, "标题名字");
        tiffOutputFields.add(MicrosoftTagConstants.EXIF_TAG_XPAUTHOR, "作者名字");
        tiffOutputFields.add(MicrosoftTagConstants.EXIF_TAG_XPCOMMENT, "备注：https://www.chendd.cn");
        tiffOutputFields.add(MicrosoftTagConstants.EXIF_TAG_XPSUBJECT, "主题");
        tiffOutputFields.add(MicrosoftTagConstants.EXIF_TAG_XPKEYWORDS, "陈冬冬、Java");

        //经度
        {
            double value = 111.68;
            final double latitudeDegrees = (long) value;
            value %= 1;
            value *= 60.0;
            final double latitudeMinutes = (long) value;
            value %= 1;
            value *= 60.0;
            final double latitudeSeconds = value;
            tiffOutputFields.add(GpsTagConstants.GPS_TAG_GPS_LONGITUDE,
                    RationalNumber.valueOf(latitudeDegrees), RationalNumber.valueOf(latitudeMinutes), RationalNumber.valueOf(latitudeSeconds));
        }
        //纬度
        {
            double value = 32.36;
            final double latitudeDegrees = (long) value;
            value %= 1;
            value *= 60.0;
            final double latitudeMinutes = (long) value;
            value %= 1;
            value *= 60.0;
            final double latitudeSeconds = value;
            tiffOutputFields.add(GpsTagConstants.GPS_TAG_GPS_LATITUDE,
                    RationalNumber.valueOf(latitudeDegrees), RationalNumber.valueOf(latitudeMinutes), RationalNumber.valueOf(latitudeSeconds));
        }

        //其它数据
        tiffOutputFields.add(TiffTagConstants.TIFF_TAG_DATE_TIME, "2020:30:40 11:22:33");
        tiffOutputFields.add(TiffTagConstants.TIFF_TAG_COPYRIGHT, "Copyright https://www.chendd.cn", "Copyright Apache");
        tiffOutputFields.add(TiffTagConstants.TIFF_TAG_IMAGE_DESCRIPTION, "这是一张来自于陈冬冬个人博客的图片");
        tiffOutputFields.add(TiffTagConstants.TIFF_TAG_ORIENTATION, (short) 35);

        tiffOutputFields.add(Rfc2301TagConstants.TIFF_TAG_DECODE, RationalNumber.valueOf(Math.PI));

        params.setOutputSet(outputSet);

        BufferedImage bufferedImage = Imaging.getBufferedImage(jpegImageFile);
        TiffImageParser parser = new TiffImageParser();
        File outputFile = new File(jpegImageFile.getParentFile(), "任意截图-写入数据-后.png");
        parser.writeImage(bufferedImage, new FileOutputStream(outputFile), params);
        //获取新图片的元数据
        final ImageMetadata metadata = Imaging.getMetadata(outputFile);
        System.out.println(metadata);
        System.out.println("新图片文件路径：" + outputFile.getPath());
    }

    // 编辑图片
    @Test
    public void test_4() {

    }

    // 压缩图片: 使用 ImageIO 来读取 PNG 图像文件并以不同的压缩质量重新保存
    // PNG 是一种无损压缩格式，因此压缩质量设置可能会对图像的文件大小和质量产生一定影响。
    @Test
    public void test_5() {
        try {
            // 读取 PNG 图像文件
            File pngFile = new File("D:\\temp\\images\\test\\2.png");
            BufferedImage image = ImageIO.read(pngFile);

            // 设置压缩质量
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("png");
            ImageWriter writer = writers.next();
            ImageWriteParam writeParam = writer.getDefaultWriteParam();
            writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            writeParam.setCompressionQuality(0.5f); // 设置压缩质量，范围为 0 到 1

            // 保存 PNG 图像文件
            File compressedPngFile = new File("target\\output_compressed.png");
            ImageOutputStream ios = ImageIO.createImageOutputStream(compressedPngFile);
            writer.setOutput(ios);
            writer.write(null, new IIOImage(image, null, null), writeParam);

            System.out.println("PNG 图像压缩完成！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //  无损压缩图片
    @Test
    public void test_6() {

    }

    // 解压图片
    @Test
    public void test_7() {

    }

}
