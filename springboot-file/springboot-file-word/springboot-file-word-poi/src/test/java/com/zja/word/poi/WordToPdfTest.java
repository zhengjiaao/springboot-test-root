// package com.zja.word.poi;
//
// import org.apache.pdfbox.pdmodel.PDDocument;
// import org.apache.pdfbox.pdmodel.PDPage;
// import org.apache.pdfbox.pdmodel.PDPageContentStream;
// import org.apache.pdfbox.pdmodel.font.PDType1Font;
// import org.apache.poi.xwpf.usermodel.XWPFDocument;
// import org.apache.poi.xwpf.usermodel.XWPFParagraph;
// import org.apache.poi.xwpf.usermodel.XWPFRun;
// import org.junit.jupiter.api.Test;
//
// import java.io.FileInputStream;
// import java.io.FileOutputStream;
// import java.io.IOException;
//
// /**
//  * Apache POI本身并没有提供将Word文档直接转换为PDF、图像等功能
//  *
//  * @author: zhengja
//  * @since: 2024/01/19 11:17
//  */
// @Deprecated
// public class WordToPdfTest {
//
//     private static final String input_docx = "D:\\temp\\word\\test.docx";
//     private static final String output_pdf = "D:\\temp\\word\\test.docx.pdf";
//
//     // todo 未成功，无法加载字体
//     @Test
//     public void test() {
//         try (FileInputStream fis = new FileInputStream(input_docx);
//              FileOutputStream fos = new FileOutputStream(output_pdf)) {
//             // 加载Word文档
//             XWPFDocument document = new XWPFDocument(fis);
//
//             // 创建PDF文档
//             PDDocument pdfDocument = new PDDocument();
//             PDPage page = new PDPage();
//             pdfDocument.addPage(page);
//
//             // 创建PDF内容流
//             PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page);
//
//             contentStream.beginText(); // 开始文本操作
//             contentStream.setFont(PDType1Font.HELVETICA, 12); // 设置字体 2.x版本
//             // contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12); // 设置字体 3.x版本
//             // 加载SimSun字体
//             // PDTrueTypeFont font = PDTrueTypeFont.load(pdfDocument, Files.newInputStream(Paths.get("C:\\WINDOWS\\FONTS\\方正粗黑宋简体.ttf")), Encoding.getInstance(COSName.WIN));
//             // contentStream.setFont(font, 12); // 设置字体 3.x版本
//
//             // 逐段落写入PDF内容
//             for (XWPFParagraph paragraph : document.getParagraphs()) {
//                 for (XWPFRun run : paragraph.getRuns()) {
//                     contentStream.showText(run.getText(0));
//                     contentStream.newLine();
//                 }
//             }
//
//             contentStream.endText(); // 结束文本操作
//
//             // 关闭PDF内容流和文档
//             contentStream.close();
//             pdfDocument.save(fos);
//             pdfDocument.close();
//
//             System.out.println("Word文档成功转换为PDF！");
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }
//
// }
