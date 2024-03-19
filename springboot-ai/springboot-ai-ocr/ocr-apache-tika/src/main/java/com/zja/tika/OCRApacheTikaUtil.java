/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-08 9:49
 * @Since:
 */
package com.zja.tika;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.ocr.TesseractOCRConfig;
import org.apache.tika.parser.pdf.PDFParserConfig;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Apache Tika 提取文件内容
 *
 * @author: zhengja
 * @since: 2023/11/08 9:49
 */
@Slf4j
public class OCRApacheTikaUtil {

    private OCRApacheTikaUtil() {

    }

    public static String autoExtractedContent(String filePath) {
        String content = null;

        // 加载测试用的文件
        try (InputStream inputStream = Files.newInputStream(Paths.get(filePath))) {

            // 创建一个解析器（这里使用自动识别解析器 AutoDetectParser）
            Parser parser = new AutoDetectParser();

            // 创建一个处理器，用于提取文本内容
            BodyContentHandler handler = new BodyContentHandler();

            // 创建元数据对象，用于存储提取的元数据
            Metadata metadata = new Metadata();

            //提取图像内容时(包含pdf等中的图像)，采用TesseractOCR方式识别图像内容，可以友好解决语言问题
            TesseractOCRConfig config = new TesseractOCRConfig();
            config.setLanguage("chi_sim+eng"); // 支持中英文

            PDFParserConfig pdfConfig = new PDFParserConfig();
            pdfConfig.setExtractInlineImages(true);

            // 创建解析上下文对象
            ParseContext context = new ParseContext();
            context.set(Parser.class, parser);
            context.set(TesseractOCRConfig.class, config);
            context.set(PDFParserConfig.class, pdfConfig);

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

        } catch (IOException | SAXException | TikaException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

        return content;
    }
}
