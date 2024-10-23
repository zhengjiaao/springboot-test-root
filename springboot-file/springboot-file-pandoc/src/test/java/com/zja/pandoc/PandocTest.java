package com.zja.pandoc;

import com.zja.pandoc.util.PandocUtil;
import org.junit.jupiter.api.Test;

/**
 * @Author: zhengja
 * @Date: 2024-10-22 14:30
 */
public class PandocTest {

    // Markdown 文件路径
    static final String inputFilePath = "D:\\temp\\md\\test.md";
    // 输出文件路径
    static final String outputFilePath = "D:\\temp\\md\\test.md";

    @Test
    public void convertFile_test_1() throws PandocUtil.PandocException {
        // 转换从 markdown 到 pptx
        PandocUtil.convertFile(inputFilePath, outputFilePath + ".pptx");

        // 转换从 markdown 到 html
        PandocUtil.convertFile(inputFilePath, outputFilePath + ".html");
    }

}
