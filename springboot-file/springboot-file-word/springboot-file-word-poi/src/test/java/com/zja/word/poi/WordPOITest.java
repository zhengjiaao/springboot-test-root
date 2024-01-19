package com.zja.word.poi;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * poi 用于操作Microsoft Office格式文件（如Word、Excel和PowerPoint）的Java库。它提供了一组API，使开发者可以读取、修改和创建这些文件。
 * 注：Apache POI本身并没有提供将Word文档直接转换为PDF、图像等功能,需要借助其他库来完成转换操作。
 *
 * @author: zhengja
 * @since: 2024/01/19 11:05
 */
public class WordPOITest {

    // poi 创建 word
    @Test
    public void test1() {
        // 创建一个新的文档对象
        XWPFDocument document = new XWPFDocument();

        // 创建一个段落对象
        XWPFParagraph paragraph = document.createParagraph();

        // 创建一个文本运行对象
        XWPFRun run = paragraph.createRun();
        run.setText("Hello, World!");

        // 保存文档到文件
        try (FileOutputStream out = new FileOutputStream("new.docx")) {
            document.write(out);
            System.out.println("新建Word文档成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // poi 编辑 word
    @Test
    public void test2() {
        // 打开现有的Word文档（"new.docx"），获取第一个段落并在其后添加了一段文本。
        try (FileInputStream fis = new FileInputStream("new.docx");
             XWPFDocument document = new XWPFDocument(fis)) {
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
        try (FileInputStream fis = new FileInputStream("edited.docx");
             XWPFDocument document = new XWPFDocument(fis)) {
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
