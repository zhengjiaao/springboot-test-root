/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-07 17:17
 * @Since:
 */
package com.zja.pdfbox;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 从pdf中提取图片
 *
 * @author: zhengja
 * @since: 2023/11/07 17:17
 */
public class PDFImageReader {

    //pdfbox 读取 pdf 所有图片
    @Test
    public static void main(String[] args) {
        String pdfFilePath = "D:\\temp\\pdf\\test.pdf";
        String outputDir = "D:\\temp\\pdf\\output3";

        try (PDDocument document = Loader.loadPDF(new RandomAccessReadBufferedFile(pdfFilePath))) {

            int pageIndex = 0;
            for (PDPage page : document.getPages()) {
                PDResources resources = page.getResources();
                int imageIndex = 1;

                for (COSName xObjectName : resources.getXObjectNames()) {
                    if (resources.isImageXObject(xObjectName)) {
                        PDImageXObject image = (PDImageXObject) resources.getXObject(xObjectName);
                        BufferedImage bImage = image.getImage();

                        String outputFilePath = outputDir + "page_" + (pageIndex + 1) + "_image_" + imageIndex + ".png";
                        ImageIO.write(bImage, "png", new File(outputFilePath));

                        System.out.println("Image extracted: " + outputFilePath);

                        imageIndex++;
                    }
                }

                pageIndex++;
            }

            document.close();
            System.out.println("Image extraction completed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
