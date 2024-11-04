package com.zja.cad.aspose;

import com.aspose.cad.Image;
import com.aspose.cad.ImageOptionsBase;
import com.aspose.cad.imageoptions.CadRasterizationOptions;
import com.aspose.cad.imageoptions.PdfOptions;
import com.aspose.cad.imageoptions.PngOptions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

/**
 * @Author: zhengja
 * @Date: 2024-10-31 10:51
 */
public class CadToPdfTest {

    // Path path = Paths.get("D:\\temp\\cad","总平面规划图终核.dxf");
    String path = Paths.get("D:\\temp\\cad", "总平面规划图终核.dwg").toString();


    @Test
    public void test_1()  {
        //        Image image = Image.load("D:\\temp\\cad\\南湖6总平面终.dwg");  // input.dwg、template.dxf
//        Image image = Image.load("D:\\temp\\cad\\9.2城北平面(修改学校指标）炸开.dwg");  // input.dwg、template.dxf
        try (Image image = Image.load(path)) {

            // create an instance of CadRasterizationOptions
            CadRasterizationOptions rasterizationOptions = new CadRasterizationOptions();

            // set page width & height
            rasterizationOptions.setPageWidth(1200);
            rasterizationOptions.setPageHeight(1200);

            // 设置背景颜色(可选地)
//        rasterizationOptions.setBackgroundColor(com.aspose.cad.Color.getBeige());
//        rasterizationOptions.setDrawType(CadDrawTypeMode.UseDrawColor);
//        rasterizationOptions.setBackgroundColor(com.aspose.cad.Color.getBlue());

            // Set Auto Layout Scaling(可选地)
            rasterizationOptions.setAutomaticLayoutsScaling(true); // 允许自动布局缩放
            rasterizationOptions.setNoScaling(true); // 禁止自动布局缩放

            // create an instance of PngOptions for the resultant image
            ImageOptionsBase options = new PdfOptions();

            // set rasterization options
            options.setVectorRasterizationOptions(rasterizationOptions);

            // save resultant image
            image.save("target/to_pdf_1.pdf", options);
        }  // input.dwg、template.dxf
    }

    @Test
    public void test_2() {
        try (Image image = Image.load(path)) {

            // create an instance of CadRasterizationOptions
            CadRasterizationOptions rasterizationOptions = new CadRasterizationOptions();

            // set page width & height
            rasterizationOptions.setPageWidth(1200);
            rasterizationOptions.setPageHeight(1200);

            // Set Auto Layout Scaling(可选地)
            rasterizationOptions.setAutomaticLayoutsScaling(true); // 允许自动布局缩放
            rasterizationOptions.setNoScaling(true); // 禁止自动布局缩放

            // create an instance of PngOptions for the resultant image
            ImageOptionsBase options = new PngOptions();

            // set rasterization options
            options.setVectorRasterizationOptions(rasterizationOptions);

            // save resultant image
            image.save("target/to_png_2.png", options);
        }  // input.dwg、template.dxf
    }

}
