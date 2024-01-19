package com.zja.jacob.util;

import org.junit.jupiter.api.Test;

/**
 * @author: zhengja
 * @since: 2024/01/19 15:07
 */
public class JacobOfficeToPDFUtilTest {

    private static final String input_docx = "D:\\temp\\word\\test.docx";
    private static final String output_pdf = "D:\\temp\\word\\test.docx.pdf";

    @Test
    public void test() {
        JacobOfficeToPDFUtil.officeToPDF(input_docx,output_pdf);
    }


}
