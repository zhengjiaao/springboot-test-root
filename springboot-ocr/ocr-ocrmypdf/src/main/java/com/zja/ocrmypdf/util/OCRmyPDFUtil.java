/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-08 10:38
 * @Since:
 */
package com.zja.ocrmypdf.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author: zhengja
 * @since: 2023/11/08 10:38
 */
@Slf4j
public class OCRmyPDFUtil {

    private OCRmyPDFUtil() {

    }

    /**
     * 将图像转 PDF/A
     *
     * @param inputImagePath /input.png
     * @param outputPdfPath  /output-image.pdf
     * @throws IOException
     * @throws InterruptedException
     */
    public static void ocrImageToPDFA(String inputImagePath, String outputPdfPath) throws IOException, InterruptedException {
        ocrImageToPDFA(inputImagePath, outputPdfPath, 300);
    }

    /**
     * 将图像转 PDF/A
     *
     * @param inputImagePath /input.png
     * @param outputPdfPath  /output-image.pdf
     * @param imageDpi       图像的 DPI
     * @throws IOException
     * @throws InterruptedException
     */
    public static void ocrImageToPDFA(String inputImagePath, String outputPdfPath, int imageDpi) throws IOException, InterruptedException {
        // OCRmyPDF命令
        // 注意：--image-dpi，某些图片不一定有image-dpi，需要设置一个默认的，不然报错。
        String command = "ocrmypdf -l chi_sim+eng --image-dpi " + imageDpi + " " + inputImagePath + " " + outputPdfPath;
        //todo 请注意，DPI值的可用性取决于图片格式以及图片本身是否包含DPI信息。某些图片格式可能没有DPI信息可供提取，因此在使用这段代码时应该注意处理这种情况。

        CommandUtil.command(command);

        // todo 可能会遇到的问题
        // 错误：UnsupportedImageFormatError: The input image has an alpha channel. Remove the alpha channel first.
        // 原因：tesseract 目前不支持带有 alpha 通道（透明度通道）的图片。
        // 方案：可以将图片转换为不带 alpha 通道的格式。
    }


    /**
     * 将 PDF 转为 PDF/A
     *
     * @param inputPdfPath  /input.pdf
     * @param outputPdfPath /output.pdf
     * @throws IOException
     * @throws InterruptedException
     */
    public static void ocrPdfToPDFA(String inputPdfPath, String outputPdfPath) throws IOException, InterruptedException {
        // OCRmyPDF命令
        String command = "ocrmypdf --output-type pdfa --pdfa-image-compression jpeg --redo-ocr -l chi_sim+eng " + inputPdfPath + " " + outputPdfPath;
        CommandUtil.command(command);
    }
}
