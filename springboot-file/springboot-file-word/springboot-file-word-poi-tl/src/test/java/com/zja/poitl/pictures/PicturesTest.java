package com.zja.poitl.pictures;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.*;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;
import com.zja.poitl.util.ResourcesFileUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设置图片：图片标签以@开始 {{@var}}
 *
 * @author: zhengja
 * @since: 2024/04/01 17:19
 */
public class PicturesTest {

    public static final Map<String, Object> put = new HashMap<>();

    private static void put(String key, Object value) {
        put.put(key, value);
    }

    // 图片单张插入示例
    @Test
    public void test_1() throws IOException {
        // 指定图片路径
        put("image", "logo.png");
        // svg图片
        put("svg", "https://img.shields.io/badge/jdk-1.6%2B-orange.svg");

        // 图片文件 设置图片宽高
        String localPath = getPath("pictures/logo.png");
        put("image1", Pictures.ofLocal(localPath).size(120, 120).create());

        // 图片流
        // put("streamImg", Pictures.ofStream(Files.newInputStream(Paths.get("logo.png")), PictureType.PNG)
        put("streamImg", Pictures.ofStream(ResourcesFileUtil.getResourceAsStream("pictures/logo.png"), PictureType.PNG).size(100, 120).create());

        // 网络图片(注意网络耗时对系统可能的性能影响)
        put("urlImg", Pictures.ofUrl("http://deepoove.com/images/icecream.png").size(100, 100).create());

        // java图片
        // put("buffered", Pictures.ofBufferedImage(bufferImage, PictureType.PNG)
        //         .size(100, 100).create());

        // base64图片
        // put("base64", Pictures.ofBase64("", PictureType.PNG).size(100, 100).create());

        // 根据模版生成 word
        XWPFTemplate template = XWPFTemplate.compile(ResourcesFileUtil.getResourceAsStream("templates/word/pictures/pictures_1.docx")).render(put);
        template.writeAndClose(Files.newOutputStream(Paths.get("target/out_pictures_1.docx")));
    }

    // 图片多张插入示例
    @Test
    public void test_2() throws IOException {
        List<Map<String, Object>> imageList = new ArrayList<>();
        List<Map<String, Object>> imageUrlList = new ArrayList<>();

        // 本地图片
        Map<String, Object> mapLocal = new HashMap<>();
        String localPath = getPath("pictures/logo.png");
        mapLocal.put("pictures", Pictures.ofLocal(localPath).size(120, 120).create());
        // 网络图片
        Map<String, Object> mapUrl = new HashMap<>();
        mapUrl.put("pictures", Pictures.of("http://deepoove.com/images/icecream.png").size(100, 100).create());

        imageList.add(mapLocal);
        imageList.add(mapLocal);
        imageUrlList.add(mapUrl);
        imageUrlList.add(mapUrl);

        put("imageList", imageList);
        put("imageUrlList", imageUrlList);

        XWPFTemplate template = XWPFTemplate.compile(ResourcesFileUtil.getResourceAsStream("templates/word/pictures/pictures_2.docx")).render(put);
        template.writeAndClose(Files.newOutputStream(Paths.get("target/out_pictures_2.docx")));
    }

    private String getPath(String pathName) {
        return ResourcesFileUtil.getFile(pathName).getAbsolutePath();
    }
}
