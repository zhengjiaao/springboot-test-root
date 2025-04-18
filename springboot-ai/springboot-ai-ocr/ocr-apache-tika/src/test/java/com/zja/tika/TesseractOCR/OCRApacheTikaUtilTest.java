/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-08 13:14
 * @Since:
 */
package com.zja.tika.TesseractOCR;

import com.zja.tika.OCRApacheTikaUtil;
import org.junit.jupiter.api.Test;

/**
 * @author: zhengja
 * @since: 2023/11/08 13:14
 */
public class OCRApacheTikaUtilTest {

    @Test
    public void testOcrImage() {
        String filePath = "D:\\temp\\ocr\\input.pdf";
//        String filePath = "D:\\temp\\ocr\\input.html";
//        String filePath = "D:\\temp\\ocr\\input.docx";
//        String filePath = "D:\\temp\\ocr\\input.png";

        String content = OCRApacheTikaUtil.autoExtractedContent(filePath);
        System.out.println(content);
    }
}
