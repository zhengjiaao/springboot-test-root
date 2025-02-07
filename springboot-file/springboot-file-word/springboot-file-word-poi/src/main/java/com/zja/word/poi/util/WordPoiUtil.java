package com.zja.word.poi.util;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.StringUtil;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: zhengja
 * @Date: 2024-10-28 16:49
 */
public class WordPoiUtil {

    private WordPoiUtil() {

    }

    /**
     * 读取文档
     *
     * @param inputFile 输入文件
     */
    public static XWPFDocument readDocument(File inputFile) throws IOException {
        return new XWPFDocument(Files.newInputStream(inputFile.toPath()));
    }

    /**
     * 保存文档
     *
     * @param document   文档对象
     * @param outputFile 输出文件
     */
    public static void saveDocument(XWPFDocument document, File outputFile) throws IOException {
        FileOutputStream out = new FileOutputStream(outputFile);
        document.write(out);
        out.close();
    }

    /**
     * 创建一个空的word文件
     */
    public static void createEmptyWord(File file) throws IOException {
        try (XWPFDocument document = new XWPFDocument()) {
            try (FileOutputStream out = new FileOutputStream(file)) {
                document.write(out);
            }
        }
    }

    /**
     * 创建一个word文件
     */
    public static void createWord(File file, XWPFDocument document) throws IOException {
        try (FileOutputStream out = new FileOutputStream(file)) {
            document.write(out);
        }
    }

    /**
     * 向文档中添加段落。
     *
     * @param document 文档对象
     * @param text     段落内容
     */
    public static void addParagraph(XWPFDocument document, String text) {
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.setText(text);
    }

    /**
     * 向文档中添加图片。
     *
     * @param document  文档对象
     * @param imagePath 图片路径
     * @param width     图片宽度
     * @param height    图片高度
     * @throws IOException 如果文件操作失败
     */
    public static void addImage(XWPFDocument document, String imagePath, int width, int height) throws IOException, InvalidFormatException {
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        try (FileInputStream fis = new FileInputStream(imagePath)) {
            run.addPicture(fis, XWPFDocument.PICTURE_TYPE_JPEG, imagePath, Units.toEMU(width), Units.toEMU(height));
        }
    }

    /**
     * 向文档中添加表格。
     *
     * @param document 文档对象
     * @param rows     行数
     * @param cols     列数
     * @param data     表格数据
     */
    public static void addTable(XWPFDocument document, int rows, int cols, String[][] data) {
        XWPFTable table = document.createTable(rows, cols);
        for (int i = 0; i < rows; i++) {
            XWPFTableRow row = table.getRow(i);
            for (int j = 0; j < cols; j++) {
                row.getCell(j).setText(data[i][j]);
            }
        }
    }

    /**
     * 设置段落的字体和样式。
     *
     * @param run        段落运行对象
     * @param text       段落内容
     * @param fontFamily 字体名称
     * @param fontSize   字体大小
     * @param bold       是否加粗
     * @param color      字体颜色（十六进制）
     */
    public static void setFontAndStyle(XWPFRun run, String text, String fontFamily, int fontSize, boolean bold, String color) {
        run.setText(text);
        run.setFontFamily(fontFamily);
        run.setFontSize(fontSize);
        run.setBold(bold);
        run.setColor(color);
    }

    /**
     * 向文档中添加列表。
     *
     * @param document 文档对象
     * @param items    列表项
     * @throws IOException 如果文件操作失败
     */
    public static void addList(XWPFDocument document, String[] items, XWPFAbstractNum abstractNum) {
        XWPFNumbering numbering = document.createNumbering();
        BigInteger abstractNumId = numbering.addAbstractNum(abstractNum);
        BigInteger numId = numbering.addNum(abstractNumId);

        for (String item : items) {
            XWPFParagraph paragraph = document.createParagraph();
            paragraph.setNumID(numId);
            XWPFRun run = paragraph.createRun();
            run.setText(item);
        }
    }

    /**
     * 向文档中添加页眉和页脚。
     *
     * @param document   文档对象
     * @param headerText 页眉内容
     * @param footerText 页脚内容
     */
    public static void addHeaderFooter(XWPFDocument document, String headerText, String footerText) {
        XWPFHeaderFooterPolicy policy = document.getHeaderFooterPolicy();
        if (policy == null) {
            policy = document.createHeaderFooterPolicy();
        }

        XWPFHeader header = policy.createHeader(XWPFHeaderFooterPolicy.DEFAULT);
        XWPFParagraph headerParagraph = header.createParagraph();
        XWPFRun headerRun = headerParagraph.createRun();
        headerRun.setText(headerText);

        XWPFParagraph footerParagraph = policy.createFooter(XWPFHeaderFooterPolicy.DEFAULT).createParagraph();
        XWPFRun footerRun = footerParagraph.createRun();
        footerRun.setText(footerText);
    }

