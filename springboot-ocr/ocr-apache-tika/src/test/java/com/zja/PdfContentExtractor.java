/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-02 15:26
 * @Since:
 */
package com.zja;

import com.zja.tika.ApacheTikaUtil;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * @author: zhengja
 * @since: 2023/11/02 15:26
 */
public class PdfContentExtractor {

    @Test
    public void testExtractPDF() throws Exception {
        File pdfFile = new File("D:\\temp\\pdf\\test.pdf"); // PDF文件路径

        try (InputStream stream = Files.newInputStream(pdfFile.toPath())) {
            // 创建一个解析器
            Parser parser = new AutoDetectParser();
            // 创建一个内容处理器
            BodyContentHandler handler = new BodyContentHandler();
            // 创建元数据对象
            Metadata metadata = new Metadata();
            // 创建解析上下文
            ParseContext context = new ParseContext();

            // 提取PDF内容和元数据
            parser.parse(stream, handler, metadata, context);
            // 打印PDF内容
            System.out.println("PDF内容:");
            System.out.println(handler.toString());

            // 打印PDF的元数据
            System.out.println("PDF元数据:");
            for (String name : metadata.names()) {
                System.out.println(name + ": " + metadata.get(name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testExtractPDF2() throws Exception {
        String extractedContent = ApacheTikaUtil.extractedContent("D:\\temp\\pdf\\test.pdf");
        System.out.println(extractedContent);
    }
}
