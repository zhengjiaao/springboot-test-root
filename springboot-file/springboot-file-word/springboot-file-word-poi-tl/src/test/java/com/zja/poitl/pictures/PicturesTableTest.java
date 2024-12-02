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
 * @Author: zhengja
 * @Date: 2024-11-20 17:40
 */
public class PicturesTableTest {

    public static final Map<String, Object> data = new HashMap<>();

    private static void put(String key, Object value) {
        data.put(key, value);
    }

    private String getPath(String fileName) {
        return getClass().getResource("/" + fileName).getPath();
    }

    // 表格不循环行：单元格内容插入图片（一张、多张）
    @Test
    public void test_1() throws IOException {
        List<Map<String, Object>> imageList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("pictures", Pictures.ofLocal(getPath("pictures/logo.png")).size(120, 120).create());
            imageList.add(map);
        }

        put("imageList", imageList);
        put("image", Pictures.ofLocal(getPath("pictures/logo.png")).size(120, 120).create());

        XWPFTemplate template = XWPFTemplate
                .compile(ResourcesFileUtil.getResourceAsStream("templates/word/pictures/pictures_table_1.docx"))
                .render(data);
        template.writeToFile("target/out_pictures_table_1.docx");
    }

    // 表格不循环行：一个单元格内插入多张图
    @Test
    public void test_2() throws IOException {

        List<Map<String, Object>> imageList = new ArrayList<>();
        List<Map<String, Object>> imageUrlList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {

            Map<String, Object> mapLocal = new HashMap<>();
            String localPath = getPath("pictures/logo.png");
            mapLocal.put("pictures", Pictures.ofLocal(localPath).size(120, 120).create());
            imageList.add(mapLocal);
            imageList.add(mapLocal);

            Map<String, Object> mapUrl = new HashMap<>();
            mapUrl.put("pictures", Pictures.of("http://deepoove.com/images/icecream.png").size(100, 100).create());
            imageUrlList.add(mapUrl);
            imageUrlList.add(mapUrl);
        }

        put("imageList", imageList);
        put("imageUrlList", imageUrlList);
        XWPFTemplate template = XWPFTemplate.compile(ResourcesFileUtil.getResourceAsStream("templates/word/pictures/pictures_table_2.docx")).render(data);
        template.writeAndClose(Files.newOutputStream(Paths.get("target/out_pictures_table_2.docx")));
    }

    // 表格循环行：一个单元格内插入单张图
    @Test
    public void test_3() throws IOException {
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

        LoopRowTableRenderPolicy hackLoopTableRenderPolicy = new LoopRowTableRenderPolicy();
        Configure config = Configure.builder()
                .bind("imageList", hackLoopTableRenderPolicy)
                .bind("imageUrlList", hackLoopTableRenderPolicy)
                // .useSpringEL() // 需要引入spring适配依赖
                .build();

        XWPFTemplate template = XWPFTemplate.compile(ResourcesFileUtil.getResourceAsStream("templates/word/pictures/pictures_table_3.docx"), config).render(data);
        template.writeAndClose(Files.newOutputStream(Paths.get("target/out_pictures_table_3.docx")));
    }

    // 表格循环行：一个单元格内插入多张图片
    @Test
    public void test_4() throws IOException {
        List<Map<String, Object>> imageList = new ArrayList<>();
        List<Map<String, Object>> imageUrlList = new ArrayList<>();

        Map<String, Object> dataImages1 = new HashMap<>();
        dataImages1.put("image1", Pictures.ofLocal(getPath("pictures/logo.png")).size(120, 120).create());
        dataImages1.put("image2", Pictures.ofLocal(getPath("pictures/logo.png")).size(120, 120).create());
        imageList.add(dataImages1);
        imageList.add(dataImages1);

        Map<String, Object> dataImages2 = new HashMap<>();
        dataImages2.put("image1", Pictures.of("http://deepoove.com/images/icecream.png").size(100, 100).create());
        dataImages2.put("image2", Pictures.of("http://deepoove.com/images/icecream.png").size(100, 100).create());
        imageUrlList.add(dataImages2);
        imageUrlList.add(dataImages2);

        put("imageList", imageList);
        put("imageUrlList", imageUrlList);

        LoopRowTableRenderPolicy hackLoopTableRenderPolicy = new LoopRowTableRenderPolicy();
        Configure config = Configure.builder()
                .bind("imageList", hackLoopTableRenderPolicy)
                .bind("imageUrlList", hackLoopTableRenderPolicy)
                // .useSpringEL() // 需要引入spring适配依赖
                .build();

        // 渲染模板
        XWPFTemplate template = XWPFTemplate
                .compile(ResourcesFileUtil.getResourceAsStream("templates/word/pictures/pictures_table_4.docx"), config)
                .render(data);
        template.writeAndClose(Files.newOutputStream(Paths.get("target/out_pictures_table_4.docx")));
    }

}
