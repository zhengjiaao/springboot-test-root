package com.zja.word.poi;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * @Author: zhengja
 * @Date: 2024-10-28 16:46
 */
public class WordCreateTest {

    // @TempDir // 使用 @TempDir 注解来创建临时目录，确保每次测试运行时都有一个干净的环境。临时文件或者文件夹在使用完之后就会自动删除。
    // public Path tempDir;
    public Path tempDir = Paths.get("target");

    @Test
    public void testCreateWordDocument() {
        File tempFile = tempDir.resolve("test_create.docx").toFile();
        assertDoesNotThrow(() -> {
            XWPFDocument document = new XWPFDocument();
            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                document.write(out);
            }
        });
    }

    @Test
    public void testCreateWord() {
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
}
