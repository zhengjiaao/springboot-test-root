package com.zja.cad.aspose;

import com.aspose.cad.Image;
import com.aspose.cad.ImageOptionsBase;
import com.aspose.cad.fileformats.cad.CadImage;
import com.aspose.cad.imageoptions.CadRasterizationOptions;
import com.aspose.cad.imageoptions.JpegOptions;
import com.aspose.cad.imageoptions.PngOptions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @Author: zhengja
 * @Date: 2024-10-31 10:05
 */
public class CadToImageTest {

    // Path path = Paths.get("D:\\temp\\cad","总平面规划图终核.dxf");
    String path = Paths.get("D:\\temp\\cad", "总平面规划图终核.dwg").toString();

    @Test
    public void testConvertCadToPng() {
        // Image image = Image.load("template.dxf");
        try (Image image = Image.load(path)) {

            // create an instance of CadRasterizationOptions
            CadRasterizationOptions rasterizationOptions = new CadRasterizationOptions();

            // set page width & height
            rasterizationOptions.setPageWidth(1200);
            rasterizationOptions.setPageHeight(1200);

            // Set Auto Layout Scaling(可选地)
            // rasterizationOptions.setAutomaticLayoutsScaling(true); // 允许自动布局缩放
            // rasterizationOptions.setNoScaling(true); // 禁止自动布局缩放

            // create an instance of PngOptions for the resultant image
            ImageOptionsBase options = new PngOptions();

            // set rasterization options
            options.setVectorRasterizationOptions(rasterizationOptions);

            // save resultant image
            image.save("target/to_image_png.png", options);
        }
    }

    // 转换 CAD 文件为 PNG 格式
    @Test
    public void testConvertCadToPng2() {
        // 读取 CAD 文件
        String outputFilePath;
        try (CadImage cadImage = (CadImage) Image.load(path)) {

            // 设置 PNG 保存选项
            PngOptions saveOptions = new PngOptions();

            // 保存为 PNG 文件
            outputFilePath = "target/to_image_png2.png";
            cadImage.save(outputFilePath, saveOptions);
        }

        // 检查文件是否转换成功
        File file = new File(outputFilePath);
        assert file.exists();
    }

    @Test
    public void testConvertCadToJpeg() {
        // 加载 DWG 文件
        try (Image image = Image.load(path)) {

            // 设置 JPEG 保存选项
            JpegOptions saveOptions = new JpegOptions();
            saveOptions.setQuality(100);

            // 保存为 JPEG 文件
            image.save("target/to_image_jpeg.jpeg", saveOptions);
        }
    }

    // 转换 CAD 文件为 jpeg 格式
    @Test
    public void testConvertCadToJpeg2() {
        // 读取 CAD 文件
        String outputFilePath;
        try (CadImage cadImage = (CadImage) Image.load(path)) {

            // 设置 JPEG 保存选项
            JpegOptions saveOptions = new JpegOptions();
            saveOptions.setQuality(100);

            // 保存为 JPEG 文件
            outputFilePath = "target/to_image_jpeg2.jpeg";
            cadImage.save(outputFilePath, saveOptions);
        }

        // 检查文件是否转换成功
        File file = new File(outputFilePath);
        assert file.exists();
    }
}
