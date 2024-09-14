package com.zja.aspose;

import com.aspose.cad.Image;
import com.aspose.cad.ImageOptionsBase;
import com.aspose.cad.imageoptions.CadRasterizationOptions;
import com.aspose.cad.imageoptions.PdfOptions;
import com.aspose.cad.imageoptions.PngOptions;
import org.junit.jupiter.api.Test;
import com.aspose.cad.License;

import java.io.InputStream;

/**
 * 官网参考：https://releases.aspose.com/cad/java/
 *
 * @Author: zhengja
 * @Date: 2024-09-13 14:53
 */
public class CADTest {

    //未授权认证情况：会有水印和数量(页数)限制
    @Test
    public void cadToPdf_test() {
//        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("license.xml");
//        License license = new License();
//        license.setLicense(is);

//        Image image = Image.load("D:\\temp\\cad\\南湖6总平面终.dwg");  // input.dwg、template.dxf
//        Image image = Image.load("D:\\temp\\cad\\9.2城北平面(修改学校指标）炸开.dwg");  // input.dwg、template.dxf
        Image image = Image.load("D:\\temp\\cad\\总平面规划图终核.dwg");  // input.dwg、template.dxf

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
        image.save("target/output9.pdf", options);
    }

    //未授权认证情况：会有水印和数量(页数)限制
    @Test
    public void cadToPng_test() throws Exception {
//        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("license.xml");
//        License license = new License();
//        license.setLicense(is);

        Image image = Image.load("D:\\temp\\cad\\南湖6总平面终.dwg");  // input.dwg、template.dxf

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
        image.save("target/output2.png", options);
    }
}
