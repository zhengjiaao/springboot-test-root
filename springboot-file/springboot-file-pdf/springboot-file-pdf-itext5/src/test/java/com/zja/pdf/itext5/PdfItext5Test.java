package com.zja.pdf.itext5;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author: zhengja
 * @Date: 2024-10-23 10:52
 */
public class PdfItext5Test {

    Path pdfPath = Paths.get("target", "test_1.pdf");

    // 创建和编辑PDF文档：创建一个简单的PDF文档并添加文本。
    @Test
    public void test_1() {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, Files.newOutputStream(Paths.get("target", "test_1.pdf")));
            document.open();
            document.add(new Paragraph("Hello, World!"));
            document.close();
            assertTrue(pdfPath.toFile().exists());
        } catch (DocumentException | IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    // PDF文档的加密和解密：创建一个加密的PDF文档，并尝试使用用户密码读取它。
    @Test
    public void test_2() {
      /*  Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("test_2_encrypted.pdf"));
            byte[] userPassword = MakeKey.createKey("user", 40);
            byte[] ownerPassword = MakeKey.createKey("owner", 40);
            writer.setEncryption(userPassword, ownerPassword, PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128);
            document.open();
            document.add(new Paragraph("Encrypted PDF"));
            document.close();
            assertTrue(new File("test_2_encrypted.pdf").exists());

            PdfReader reader = new PdfReader("test_2_encrypted.pdf", "user".getBytes());
            assertNotNull(reader);
        } catch (DocumentException | IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }*/
    }

    // PDF文档的合并和拆分：创建两个PDF文档，然后将它们合并成一个新文档。
    @Test
    public void test_3() {
        Document document1 = new Document();
        Document document2 = new Document();
        try {
            PdfWriter.getInstance(document1, Files.newOutputStream(Paths.get("test_3_part1.pdf")));
            PdfWriter.getInstance(document2, Files.newOutputStream(Paths.get("test_3_part2.pdf")));
            document1.open();
            document2.open();
            document1.add(new Paragraph("Part 1"));
            document2.add(new Paragraph("Part 2"));
            document1.close();
            document2.close();

            Document mergedDocument = new Document();
            PdfCopy copy = new PdfCopy(mergedDocument, Files.newOutputStream(Paths.get("test_3_merged.pdf")));
            mergedDocument.open();
            PdfReader reader1 = new PdfReader("test_3_part1.pdf");
            PdfReader reader2 = new PdfReader("test_3_part2.pdf");
            for (int i = 1; i <= reader1.getNumberOfPages(); i++) {
                PdfImportedPage page = copy.getImportedPage(reader1, i);
                copy.addPage(page);
            }
            for (int i = 1; i <= reader2.getNumberOfPages(); i++) {
                PdfImportedPage page = copy.getImportedPage(reader2, i);
                copy.addPage(page);
            }
            mergedDocument.close();
            assertTrue(new File("test_3_merged.pdf").exists());
        } catch (DocumentException | IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    // PDF文档的图片处理：在PDF文档中添加一张图片。
    @Test
    public void test_4() {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, Files.newOutputStream(Paths.get("test_4.pdf")));
            document.open();
            Image image = Image.getInstance("D:\\temp\\images\\test\\1.jpeg");
            document.add(image);
            document.close();
            assertTrue(new File("test_4.pdf").exists());
        } catch (DocumentException | IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    // PDF文档的文字处理：在PDF文档中添加一段文字。
    @Test
    public void test_5() {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, Files.newOutputStream(Paths.get("test_5.pdf")));
            document.open();
            document.add(new Paragraph("This is a paragraph with some text."));
            document.close();
            assertTrue(new File("test_5.pdf").exists());
        } catch (DocumentException | IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    // PDF文档的表格处理：在PDF文档中添加一个表格。
    @Test
    public void test_6() {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, Files.newOutputStream(Paths.get("test_6.pdf")));
            document.open();
            PdfPTable table = new PdfPTable(3);
            PdfPCell cell = new PdfPCell(new Paragraph("Header 1"));
            table.addCell(cell);
            table.addCell("Cell 2");
            table.addCell("Cell 3");
            document.add(table);
            document.close();
            assertTrue(new File("test_6.pdf").exists());
        } catch (DocumentException | IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    // PDF文档的签名和验证：对PDF文档进行数字签名，并验证签名的有效性。
    @Test
    public void test_7() {
        /*Security.addProvider(new BouncyCastleProvider());
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("test_7_signed.pdf"));
            document.open();
            document.add(new Paragraph("Signed PDF"));
            document.close();

            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(new FileInputStream("keystore.jks"), "password".toCharArray());
            String alias = ks.aliases().nextElement();
            PrivateKey pk = (PrivateKey) ks.getKey(alias, "password".toCharArray());
            X509Certificate cert = (X509Certificate) ks.getCertificate(alias);

            PdfReader reader = new PdfReader("test_7_signed.pdf");
            FileOutputStream os = new FileOutputStream("test_7_signed_and_validated.pdf");
            PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0');
            PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
            appearance.setVisibleSignature(new Rectangle(36, 748, 144, 780), 1, "sig");
            ExternalDigest digest = new BouncyCastleDigest();
            MakeSignature.signDetached(appearance, digest, pk, new Certificate[]{cert}, null, null, null, 0, MakeSignature.CryptoStandard.CMS);

            reader = new PdfReader("test_7_signed_and_validated.pdf");
            PdfPKCS7 sig = reader.getAcroFields().verifySignature("sig");
            assertTrue(sig.verify());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }*/
    }

    // HTML到PDF转换：轻松将网页内容转化为符合PDF/UA标准的文件。
    @Test
    public void test_8() {
        // 使用 XMLWorkerHelper 将HTML字符串解析并转换为PDF文档。
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, Files.newOutputStream(Paths.get("test_8.pdf")));
            document.open();
            String html = "<html><body><h1>Hello, World!</h1><p>This is a test.</p></body></html>";
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new StringReader(html));
            document.close();
            assertTrue(new File("test_8.pdf").exists());
        } catch (DocumentException | IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    // PDF红化处理：删除PDF中的敏感信息。
    @Test
    public void test_9() {
        // 创建一个包含敏感信息的PDF文档。
        // 使用 PdfStamper 在敏感信息的位置绘制黑色矩形以覆盖敏感信息。
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, Files.newOutputStream(Paths.get("test_9_original.pdf")));
            document.open();
            document.add(new Paragraph("This is a sensitive document. Confidential information: 123-45-6789"));
            document.close();

            PdfReader reader = new PdfReader("test_9_original.pdf");
            PdfStamper stamper = new PdfStamper(reader, Files.newOutputStream(Paths.get("test_9_redacted.pdf")));
            PdfContentByte cb = stamper.getOverContent(1);
            cb.setColorFill(BaseColor.BLACK);
            cb.rectangle(100, 700, 200, 50); // 覆盖敏感信息的位置
            cb.fill();
            stamper.close();
            reader.close();
            assertTrue(new File("test_9_redacted.pdf").exists());
        } catch (DocumentException | IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    // SVG支持：集成矢量图，确保PDF的高质量显示。
    @Test
    public void test_10() {
        // 在PDF文档中添加一个SVG图像。
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, Files.newOutputStream(Paths.get("test_10.pdf")));
            document.open();
            Image svgImage = Image.getInstance("D:\\temp\\svg\\test.svg");
            document.add(svgImage);
            document.close();
            assertTrue(new File("test_10.pdf").exists());
        } catch (DocumentException | IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    // 数据提取：从PDF中提取文本和表格信息。
    @Test
    public void test_11() {
        // 创建一个包含文本和表格的PDF文档。
        // 使用 PdfTextExtractor 提取第一页的文本内容，并验证是否包含预期的文本
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, Files.newOutputStream(Paths.get("test_11.pdf")));
            document.open();
            document.add(new Paragraph("This is a test document with some text and a table."));
            PdfPTable table = new PdfPTable(3);
            PdfPCell cell = new PdfPCell(new Paragraph("Header 1"));
            table.addCell(cell);
            table.addCell("Cell 2");
            table.addCell("Cell 3");
            document.add(table);
            document.close();

            // 检查文件是否存在
            PdfReader reader = new PdfReader("test_11.pdf");
            if (reader == null) {
                fail("PdfReader is null");
            }

            String text = PdfTextExtractor.getTextFromPage(reader, 1);
            Assertions.assertNotNull(text);
            Assertions.assertTrue(text.contains("This is a test document with some text and a table."));

            PdfDictionary pageDict = reader.getPageN(1);
            if (pageDict == null) {
                fail("Page dictionary is null");
            }

            PdfArray contentArray = pageDict.getAsArray(PdfName.CONTENTS);
            if (contentArray == null) {
                fail("Content array is null");
            }
        } catch (DocumentException | IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
}
