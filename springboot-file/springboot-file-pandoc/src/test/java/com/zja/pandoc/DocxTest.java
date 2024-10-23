package com.zja.pandoc;

import com.zja.pandoc.util.PandocUtil;
import org.junit.jupiter.api.Test;

/**
 * @Author: zhengja
 * @Date: 2024-10-22 14:45
 */
public class DocxTest {

    // 输入文件路径
    static final String inputFilePath = "D:\\temp\\word\\test.md.docx";
    // 输出文件路径
    static final String outputFilePath = inputFilePath;

    @Test
    public void convertFile_test_1() throws PandocUtil.PandocException {
        // PandocUtil.convertFile(inputFilePath, outputFilePath + ".pdf"); // todo 不支持

        // 问题：多张图片会重叠
        PandocUtil.convertFile(inputFilePath, outputFilePath + ".pptx");

        // 问题：图片会丢失
        PandocUtil.convertFile(inputFilePath, outputFilePath + ".md");
        // PandocUtil.executeCommand("pandoc -s " + inputFilePath + " -t markdown -o " + outputFilePath + ".md");
    }
}
