package com.zja.word.poi;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Apache POI本身并没有提供将Word文档直接转换为PDF、图像等功能
 */
@Deprecated
public class WordToImageTest {

    //todo 未完成
    @Test
    public void test() {
        String docFilePath = "D:\\temp\\word\\test.docx";
        String outputFolderPath = "D:\\temp\\word\\to";

        try (InputStream is = Files.newInputStream(Paths.get(docFilePath));
             XWPFDocument document = new XWPFDocument(is)) {

            // 获取文档中的段落
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                // 创建一个空白的BufferedImage对象
                BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

                // 获取段落内容的运行对象
                for (XWPFRun run : paragraph.getRuns()) {
                    // 将段落内容绘制到BufferedImage
                    Graphics2D graphics = image.createGraphics();
                    Font font = new Font(run.getFontFamily(), Font.PLAIN, run.getFontSize());
                    graphics.setFont(font);
                    FontMetrics fontMetrics = graphics.getFontMetrics();
                    int width = fontMetrics.stringWidth(run.getText(0));
                    int height = fontMetrics.getHeight();
                    graphics.dispose();

                    // 创建一个新的BufferedImage对象，大小由段落内容决定
                    image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                    graphics = image.createGraphics();
                    graphics.setFont(font);
                    graphics.setColor(Color.BLACK);
                    graphics.drawString(run.getText(0), 0, fontMetrics.getAscent());
                    graphics.dispose();

                    // 将BufferedImage保存为图像文件
                    // String outputFilePath = outputFolderPath + "paragraph_" + paragraph.getParagraphNumber() + ".png";
                    String outputFilePath = outputFolderPath + "paragraph_" + paragraph.getNumID() + ".png";
                    ImageIO.write(image, "png", new File(outputFilePath));

                    // System.out.println("Paragraph " + paragraph.getParagraphNumber() + " converted to image: " + outputFilePath);
                    System.out.println("Paragraph " + paragraph.getNumID() + " converted to image: " + outputFilePath);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}