package com.zja.jodconverter;

import com.zja.jodconverter.util.LibreOfficeUtil;
import org.junit.jupiter.api.Test;

/**
 * @Author: zhengja
 * @Date: 2025-04-18 17:08
 */
public class JodconverterTests {

    @Test
    public void test() {
        // 源文件路径
        String sourceFilePath = "D:\\test\\test.docx";
        // 目标文件路径
        String targetFilePath = "D:\\test\\test.pdf";
        // 调用 LibreOfficeUtil 的 officeConvert 方法进行转换
        boolean result = LibreOfficeUtil.officeConvert(sourceFilePath, targetFilePath);
        System.out.println("转换结果：" + result);
    }
}
