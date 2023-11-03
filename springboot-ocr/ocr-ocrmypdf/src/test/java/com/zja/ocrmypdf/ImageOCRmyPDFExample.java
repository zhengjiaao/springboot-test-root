/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-03 14:55
 * @Since:
 */
package com.zja.ocrmypdf;

import com.zja.ocrmypdf.util.OCRmyPDFUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author: zhengja
 * @since: 2023/11/03 14:55
 */
public class ImageOCRmyPDFExample {


    /**
     * OCRmyPDF 仅限单个图像
     */
    @Test
    public void ocr_png_to_pdf() throws IOException, InterruptedException {
        // 输入文件路径
        String inputImagePath = "D:\\temp\\ocr\\input.png";
        // 输出PDF文件路径
        String outputPdfPath = "D:\\temp\\ocr\\image-output.pdf";

        // 执行OCRmyPDF命令  注意：--image-dpi，某些图片不一定有image-dpi，需要设置一个默认的，不然报错。
        String command = "ocrmypdf -l eng+chi_sim --image-dpi 500 " + inputImagePath + " " + outputPdfPath;
        //todo 请注意，DPI值的可用性取决于图片格式以及图片本身是否包含DPI信息。某些图片格式可能没有DPI信息可供提取，因此在使用这段代码时应该注意处理这种情况。

        // 执行OCRmyPDF命令
        OCRmyPDFUtil.ocrCommand(command);
    }

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
        String outputPdfPrefixPath = "D:\\temp\\ocr\\output-prefix";

        String command = "tesseract " + inputImagePath + " " + outputPdfPrefixPath + " pdf";

        OCRmyPDFUtil.ocrCommand(command);
    }

    //Tesseract 将图像转换为 PDF: 当有多个图谱转为pdf时
    @Test
    public void ocr_multiple_images_to_pdf() throws IOException, InterruptedException {
        String inputMultipleImagesPath = "D:\\temp\\ocr\\input-multiple_images.txt";
        String outputPdfPrefixPath = "D:\\temp\\ocr\\output-prefix-multiple_images";

        String command = "tesseract " + inputMultipleImagesPath + " " + outputPdfPrefixPath + " pdf";

        OCRmyPDFUtil.ocrCommand(command);
    }

}
