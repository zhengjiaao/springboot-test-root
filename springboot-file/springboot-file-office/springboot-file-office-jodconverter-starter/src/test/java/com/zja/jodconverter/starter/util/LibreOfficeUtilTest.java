package com.zja.jodconverter.starter.util;

import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * @Author: zhengja
 * @Date: 2025-04-18 16:38
 */
public class LibreOfficeUtilTest {

    @Test
    public void test() {
        File sourceFile = new File("D:\\temp\\word\\test.docx");
        File targetFile = new File("D:\\temp\\word\\test.docx.pdf");
        boolean result = LibreOfficeUtil.officeConvert(sourceFile, targetFile);
        System.out.println("转换结果：" + result);
    }
}
