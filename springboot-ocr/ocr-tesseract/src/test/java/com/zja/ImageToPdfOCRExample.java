/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-03 17:02
 * @Since:
 */
package com.zja;

import com.zja.util.CommandUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author: zhengja
 * @since: 2023/11/03 17:02
 */
public class ImageToPdfOCRExample {

    /**
     * Tesseract 将图像转换为 PDF:单张图像 or 多张图像
     * <p>
     * Tesseract 的 PDF 输出非常好 – OCRmyPDF 在某些情况下在内部使用它。
     */
    @Test
    public void ocr_image_to_pdf() throws IOException, InterruptedException {
        // 输入文件路径
        String inputImagePath = "D:\\temp\\ocr\\input.png";
        // 输出PDF文件路径(不带文件扩展名)
        String outputPdfPrefixPath = "D:\\temp\\ocr\\output-prefix"; //输出文件名称：output-prefix.pdf

        String command = "tesseract " + inputImagePath + " " + outputPdfPrefixPath + " pdf";

        CommandUtil.command(command);
    }

    //Tesseract 将图像转换为 PDF: 多张图像转为pdf
    @Test
    public void ocr_multiple_images_to_pdf() throws IOException, InterruptedException {
        String inputMultipleImagesPath = "D:\\temp\\ocr\\input-multiple_images.txt";
        String outputPdfPrefixPath = "D:\\temp\\ocr\\output-prefix-multiple_images";

        String command = "tesseract " + inputMultipleImagesPath + " " + outputPdfPrefixPath + " pdf";

        CommandUtil.command(command);
    }

}
