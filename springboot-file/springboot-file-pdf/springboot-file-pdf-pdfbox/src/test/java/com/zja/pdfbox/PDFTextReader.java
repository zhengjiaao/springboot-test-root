/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-07 17:16
 * @Since:
 */
package com.zja.pdfbox;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * pdfbox 读取pdf所有文字
 *
 * @author: zhengja
 * @since: 2023/11/07 17:16
 */
public class PDFTextReader {
    public static void main(String[] args) {
        String pdfFilePath = "D:\\temp\\pdf\\test.pdf";

        try (PDDocument document = Loader.loadPDF(new RandomAccessReadBufferedFile(pdfFilePath))) {

            PDFTextStripper textStripper = new PDFTextStripper();
            String text = textStripper.getText(document);

            System.out.println("PDF Text:");
            System.out.println(text);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // 提取内容
        String extractedTextFromPDF = extractTextFromPDF(pdfFilePath);
        System.out.println("Extracted Text: " + extractedTextFromPDF);
    }

    public static String extractTextFromPDF(String filePath) {
        try (PDDocument document = Loader.loadPDF(new File(filePath))) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        } catch (IOException e) {
            e.printStackTrace();
            return "Error reading PDF";
        }
    }
}
