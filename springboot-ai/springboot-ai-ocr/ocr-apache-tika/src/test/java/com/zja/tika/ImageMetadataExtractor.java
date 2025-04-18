/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-02 15:32
 * @Since:
 */
package com.zja.tika;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author: zhengja
 * @since: 2023/11/02 15:32
 */
@Deprecated // todo 不理想，无法打印正确内容，推荐参考：TikaAndTesseractOCRTest.java
public class ImageMetadataExtractor {

    @Test
    public void test() {
        File imageFile = new File("D:\\temp\\ocr\\input.png"); // 图片文件路径

        try (InputStream stream = new FileInputStream(imageFile)) {
            // 创建一个解析器
            Parser parser = new AutoDetectParser();

            // 创建一个内容处理器
            BodyContentHandler handler = new BodyContentHandler(-1); // 设置字符编码为UTF-8

            // 创建元数据对象
            Metadata metadata = new Metadata();

            // 创建解析上下文
            ParseContext context = new ParseContext();

            // 提取图片元数据
            parser.parse(stream, handler, metadata, context);

            // 打印PNG内容
            System.out.println("PNG内容:");
            System.out.println(handler.toString()); //todo 不理想，无法打印正确内容，需要 Tesseract OCR与Apache Tika结合使用

            // 打印图片的元数据
            for (String name : metadata.names()) {
                System.out.println(name + ": " + metadata.get(name));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
