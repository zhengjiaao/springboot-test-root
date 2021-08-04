package com.zja.utils;

import com.zja.BaseTests;
import org.junit.Test;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-08-02 15:04
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
public class DocConversionPreviewUtilTests extends BaseTests {

    @Resource
    private DocConversionPreviewUtil docConversionPreviewUtil;

    @Test
    public void test() throws IOException {
        String pdfPreviewURL = docConversionPreviewUtil.getPdfPreviewURL("D:\\Temp\\excel\\sample.xlsx");
        printlnAnyObj(pdfPreviewURL);
    }

    @Test
    public void test2() throws IOException {
        String pdfPath = docConversionPreviewUtil.toPdf("D:\\Temp\\excel\\sample.xlsx");
        printlnAnyObj(pdfPath);
    }

    @Test
    public void test3() throws IOException {
        String pdfPath = docConversionPreviewUtil.toPdf("D:\\Temp\\excel\\sample.xlsx","D:\\Temp\\pdf\\sample.pdf");
        printlnAnyObj(pdfPath);
    }

}