    /**
     * 向文档中添加超链接。
     *
     * @param document 文档对象
     * @param url      超链接URL
     * @param text     超链接文本
     */
    public static void addHyperlink(XWPFDocument document, String url, String text) {
        XWPFParagraph paragraph = document.createParagraph();
        XWPFHyperlinkRun hyperlinkRun = paragraph.createHyperlinkRun(url);
        hyperlinkRun.setText(text);
    }

    /**
     * 设置页面边距。
     *
     * @param document 文档对象
     * @param left     左边距（单位：twips）
     * @param right    右边距（单位：twips）
     * @param top      上边距（单位：twips）
     * @param bottom   下边距（单位：twips）
     */
    public static void setPageMargins(XWPFDocument document, int left, int right, int top, int bottom) {
        CTPageMar pageMar = document.getDocument().getBody().addNewSectPr().addNewPgMar();
        pageMar.setLeft(left);
        pageMar.setRight(right);
        pageMar.setTop(top);
        pageMar.setBottom(bottom);
    }

    /**
     * 向文档中添加分页符。
     *
     * @param document 文档对象
     */
    public static void addPageBreak(XWPFDocument document) {
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.addBreak(BreakType.PAGE);
    }

    /**
     * 替换文档中的文本。
     *
     * @param document 文档对象
     * @param oldText  要替换的文本
     * @param newText  新文本
     */
    public static void replaceText(XWPFDocument document, String oldText, String newText) {
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            String text = paragraph.getText();
            if (text.contains(oldText)) {
                paragraph.getRuns().forEach(run -> {
                    String runText = run.getText(0);
                    if (runText != null && runText.contains(oldText)) {
                        run.setText(runText.replace(oldText, newText), 0);
                    }
                });
            }
        }
    }

    /**
     * 设置整个表格的单元格内容垂直居中
     */
    public static void centerTableVertically(XWPFTable table) {
        for (XWPFTableRow row : table.getRows()) {
            for (XWPFTableCell cell : row.getTableCells()) {
                for (XWPFParagraph paragraph : cell.getParagraphs()) {
                    paragraph.setVerticalAlignment(TextAlignment.CENTER); // 垂直居中
                }
                cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER); // 垂直
            }
        }
    }

    /**
     * 设置指定列的单元格内容垂直居中
     */
    public static void centerColumnVertically(XWPFTable table, int columnIndex) {
        int rowCount = table.getRows().size();
        for (int i = 0; i < rowCount; i++) {
            XWPFTableCell cell = table.getRow(i).getCell(columnIndex);
            if (cell != null) {
                for (XWPFParagraph paragraph : cell.getParagraphs()) {
                    paragraph.setVerticalAlignment(TextAlignment.CENTER); // 垂直居中
                }
                cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER); // 垂直
            }
        }
    }

    /**
     * 设置指定行的单元格内容垂直居中
     */
    public static void centerRowVertically(XWPFTable table, int rowIndex) {
        XWPFTableRow row = table.getRow(rowIndex);
        if (row != null) {
            for (XWPFTableCell cell : row.getTableCells()) {
                for (XWPFParagraph paragraph : cell.getParagraphs()) {
                    paragraph.setVerticalAlignment(TextAlignment.CENTER); // 垂直居中
                }
                cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER); // 垂直
            }
        }
    }

    /**
     * 设置指定单元格的单元格内容垂直居中
     */
    public static void centerCellVertically(XWPFTable table, int rowIndex, int columnIndex) {
        XWPFTableRow row = table.getRow(rowIndex);
        if (row != null) {
            XWPFTableCell cell = row.getCell(columnIndex);
            if (cell != null) {
                for (XWPFParagraph paragraph : cell.getParagraphs()) {
                    paragraph.setVerticalAlignment(TextAlignment.CENTER); // 垂直居中
                }
                cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER); // 垂直
            }
        }
    }

    // 设置指定单元格的新值
    private void setCellNewValue(XWPFTable table, int rowIndex, int columnIndex, String newValue) {
        XWPFTableRow row = table.getRow(rowIndex);
        if (row != null) {
            XWPFTableCell cell = row.getCell(columnIndex);
            if (cell == null) {
                cell = row.createCell();
            }
            // 清空单元格文本内容
            clearCellText(cell);
            cell.setText(newValue);
        }
    }

    // 清空单元格文本内容
    private void clearCellText(XWPFTableCell cell) {
        // 清空所有段落的文本内容
        for (XWPFParagraph paragraph : cell.getParagraphs()) {
            // 删除段落中的所有运行（runs）
            for (XWPFRun run : paragraph.getRuns()) {
                run.setText("", 0);
            }
        }
    }

}
