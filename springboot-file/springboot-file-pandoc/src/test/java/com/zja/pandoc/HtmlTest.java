package com.zja.pandoc;

import com.zja.pandoc.util.PandocUtil;
import org.junit.jupiter.api.Test;

/**
 * @Author: zhengja
 * @Date: 2024-10-22 14:45
 */
public class HtmlTest {

    // 输入文件路径
    static final String inputFilePath = "D:\\temp\\html\\test.md.html";
    // 输出文件路径
    static final String outputFilePath = inputFilePath;

    @Test
    public void convertFile_test_1() throws PandocUtil.PandocException {

        PandocUtil.convertFileV2(inputFilePath, outputFilePath + ".md");

        // 问题：多张图片会重叠
        PandocUtil.convertFile(inputFilePath, outputFilePath + ".pptx");
    }
}
