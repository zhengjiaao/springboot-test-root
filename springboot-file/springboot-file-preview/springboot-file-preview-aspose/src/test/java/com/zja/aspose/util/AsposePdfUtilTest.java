package com.zja.aspose.util;

import com.zja.util.AsposePdfUtil;
import org.junit.jupiter.api.Test;

/**
 * @author: zhengja
 * @since: 2024/01/18 17:27
 */
public class AsposePdfUtilTest {

    @Test
    public void officeToPdf() throws Exception {
        AsposePdfUtil.officeToPdf("D:\\temp\\word\\test.docx","D:\\temp\\word\\test.docx.pdf");
    }

}
