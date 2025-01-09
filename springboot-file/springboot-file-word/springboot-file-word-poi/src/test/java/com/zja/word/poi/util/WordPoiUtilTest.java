package com.zja.word.poi.util;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFAbstractNum;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @Author: zhengja
 * @Date: 2024-10-28 17:00
 */
public class WordPoiUtilTest {

    // @TempDir // 使用 @TempDir 注解来创建临时目录，确保每次测试运行时都有一个干净的环境。临时文件或者文件夹在使用完之后就会自动删除。
    // public Path tempDir;
    public Path tempDir = Paths.get("target");

    @Test
    public void test() {
        try {
            XWPFDocument document = new XWPFDocument();

            // 添加段落
            WordPoiUtil.addParagraph(document, "这是一个段落。");

            // 添加图片
            WordPoiUtil.addImage(document, "D:\\temp\\images\\test.png", 100, 100);

            // 添加表格
            String[][] data = {{"列1", "列2"}, {"数据1", "数据2"}};
            WordPoiUtil.addTable(document, 2, 2, data);

            // 设置字体和样式
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            WordPoiUtil.setFontAndStyle(run, "这是一个带有样式的段落。", "宋体", 14, true, "FF0000");

            // 添加列表
            // 创建一个抽象编号对象
            // XWPFAbstractNum abstractNum = document.createNumbering().getAbstractNum(BigInteger.ZERO);
            // // 定义列表项
            // String[] items = {"列表项1", "列表项2", "列表项3"};
            // // 调用 addList 方法
            // WordPoiUtil.addList(document, items, abstractNum);

            // 添加页眉和页脚
            WordPoiUtil.addHeaderFooter(document, "这是页眉", "这是页脚");

            // 添加超链接
            WordPoiUtil.addHyperlink(document, "https://www.example.com", "点击这里访问示例网站");

            // 设置页面边距
            WordPoiUtil.setPageMargins(document, 1440, 1440, 1440, 1440);

            // 添加分页符
            WordPoiUtil.addPageBreak(document);

            // 替换文档中的文本
            WordPoiUtil.replaceText(document, "旧文本", "新文本");

            // 保存文档
            File tempFile = tempDir.resolve("test_create2.docx").toFile();
            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                document.write(out);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test_2() throws IOException {
        try (XWPFDocument xwpfDocument = WordPoiUtil.readDocument(new File("D:\\temp\\word\\绿心准入合规性检测报告 - 2024-12-30T155549.729.docx"))) {
            // WordPoiUtil.replaceText(xwpfDocument, "{{word_0}}", "新文本");
            // WordPoiUtil.replaceTextWithStyle(xwpfDocument, "{{word_0}}", "新文本",null);
            // WordPoiUtil.replaceTextAndSetStyle(xwpfDocument, "{{word_0}}", "新文本",  false, "FF0000");
            // WordPoiUtil.replaceTextColor( "{{word_0}}",xwpfDocument,  "FF0000");
            // WordPoiUtil.replaceTextAndSetStyle(xwpfDocument, "{{word_0}}", "新文本", "" ,0,false, "FF0000");
            // WordPoiUtil.replaceText(xwpfDocument, "{{word_0}}", "新文本", "FF0000" ,12, false);
            // WordPoiUtil.replaceTextAndSetColor(xwpfDocument, "{{word_0}}", "新文本", "FF0000");
            // WordPoiUtil.replaceTextAndSetColor(xwpfDocument, "{{word_0}}", "新文本", "FF0000");

            WordPoiUtil.saveDocument(xwpfDocument, new File("D:\\temp\\word\\test_create2.docx"));
        }
    }
}
