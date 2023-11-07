/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-07 17:27
 * @Since:
 */
package com.zja.pdfbox;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;

/**
 * pdfbox 将一个PDF文档拆成多个pdf
 *
 * @author: zhengja
 * @since: 2023/11/07 17:27
 */
public class PDFSplitter {
    public static void main(String[] args) {
        String pdfFilePath = "D:\\temp\\pdf\\test.pdf";
        String outputDir = "D:\\temp\\pdf\\output4";

        try (PDDocument document = Loader.loadPDF(new RandomAccessReadBufferedFile(pdfFilePath))) {

            int pageCount = document.getNumberOfPages();
            for (int pageIndex = 0; pageIndex < pageCount; pageIndex++) {
                PDDocument singlePageDocument = new PDDocument();
                singlePageDocument.addPage(document.getPage(pageIndex));

                String outputFilePath = outputDir + "page_" + (pageIndex + 1) + ".pdf";
                singlePageDocument.save(new File(outputFilePath));

                singlePageDocument.close();

                System.out.println("Page " + (pageIndex + 1) + " extracted: " + outputFilePath);
            }
            System.out.println("PDF splitting completed.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
