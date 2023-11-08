/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-08 9:49
 * @Since:
 */
package com.zja.tika;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author: zhengja
 * @since: 2023/11/08 9:49
 */
public class ApacheTikaUtil {

    private ApacheTikaUtil() {

    }

    public static String extractedContent(String filePath) {
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

        } catch (IOException | SAXException | TikaException e) {
            e.printStackTrace();
        }

        return content;
    }
}
