package com.zja.poitl.pictures;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.PictureType;
import com.deepoove.poi.data.Pictures;
import com.zja.poitl.util.ResourcesFileUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * 设置图片：图片标签以@开始 {{@var}}
 *
 * @author: zhengja
 * @since: 2024/04/01 17:19
 */
public class PicturesPoiTLTest {

    private static final Map<String, Object> put = new HashMap<>();

    private static void put(String key, Object value) {
        put.put(key, value);
    }

    private static void generateWord() throws IOException {
        XWPFTemplate template = XWPFTemplate.compile(ResourcesFileUtil.getResourceAsStream("pictures.docx")).render(put);
        template.writeAndClose(Files.newOutputStream(Paths.get("pictures-output.docx")));
    }

    // 图片插入示例
    @Test
    public void test_1() throws IOException {
        // 指定图片路径
        put("image", "logo.png");
        // svg图片
        put("svg", "https://img.shields.io/badge/jdk-1.6%2B-orange.svg");

        // 设置图片宽高
        put("image1", Pictures.ofLocal("logo.png").size(120, 120).create());

        // 图片流
        // put("streamImg", Pictures.ofStream(Files.newInputStream(Paths.get("logo.png")), PictureType.PNG)
        put("streamImg", Pictures.ofStream(ResourcesFileUtil.getResourceAsStream("pictures/logo.png"), PictureType.PNG)
                .size(100, 120).create());

        // 网络图片(注意网络耗时对系统可能的性能影响)
        put("urlImg", Pictures.ofUrl("http://deepoove.com/images/icecream.png")
                .size(100, 100).create());

        // java图片
        // put("buffered", Pictures.ofBufferedImage(bufferImage, PictureType.PNG)
        //         .size(100, 100).create());

        // 根据模版生成 word
        generateWord();
    }

}
