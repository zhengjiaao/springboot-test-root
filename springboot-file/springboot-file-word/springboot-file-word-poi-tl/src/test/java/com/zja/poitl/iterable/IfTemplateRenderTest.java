package com.zja.poitl.iterable;

import com.deepoove.poi.XWPFTemplate;
import com.zja.poitl.util.XWPFTestSupport;
import org.apache.poi.xwpf.usermodel.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @Author: zhengja
 * @Date: 2024-11-18 11:47
 */
@DisplayName("If template test case")
public class IfTemplateRenderTest {

    @SuppressWarnings("serial")
    @Test
    public void testIfFalse() throws Exception {
        Map<String, Object> datas = new HashMap<String, Object>() {
            {
                put("title", "poi-tl");
                put("isShowTitle", true);
                put("showUser", false);
                put("showDate", false);
            }
        };

        XWPFTemplate template = XWPFTemplate.compile(getResourceAsStream("templates/word/iterable/iterable_if1.docx"));
        template.render(datas);

        XWPFDocument document = XWPFTestSupport.readNewDocument(template);
        XWPFParagraph paragraph = document.getParagraphArray(0);
        assertEquals(paragraph.getText(), "Hi, poi-tl");

        XWPFTable table = document.getTableArray(0);
        XWPFTableCell cell = table.getRow(1).getCell(0);
        assertEquals(cell.getText(), "Hi, poi-tl");

        XWPFHeader header = document.getHeaderArray(0);
        paragraph = header.getParagraphArray(0);
        assertEquals(paragraph.getText(), "Hi, poi-tl");
    }

    @SuppressWarnings("serial")
    @Test
    public void testIfTrue() throws Exception {
        Map<String, Object> datas = new HashMap<String, Object>() {
            {
                put("title", "poi-tl");
                put("isShowTitle", true);
                put("showUser", new HashMap<String, Object>() {
                    {
                        put("user", "Sayi");
                        put("showDate", new HashMap<String, Object>() {
                            {
                                put("date", "2020-02-10");
                            }
                        });
                    }
                });
            }
        };

        XWPFTemplate template = XWPFTemplate.compile(getResourceAsStream("templates/word/iterable/iterable_if1.docx"));
        template.render(datas);

        XWPFDocument document = XWPFTestSupport.readNewDocument(template);
        XWPFTable table = document.getTableArray(0);
        XWPFTableCell cell = table.getRow(1).getCell(0);
        XWPFHeader header = document.getHeaderArray(0);

        testParagraph(document);
        testParagraph(cell);
        testParagraph(header);
    }

    private void testParagraph(IBody document) {
        XWPFParagraph paragraph = document.getParagraphArray(0);
        assertEquals(paragraph.getText(), "Hi, poi-tl");

        paragraph = document.getParagraphArray(1);
        assertEquals(paragraph.getText(), "Hello, My perfect.");

        paragraph = document.getParagraphArray(2);
        assertEquals(paragraph.getText(), "UserName: Sayi");

        paragraph = document.getParagraphArray(3);
        assertEquals(paragraph.getText(), "Date: 2020-02-10");

        paragraph = document.getParagraphArray(4);
        assertEquals(paragraph.getText(), "Date: 2020-02-10");

        paragraph = document.getParagraphArray(5);
        assertEquals(paragraph.getText(), "I love this Game Date: 2020-02-10 and good game.");
    }

    @SuppressWarnings("serial")
    @Test
    public void testBasicIf() throws Exception {
        Map<String, Object> datas = new HashMap<String, Object>() {
            {
                put("title", "poi-tl");
                put("show", true);
            }
        };

        XWPFTemplate template = XWPFTemplate.compile(getResourceAsStream("templates/word/iterable/iterable_if2.docx"));
        template.render(datas);
        template.writeToFile("target/out_iterable_if_basic.docx");
    }

    private static InputStream getResourceAsStream(String fileName) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
    }
}