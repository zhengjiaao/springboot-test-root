/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-07 17:01
 * @Since:
 */
package com.zja.pdfbox;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.File;
import java.io.IOException;

/**
 * 文本转pdf
 *
 * @author: zhengja
 * @since: 2023/11/07 17:01
 */
public class TextToPdfConverter {
    public static void main(String[] args) {
        String text = "This is a sample text to be converted to PDF.";
        String outputFilePath = "D:\\temp\\pdf\\output.pdf";

        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

//            PDType1Font font = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD); //注，该字体不支持中文
            PDType1Font font = new PDType1Font(Standard14Fonts.FontName.COURIER); //注，该字体不支持中文

            int fontSize = 12;
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(font, fontSize);

            contentStream.beginText();
            contentStream.newLineAtOffset(50, 700); // 设置文本起始位置
            contentStream.showText(text);
            contentStream.endText();

            contentStream.close();

            document.save(new File(outputFilePath));
            document.close();

            System.out.println("Text converted to PDF successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
