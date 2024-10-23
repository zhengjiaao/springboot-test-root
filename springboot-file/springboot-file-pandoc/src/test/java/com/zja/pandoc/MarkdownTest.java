package com.zja.pandoc;

import com.zja.pandoc.util.PandocUtil;
import org.junit.jupiter.api.Test;

/**
 * 假设你有一个名为 pandoc.exe 的Pandoc可执行文件在系统路径中
 *
 * @Author: zhengja
 * @Date: 2024-10-22 14:44
 */
public class MarkdownTest {

    // Markdown 文件路径
    static final String inputFilePath = "D:\\temp\\md\\test.md";
    // 输出文件路径
    static final String outputFilePath = "D:\\temp\\md\\test.md";

    @Test
    public void convertFile_test_1() throws PandocUtil.PandocException {
        //  todo 不支持
        // PandocUtil.convertFile(inputFilePath, outputFilePath + ".pdf");

        PandocUtil.convertFile(inputFilePath, outputFilePath + ".docx");

        PandocUtil.convertFile(inputFilePath, outputFilePath + ".html");

        // 问题：多张图片会重叠
        PandocUtil.convertFile(inputFilePath, outputFilePath + ".pptx");

        // 转换从 html 到 pptx，效果与上面一样
        // PandocUtil.convertFile(outputFilePath + ".html", outputFilePath + ".html.pptx");
    }

}
