/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-08 10:39
 * @Since:
 */
package com.zja.util;

import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;
import java.io.IOException;

/**
 * @author: zhengja
 * @since: 2023/11/08 10:39
 */
@Slf4j
public class OCRTesseractUtil {

    private OCRTesseractUtil() {

    }

    public static String ocrImage(String inputImagePath) {
        //结果内容
        String result = null;

        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("E:\\App\\tesseract-ocr\\tessdata");
        // 设置识别语言："eng"表示英语，"chi_sim"表示简体中文
        tesseract.setLanguage("chi_sim+eng");

        File imageFile = new File(inputImagePath);

        try {
            result = tesseract.doOCR(imageFile);
        } catch (TesseractException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

        return result;
    }


    /**
     * 将单张图像转换为 PDF
     *
     * @param inputImagePath      /input.png
     * @param outputPdfPrefixPath /output-prefix
     * @throws IOException
     * @throws InterruptedException
     */
    public static void ocrImageToPdf(String inputImagePath, String outputPdfPrefixPath) throws IOException, InterruptedException {
        String command = "tesseract " + inputImagePath + " " + outputPdfPrefixPath + " pdf";
        CommandUtil.command(command);
    }

    /**
     * 将图像转换为 PDF
     *
     * @param inputImagesTxtPath  /input-multiple_images.txt
     * @param outputPdfPrefixPath /output-prefix-multiple_images
     * @throws IOException
     * @throws InterruptedException
     */
    public static void ocrImagesToPdf(String inputImagesTxtPath, String outputPdfPrefixPath) throws IOException, InterruptedException {
        String command = "tesseract " + inputImagesTxtPath + " " + outputPdfPrefixPath + " pdf";
        CommandUtil.command(command);
    }

}
