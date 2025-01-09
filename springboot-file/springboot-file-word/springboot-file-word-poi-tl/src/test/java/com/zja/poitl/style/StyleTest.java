package com.zja.poitl.style;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.TextRenderData;
import com.deepoove.poi.data.Texts;
import com.deepoove.poi.data.style.Style;
import com.zja.poitl.util.WordPoiTLUtil;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhengja
 * @Date: 2024-12-30 9:24
 */
public class StyleTest {

    // 设置字体颜色
    @Test
    public void test_color_1() throws IOException {
        // 创建一个Map来存储占位符和对应的文本数据
        Map<String, Object> data = new HashMap<>();
        data.put("name", createColoredText("John Doe", "FF0000")); // 红色字体，有效的
        data.put("age", createColoredText("25", "00FF00")); // 绿色字体，有效的
        XWPFTemplate xwpfTemplate = XWPFTemplate.compile(getResourceAsStream("templates/word/style/template_color.docx")).render(data);
        xwpfTemplate.writeAndClose(Files.newOutputStream(Paths.get("target/output_style_color.docx")));
    }

    // 设置字体颜色-动态字体
    @Test
    public void test_color_2() throws IOException {
        // 动态生成的句子
        String sentence = "好美美丽的花朵，这个春天好美呀，你好世界。";

        List<String> words = Lists.list("春天", "好美");

        Map<String, String> wordsMap = new HashMap<>();

        for (int i = 0; i < words.size(); i++) {
            String word = words.get(i);
            String wordKey = word + i;
            String wordValue = "{{" + wordKey + "}}";
            if (sentence.contains(word)) {
                sentence = sentence.replace(word, wordValue);
                wordsMap.put(wordKey, word);
            }
        }

        // 创建一个Map来存储占位符和对应的文本数据
        Map<String, Object> data = new HashMap<>();
        data.put("name", createColoredText("John Doe", "FF0000")); // 红色字体，有效的
        data.put("age", createColoredText("25", "00FF00")); // 绿色字体，有效的
        data.put("p_constructContent", sentence); // 默认设置 {{p_constructContent}}

        Map<String, Object> data2 = new HashMap<>();
        for (Map.Entry<String, String> entry : wordsMap.entrySet()) {
            data2.put(entry.getKey(), createColoredText(entry.getValue(), "FF0000")); // 红色字体
        }

        Path path = Paths.get("target/output_style_color_2.docx");

        XWPFTemplate xwpfTemplate = XWPFTemplate.compile(getResourceAsStream("templates/word/style/template_color_2.docx")).render(data);
        xwpfTemplate.writeAndClose(Files.newOutputStream(path));

        XWPFTemplate xwpfTemplate2 = XWPFTemplate.compile(path.toFile().getAbsolutePath()).render(data2);
        xwpfTemplate2.writeAndClose(Files.newOutputStream(Paths.get("target/output_style_color_2_1.docx")));
    }

    @Test
    public void test_color_3() throws IOException {
        // 创建一个Map来存储占位符和对应的文本数据
        Map<String, Object> data = new HashMap<>();
        data.put("word_0", createColoredText("John Doe", "FF0000")); // 红色字体，有效的
        XWPFTemplate xwpfTemplate = XWPFTemplate.compile(getResourceAsStream("templates/word/style/template_color_3.docx")).render(data);
        xwpfTemplate.writeAndClose(Files.newOutputStream(Paths.get("target/output_style_color_3.docx")));
    }

    private Object createColoredText(String text, String color) {
        return Texts.of(text).color(color).create();
    }

    private InputStream getResourceAsStream(String path) {
        return getClass().getClassLoader().getResourceAsStream(path);
    }

}
