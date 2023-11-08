/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-08 11:08
 * @Since:
 */
package com.zja;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 图像内容文字提取
 *
 * @author: zhengja
 * @since: 2023/11/08 11:08
 */
@Deprecated //todo 效果不好，提取中文不友好，推荐参考：TikaAndTesseractOCRTest.java
public class ImageContentExtractionTest {

    @Test
    public void testExtractImageContent() throws Exception {
        // 加载图像文件
        String filePath = "D:\\temp\\ocr\\input.png";
        InputStream input = new FileInputStream(filePath);

        // 创建解析器（AutoDetectParser）
        Parser parser = new AutoDetectParser();

        // 创建元数据对象
        Metadata metadata = new Metadata();

        // 创建处理器，用于提取文本内容
        BodyContentHandler handler = new BodyContentHandler();

        // 设置解析器为图像解析器（ImageParser）
//        metadata.set(Metadata.CONTENT_TYPE, "image/jpeg");
//        metadata.set(Metadata..RESOURCE_NAME_KEY, filePath);

        // 设置解析器为图像解析器（ImageParser）,Tika将使用Tesseract OCR引擎进行光学字符识别。
        ParseContext context = new ParseContext();
//        context.set(Parser.class, new ImageParser());

        // 执行解析过程
        parser.parse(input, handler, metadata, context);

        // 提取文本内容
        String content = handler.toString();

        System.out.println(content);

        // 关闭输入流
        input.close();

        // 验证文本内容
        Assert.assertNotNull(content);
        Assert.assertNotEquals("", content.trim());
    }

}
