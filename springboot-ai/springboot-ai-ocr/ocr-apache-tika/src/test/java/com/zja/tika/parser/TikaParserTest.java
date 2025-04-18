package com.zja.tika.parser;

import com.zja.tika.ApacheTikaUtil;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.ocr.TesseractOCRConfig;
import org.apache.tika.parser.pdf.PDFParserConfig;
import org.apache.tika.sax.BodyContentHandler;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @Author: zhengja
 * @Date: 2025-04-17 17:32
 */
public class TikaParserTest {

    // 文件路径
    // String filePath = "D:\\temp\\pdf\\test.pdf";
    String filePath = "D:\\temp\\word\\test.docx";


    // 自动识别 解析器
    @Test
    public void testAutoExtractV1() throws Exception {
        String content = autoExtractText(filePath);
        System.out.println(content);
    }

    public static String autoExtractText(String filePath) throws IOException, TikaException {
        try (InputStream inputStream = Files.newInputStream(Paths.get(filePath))) {
            return new Tika().parseToString(inputStream);
        }
    }

    // 自动识别 解析器
    @Test
    public void testAutoExtractV2() throws Exception {
        String content = autoExtractTextV2(filePath);
        System.out.println(content);
    }

    public static String autoExtractTextV2(String filePath) throws TikaException, IOException, SAXException {
        String content = null;

        // 加载测试用的文件
        try (InputStream inputStream = Files.newInputStream(Paths.get(filePath))) {

            // 创建一个解析器（这里使用自动识别解析器 AutoDetectParser）
            Parser parser = new AutoDetectParser();

            // 创建一个处理器，用于提取文本内容
            BodyContentHandler handler = new BodyContentHandler();

            // 创建元数据对象，用于存储提取的元数据
            Metadata metadata = new Metadata();

            // 创建解析上下文对象
            ParseContext context = new ParseContext();
            context.set(Parser.class, parser);

            // 执行解析过程
            parser.parse(inputStream, handler, metadata, context);

            // 提取文本内容
            content = handler.toString();

            // 输出文本内容
            /*if (content.trim().isEmpty()) {
                System.out.println("No content found.");
            } else {
                System.out.println("Content: " + content);
            }*/

        }

        return content;
    }
}
