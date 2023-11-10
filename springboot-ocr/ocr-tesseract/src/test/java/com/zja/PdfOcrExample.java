/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-10 15:15
 * @Since:
 */
package com.zja;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;
import java.io.IOException;

/**
 * @author: zhengja
 * @since: 2023/11/10 15:15
 */
public class PdfOcrExample {
    public static void main(String[] args) throws IOException {
        Tesseract tesseract = new Tesseract();
        tesseract.setLanguage("chi_sim+eng");
        try {
            String ocrResult = tesseract.doOCR(new File("D:\\temp\\ocr\\文件识别测试\\测试1.pdf"));
            System.out.println("OCR Result: " + ocrResult);
        } catch (TesseractException e) {
            e.printStackTrace();
        }
    }
}
