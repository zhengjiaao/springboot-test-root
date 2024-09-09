package com.zja.asposeutil.util;

import com.dist.zja.aspose.AsposeConvertToPDF;
import com.zja.asposeutil.AsposeUtilApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 *
 * @Author: zhengja
 * @Date: 2024-09-09 15:00
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AsposeUtilApplication.class)
public class AsposeConvertToPDFTest  {

    @Autowired
    private AsposeConvertToPDF asposeConvertToPDF;

    String sourceFilePath = "D:\\temp\\cad\\9.2城北平面(修改学校指标）炸开.dwg";
    String pdfFilePath = "D:\\Temp\\cad\\9.2城北平面(修改学校指标）炸开.pdf";

    @Test
    public void test4() throws IOException {
       asposeConvertToPDF.toPdfByCad(sourceFilePath,pdfFilePath);
    }
}
