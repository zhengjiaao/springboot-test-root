package com.zja.preview.libreoffice.util;

import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * @Author: zhengja
 * @Date: 2025-04-18 14:13
 */
public class LibreOfficeUtilTest {

    @Test
    public void officeConvert_1() {
        File sourceFile = new File("D:\\temp\\word\\test.docx");
        File targetFile = new File("D:\\temp\\word\\test.docx.pdf");
        boolean convert = LibreOfficeUtil.officeConvert(sourceFile, targetFile);
        System.out.println("convert = " + convert);
    }

    @Test
    public void officeConvert_2() {
        File sourceFile = new File("D:\\temp\\word\\test.doc");
        File targetFile = new File("D:\\temp\\word\\to\\test.docx");
        boolean convert = LibreOfficeUtil.officeConvert(sourceFile, targetFile);
        System.out.println("convert = " + convert);
    }

    @Test
    public void officeConvert_3() {
        // 多个Sheet1，分页展示
        File sourceFile = new File("D:\\temp\\excel\\input.xlsx");
        File targetFile = new File("D:\\temp\\excel\\input.xlsx.pdf");

        // todo 超宽的Sheet页，转换pdf后会被分割多页展示，效果不好
        // File sourceFile = new File("D:\\temp\\excel\\sample.xlsx");
        // File targetFile = new File("D:\\temp\\excel\\sample.xlsx.pdf");

        boolean convert = LibreOfficeUtil.officeConvert(sourceFile, targetFile);
        System.out.println("convert = " + convert);
    }

    @Test
    public void officeConvert_4() {
        File sourceFile = new File("D:\\temp\\ppt\\input.pptx");
        File targetFile = new File("D:\\temp\\ppt\\input.pptx.pdf");
        boolean convert = LibreOfficeUtil.officeConvert(sourceFile, targetFile);
        System.out.println("convert = " + convert);
    }
}
