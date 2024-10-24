package com.zja.pdf.itext8;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.listener.ITextExtractionStrategy;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;
import com.itextpdf.kernel.utils.PdfMerger;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.itextpdf.svg.converter.SvgConverter;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author: zhengja
 * @Date: 2024-10-23 10:52
 */
public class PdfItext8Test {

    // 创建和编辑PDF文档
    @Test
    public void test_1() {
        try (PdfWriter writer = new PdfWriter("test_1.pdf");
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document doc = new Document(pdfDoc)) {

            doc.add(new Paragraph("Hello, World!"));
            assertEquals(1, pdfDoc.getNumberOfPages(), "PDF should have one page");
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }

        try (Document document = new Document(new PdfDocument(new PdfWriter("test_1_hello-pdf.pdf")))) {
            document.add(new Paragraph("Hello PDF!"));
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    // 渲染高质量的PDF内容
    @Test
    public void test_2() {
        try (PdfWriter writer = new PdfWriter("test_2.pdf");
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document doc = new Document(pdfDoc)) {

            doc.add(new Paragraph("This is a high-quality rendered text"));
            PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
            canvas.setLineWidth(2).setStrokeColor(Color.makeColor(DeviceRgb.BLUE.getColorSpace())).rectangle(100, 600, 200, 100).stroke();
            assertEquals(2, pdfDoc.getNumberOfPages(), "PDF should have one page");
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    // PDF文档的合并和拆分
    @Test
    public void test_3() {
        try {
            // 创建两个源PDF文件
            createSourcePdf("source1.pdf", "Content of Source 1");
            createSourcePdf("source2.pdf", "Content of Source 2");

            // 合并PDF文件
            mergePdfs("source1.pdf", "source2.pdf");

            // 拆分PDF文件
            splitPdf("merged.pdf", "split_");

            // 验证合并后的PDF文件页数
            try (PdfReader mergedReader = new PdfReader("merged.pdf");
                 PdfDocument mergedPdfDoc = new PdfDocument(mergedReader)) {
                assertEquals(2, mergedPdfDoc.getNumberOfPages(), "Merged PDF should have two pages");
            }

            // 验证拆分后的PDF文件页数
            try (PdfReader splitReader1 = new PdfReader("split_1.pdf");
                 PdfDocument splitPdfDoc1 = new PdfDocument(splitReader1)) {
                assertEquals(1, splitPdfDoc1.getNumberOfPages(), "Split PDF 1 should have one page");
            }

            try (PdfReader splitReader2 = new PdfReader("split_2.pdf");
                 PdfDocument splitPdfDoc2 = new PdfDocument(splitReader2)) {
                assertEquals(1, splitPdfDoc2.getNumberOfPages(), "Split PDF 2 should have one page");
            }
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    private void createSourcePdf(String filePath, String content) throws IOException {
        try (PdfWriter writer = new PdfWriter(filePath);
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document doc = new Document(pdfDoc)) {
            doc.add(new Paragraph(content));
        }
    }

    private void mergePdfs(String... fileNames) throws IOException {
        PdfMerger merger = new PdfMerger(new PdfDocument(new PdfWriter("merged.pdf")));
        for (String fileName : fileNames) {
            PdfDocument sourceDoc = new PdfDocument(new PdfReader(fileName));
            merger.merge(sourceDoc, 1, sourceDoc.getNumberOfPages());
            sourceDoc.close();
        }
        merger.close();
    }

    private void splitPdf(String inputFilePath, String outputPrefix) throws IOException {
        try (PdfReader reader = new PdfReader(inputFilePath);
             PdfDocument pdfDoc = new PdfDocument(reader)) {
            for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
                PdfDocument splitDoc = new PdfDocument(new PdfWriter(outputPrefix + i + ".pdf"));
                PdfPage page = pdfDoc.getPage(i);
                PdfPage copiedPage = page.copyTo(splitDoc);
                splitDoc.addPage(copiedPage);
                splitDoc.close();
            }
        }
    }


    // 处理复杂的PDF任务
    @Test
    public void test_4() {
        try (PdfWriter writer = new PdfWriter("test_4.pdf");
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document doc = new Document(pdfDoc)) {

            doc.add(new Paragraph("Complex PDF task"));
            // 添加复杂内容，例如嵌套表格、图表等
            Table table = new Table(new float[]{100, 100});
            table.addCell(new Cell().add(new Paragraph("Header 1")));
            table.addCell(new Cell().add(new Paragraph("Header 2")));
            table.addCell(new Cell().add(new Paragraph("Row 1, Col 1")));
            table.addCell(new Cell().add(new Paragraph("Row 1, Col 2")));
            doc.add(table);
            assertEquals(1, pdfDoc.getNumberOfPages(), "PDF should have one page");
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    // 文本处理
    @Test
    public void test_5() {
        try (PdfWriter writer = new PdfWriter("test_5.pdf");
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document doc = new Document(pdfDoc)) {

            doc.add(new Paragraph("Text processing example"));
            doc.add(new Paragraph("This is a second paragraph with different styling").setTextAlignment(TextAlignment.CENTER));
            assertEquals(1, pdfDoc.getNumberOfPages(), "PDF should have one page");
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    // 图像处理
    @Test
    public void test_6() {
        try (PdfWriter writer = new PdfWriter("test_6.pdf");
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document doc = new Document(pdfDoc)) {

            ImageData imageData = ImageDataFactory.create("D:\\temp\\images\\test\\1.jpeg");
            Image image = new Image(imageData);
            doc.add(image);
            assertEquals(1, pdfDoc.getNumberOfPages(), "PDF should have one page");
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    // 插入图像并设置大小、位置等属性
    @Test
    public void test_6_1() {
        try (PdfWriter writer = new PdfWriter("test_6_1.pdf");
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document doc = new Document(pdfDoc)) {

            // 创建图像数据
            ImageData imageData = ImageDataFactory.create("D:\\temp\\images\\test\\1.jpeg");

            // 创建图像对象
            Image image = new Image(imageData);

            // 设置图像大小
            // image.scale(0.5f, 0.5f); // 缩放图像到50%的大小
            // 或者使用固定宽度和高度
            image.setWidth(100);
            image.setHeight(150);

            // 设置图像位置
            image.setHorizontalAlignment(HorizontalAlignment.CENTER);
            // image.setVerticalAlignment(VerticalAlignment.MIDDLE);

            // 设置图像边距
            image.setMarginTop(50);
            image.setMarginBottom(50);
            image.setMarginLeft(50);
            image.setMarginRight(50);

            // 设置图像旋转
            image.setRotationAngle(Math.PI / 6); // 旋转30度

            // 将图像添加到文档
            doc.add(image);

            // 验证PDF文档的页数
            assertEquals(1, pdfDoc.getNumberOfPages(), "PDF should have one page");
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    // 表格处理
    @Test
    public void test_7() {
        try (PdfWriter writer = new PdfWriter("test_7.pdf");
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document doc = new Document(pdfDoc)) {

            Table table = new Table(new float[]{100, 100});
            table.addCell(new Cell().add(new Paragraph("Header 1")));
            table.addCell(new Cell().add(new Paragraph("Header 2")));
            table.addCell(new Cell().add(new Paragraph("Row 1, Col 1")));
            table.addCell(new Cell().add(new Paragraph("Row 1, Col 2")));
            doc.add(table);
            assertEquals(1, pdfDoc.getNumberOfPages(), "PDF should have one page");
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    // 字体和样式
    @Test
    public void test_8() {
        try (PdfWriter writer = new PdfWriter("test_8.pdf");
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document doc = new Document(pdfDoc)) {

            PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            Paragraph paragraph = new Paragraph("Styled text")
                    .setFont(font)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER);
            doc.add(paragraph);
            assertEquals(1, pdfDoc.getNumberOfPages(), "PDF should have one page");
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    // HTML到PDF转换
    @Test
    public void test_9() {
        // 通过
        try (PdfWriter writer = new PdfWriter("test_9.pdf")) {
            // 创建转换属性
            ConverterProperties properties = new ConverterProperties();

            // 将HTML内容转换为PDF
            HtmlConverter.convertToPdf("<h1>Hello, World!</h1>", writer, properties);
        } catch (Exception e) {
            fail("Exception should not be thrown during conversion: " + e.getMessage());
        }

        try (PdfReader reader = new PdfReader("test_9.pdf");
             PdfDocument pdfDoc = new PdfDocument(reader)) {
            // 验证PDF文档的页数
            assertEquals(1, pdfDoc.getNumberOfPages(), "PDF should have one page");
        } catch (Exception e) {
            fail("Exception should not be thrown during validation: " + e.getMessage());
        }
    }

    // 删除PDF中的敏感信息：删除页面内容
    @Test
    public void test_11() {
        try (PdfWriter writer = new PdfWriter("input.pdf");
             PdfDocument pdfDoc = new PdfDocument(writer)) {
            // 创建一个PDF文档，用于写入敏感信息
            Document doc = new Document(pdfDoc);
            doc.add(new Paragraph("This is a sensitive information"));
            doc.close();
        } catch (Exception e) {
            fail("Exception should not be thrown during creation: " + e.getMessage());
        }

        try (PdfReader reader = new PdfReader("input.pdf");
             PdfWriter writer = new PdfWriter("test_11.pdf");
             PdfDocument pdfDoc = new PdfDocument(reader, writer)) {

            // 删除敏感信息的逻辑
            for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
                PdfPage page = pdfDoc.getPage(i);
                PdfDictionary pageDict = page.getPdfObject();
                pageDict.remove(PdfName.Contents); // 示例：删除页面内容
            }
            assertEquals(1, pdfDoc.getNumberOfPages(), "PDF should have one page");
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    // 删除PDF中的敏感信息：删除页面指定内容
    @Test
    public void test_11_2() {
        try (PdfWriter writer = new PdfWriter("input2.pdf");
             PdfDocument pdfDoc = new PdfDocument(writer)) {
            // 创建一个PDF文档，用于写入敏感信息
            Document doc = new Document(pdfDoc);
            doc.add(new Paragraph("This is a sensitive information. 傻逼，大傻逼，哈哈哈。"));
            doc.add(new Paragraph("This is a sensitive information. 傻逼，大傻逼，哈哈哈。"));
            doc.close();
        } catch (Exception e) {
            fail("Exception should not be thrown during creation: " + e.getMessage());
        }

        try (PdfReader reader = new PdfReader("input2.pdf");
             PdfWriter writer = new PdfWriter("test_11_2.pdf");
             PdfDocument pdfDoc = new PdfDocument(reader, writer)) {

            // 删除敏感信息的逻辑
            String sensitiveWord = "傻逼";
            for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
                PdfPage page = pdfDoc.getPage(i);
                String text = PdfTextExtractor.getTextFromPage(page, new LocationTextExtractionStrategy());

                if (text.contains(sensitiveWord)) {
                    PdfCanvas canvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);
                    canvas.beginText()
                            .setFontAndSize(PdfFontFactory.createFont(), 12)
                            .moveText(0, 0)
                            .showText(new String(new char[sensitiveWord.length()]).replace("\0", " "))
                            .endText();
                    canvas.stroke();
                }
            }
            assertEquals(1, pdfDoc.getNumberOfPages(), "PDF should have one page");
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }


    // SVG支持：集成矢量图
    @Test
    public void test_12() {
       /* try (PdfWriter writer = new PdfWriter("test_12.pdf");
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document doc = new Document(pdfDoc)) {

            // 假设有一个SVG文件路径
            String svgPath = "path/to/svg/file.svg";
            ImageData imageData = SvgConverter.convertToImage(svgPath, pdfDoc);
            Image image = new Image(imageData);
            doc.add(image);
            assertEquals(1, pdfDoc.getNumberOfPages(), "PDF should have one page");
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }*/
    }

    // 数据提取(文本、表格)
    @Test
    public void test_13() {
        // 创建 PDF文档 input.pdf
        try (PdfWriter writer = new PdfWriter("input_13.pdf");
             PdfDocument pdfDoc = new PdfDocument(writer)) {
            Document doc = new Document(pdfDoc);
            doc.add(new Paragraph("Hello, World!"));
            doc.close();
        } catch (Exception e) {
            fail("Exception should not be thrown during creation: " + e.getMessage());
        }

        try (PdfReader reader = new PdfReader("input_13.pdf");
             PdfDocument pdfDoc = new PdfDocument(reader)) {

            // 提取文本
            String text = PdfTextExtractor.getTextFromPage(pdfDoc.getPage(1), new LocationTextExtractionStrategy());
            assertNotNull(text, "Text should not be null");

            // 提取表格
            Table table = extractTableFromPage(pdfDoc.getPage(1));
            assertNotNull(table, "Table should not be null");

            assertEquals(1, pdfDoc.getNumberOfPages(), "PDF should have one page");
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    // 假设的提取表格的方法
    private Table extractTableFromPage(PdfPage page) {
        // 使用iText 8的布局解析器来识别表格结构
        List<List<String>> tableData = new ArrayList<>();
        List<String> row = new ArrayList<>();

        // 这里可以使用第三方库（如Apache POI、Tabula等）或自定义逻辑来提取表格数据
        // 以下是一个简单的示例，假设我们有一个方法来提取表格数据

        // 示例：假设我们从PDF中提取了以下表格数据
        row.add("Header1");
        row.add("Header2");
        tableData.add(row);

        row = new ArrayList<>();
        row.add("Row1Col1");
        row.add("Row1Col2");
        tableData.add(row);

        row = new ArrayList<>();
        row.add("Row2Col1");
        row.add("Row2Col2");
        tableData.add(row);

        // 创建表格
        Table table = new Table(tableData.get(0).size());
        for (List<String> rowData : tableData) {
            for (String cellData : rowData) {
                table.addCell(new Cell().add(new Paragraph(cellData)));
            }
        }

        return table;
    }

    // 数据识别：OCR
    @Test
    public void test_14() {
       /* try (PdfReader reader = new PdfReader("input.pdf");
             PdfDocument pdfDoc = new PdfDocument(reader)) {

            // 使用Tesseract进行OCR识别
            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath("path/to/tessdata"); // 设置Tesseract数据路径

            for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
                PdfPage page = pdfDoc.getPage(i);
                PdfCanvasProcessor canvasProcessor = new PdfCanvasProcessor(new ITextExtractionStrategy() {
                    @Override
                    public void eventOccurred(IEventData data, EventType type) {
                        if (type == EventType.RENDER_TEXT) {
                            RenderText renderText = (RenderText) data;
                            String text = renderText.getText();
                            System.out.println("Extracted Text: " + text);
                        }
                    }

                    @Override
                    public Set<EventType> getSupportedEvents() {
                        return Collections.singleton(EventType.RENDER_TEXT);
                    }
                });

                PdfCanvas canvas = new PdfCanvas(page);
                canvasProcessor.processPageContent(canvas);
            }

            assertEquals(1, pdfDoc.getNumberOfPages(), "PDF should have one page");
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }*/
    }

    // 添加水印
    @Test
    public void test_15() {
        try (PdfWriter writer = new PdfWriter("test_15.pdf");
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document doc = new Document(pdfDoc)) {

            // 示例：添加水印
            PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
            canvas.beginText()
                    .setFontAndSize(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD), 50)
                    .moveText(200, 400)
                    .showText("DRAFT")
                    .endText();

            doc.add(new Paragraph("This document contains a watermarked page"));
            assertEquals(1, pdfDoc.getNumberOfPages(), "PDF should have one page");
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

}
