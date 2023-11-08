/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-08 9:24
 * @Since:
 */
package com.zja;

import com.zja.tika.ApacheTikaUtil;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Apache Tika 媒体文件提取功能单元测试实例
 *
 * @author: zhengja
 * @since: 2023/11/08 9:24
 */
@Deprecated // todo 无法提取 mp3的内容，包括：音频，视频
//tika可以从图像，音频，视频文件中提取元数据，但是几乎无法提取出任何有价值的文本内容，在大多数场景下建议禁用这些类型的Parser
//针对图像，可以使用TesseractOCRParser进行OCR操作，这需要服务器安装了tesseract
public class MediaExtractionTest {

    @Test
    public void testExtractMP3Metadata() throws Exception {

        // 加载测试用的MP3文件
        try (InputStream inputStream = Files.newInputStream(Paths.get("D:\\temp\\mp3\\成都-赵雷.mp3"))) {

            // 创建一个解析器（这里使用Mp3Parser）
            Parser parser = new Mp3Parser();

            // 创建一个处理器，用于提取文本内容
            BodyContentHandler handler = new BodyContentHandler();

            // 创建元数据对象，用于存储提取的元数据
            Metadata metadata = new Metadata();

            // 创建解析上下文对象
            ParseContext context = new ParseContext();
            context.set(Parser.class, parser);

            // 执行解析过程
            parser.parse(inputStream, handler, metadata, context);

            // 提取并验证元数据: 注，MP3文件的歌词信息并不是所有文件都包含的标准元数据，而是一种可选的附加信息。
            String title = metadata.get("title");
            String artist = metadata.get("artist");
            String album = metadata.get("album");
            String duration = metadata.get("xmpDM:duration"); // 自定义元数据

            System.out.println("title：" + title);
            System.out.println("artist：" + artist);
            System.out.println("album：" + album);
            System.out.println("duration：" + duration);  // 假设MP3文件的时长为328.46毫秒

            // 提取文本内容
            String content = handler.toString();

            // 输出文本内容
            if (content.trim().isEmpty()) {
                System.out.println("No content found.");
            } else {
                System.out.println("Content: " + content);
            }

        } catch (IOException | SAXException | TikaException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testExtractMP3Metadata2() throws Exception {
        String extractedContent = ApacheTikaUtil.extractedContent("D:\\temp\\mp3\\成都-赵雷.mp3");
        System.out.println(extractedContent);
    }
}
