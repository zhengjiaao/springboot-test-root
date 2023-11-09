/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-08 13:14
 * @Since:
 */
package com.zja.util;

import org.junit.jupiter.api.Test;

/**
 * @author: zhengja
 * @since: 2023/11/08 13:14
 */
public class OCRTesseractUtilTest {

    @Test
    public void testOcrImage() {
//        String inputImagePath = "D:\\temp\\ocr\\input.png";
//        String inputImagePath = "D:\\temp\\ocr\\input-2.png";
        String inputImagePath = "D:\\temp\\ocr\\input-3.png";

        String result = OCRTesseractUtil.ocrImage(inputImagePath);
        System.out.println(result);
    }

}
