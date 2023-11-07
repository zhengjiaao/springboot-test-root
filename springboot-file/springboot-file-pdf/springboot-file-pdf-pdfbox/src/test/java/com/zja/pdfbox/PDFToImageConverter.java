/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-07 16:50
 * @Since:
 */
package com.zja.pdfbox;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * pdf转图片
 *
 * @author: zhengja
 * @since: 2023/11/07 16:50
 */
public class PDFToImageConverter {
    public static void main(String[] args) {
        String pdfFilePath = "D:\\temp\\pdf\\test.pdf";
        String outputDir = "D:\\temp\\pdf\\output2";

        try (PDDocument document = Loader.loadPDF(new RandomAccessReadBufferedFile(pdfFilePath))) {
            /*for (PDPage page : document.getPages()) {
            }*/

            PDFRenderer pdfRenderer = new PDFRenderer(document);

            for (int pageIndex = 0; pageIndex < document.getNumberOfPages(); pageIndex++) {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(pageIndex, 300); // 设置dpi（像素密度）

                String outputFilePath = outputDir + "page_" + (pageIndex + 1) + ".png";
                ImageIO.write(bim, "png", new File(outputFilePath));

                System.out.println("Page " + (pageIndex + 1) + " converted to image.");
            }

            document.close();
            System.out.println("Conversion completed.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //提取pdf中某一页转为图片
    @Test
    public void test_one() {
        String pdfFilePath = "D:\\temp\\pdf\\test.pdf";
        String outputDir = "D:\\temp\\pdf\\output3";

        try (PDDocument document = Loader.loadPDF(new RandomAccessReadBufferedFile(pdfFilePath))) {

            //实例化PDFRenderer类
            PDFRenderer renderer = new PDFRenderer(document);

            //从PDF文档渲染图像
//            BufferedImage image = renderer.renderImage(0);
            BufferedImage image = renderer.renderImageWithDPI(0, 300); // 设置dpi（像素密度）

            //将图像写入文件
            ImageIO.write(image, "JPEG", new File(outputDir + "-myimage.jpg"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
