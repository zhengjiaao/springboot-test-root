/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-02 15:26
 * @Since:
 */
package com.zja.tika;

import org.junit.Test;

/**
 * @author: zhengja
 * @since: 2023/11/02 15:26
 */
public class HtmlContentExtractor {

    @Test
    public void testExtractPDF2() throws Exception {
        String extractedContent = ApacheTikaUtil.extractedContent("D:\\temp\\ocr\\input.html");
        System.out.println(extractedContent);
    }
}
