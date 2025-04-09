package com.zja.word.poi;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * poi 用于操作Microsoft Office格式文件（如Word、Excel和PowerPoint）的Java库。它提供了一组API，使开发者可以读取、修改和创建这些文件。
 * 注：Apache POI本身并没有提供将Word文档直接转换为PDF、图像等功能,需要借助其他库来完成转换操作。
 *
 * @author: zhengja
 * @since: 2024/01/19 11:05
 */
public class WordPOITest {

    // @TempDir // 使用 @TempDir 注解来创建临时目录，确保每次测试运行时都有一个干净的环境。临时文件或者文件夹在使用完之后就会自动删除。
    // public Path tempDir;
    public Path tempDir = Paths.get("target");

    // poi 创建 word
    @Test
    public void test1_create() {
        File tempFile = tempDir.resolve("test1.docx").toFile();

        // 创建一个新的文档对象
        try (XWPFDocument document = new XWPFDocument()) {

            // 创建一个段落对象
            XWPFParagraph paragraph = document.createParagraph();

            // 创建一个文本运行对象
            XWPFRun run = paragraph.createRun();
            run.setText("Hello, World!");

            // 保存文档到文件
            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                document.write(out);
                System.out.println("新建Word文档成功！");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 读
    @Test
    public void test1_getText() {
        File tempFile = tempDir.resolve("test1.docx").toFile();
        try (FileInputStream fis = new FileInputStream(tempFile);
             XWPFDocument document = new XWPFDocument(fis)) {

            for (XWPFParagraph paragraph : document.getParagraphs()) {
                List<XWPFRun> runs = paragraph.getRuns();
                for (XWPFRun run : runs) {
                    System.out.println("Text: " + run.getText(0));
                    System.out.println("Font Family: " + run.getFontFamily());
                    System.out.println("Font Size: " + run.getFontSize());
                    System.out.println("Bold: " + run.isBold());
                    System.out.println("Color: " + run.getColor());
                    System.out.println("-----------------------------");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2_getText() {
        File tempFile = tempDir.resolve("test1.docx").toFile();
        String extractedText = extractTextFromWord(tempFile.getAbsolutePath());
        System.out.println("Extracted Text: " + extractedText);
    }

    public String extractTextFromWord(String filePath) {
        StringBuilder content = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(filePath);
             XWPFDocument document = new XWPFDocument(fis)) {
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            for (XWPFParagraph para : paragraphs) {
                content.append(para.getText()).append("\n"); // 或者其他分隔符，例如空格或<br>标签等HTML格式。
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error reading Word document";
        }
        return content.toString();
    }

    // poi 编辑 word
    @Test
    public void test2() {
        // 打开现有的Word文档（"new.docx"），获取第一个段落并在其后添加了一段文本。
        try (FileInputStream fis = new FileInputStream("new.docx"); XWPFDocument document = new XWPFDocument(fis)) {
            // 获取第一个段落
            XWPFParagraph paragraph = document.getParagraphs().get(0);

            // 创建一个文本运行对象
            XWPFRun run = paragraph.createRun();
            run.setText(" - Edited");

            // 保存文档到文件
            try (FileOutputStream out = new FileOutputStream("edited.docx")) {
                document.write(out);
                System.out.println("编辑Word文档成功！");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // poi 读取 word
    @Test
    public void test3() {
        // 并遍历文档中的每个段落和文本，并将其打印到控制台。
        try (FileInputStream fis = new FileInputStream("edited.docx"); XWPFDocument document = new XWPFDocument(fis)) {
            // 遍历文档中的段落和文本
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                for (XWPFRun run : paragraph.getRuns()) {
                    System.out.println(run.getText(0));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
