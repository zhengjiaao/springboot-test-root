/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-16 15:52
 * @Since:
 */
package com.zja.util;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;

/**
 * @author: zhengja
 * @since: 2023/11/16 15:52
 */
public class PDFBoxUtil {

    private PDFBoxUtil() {

    }

    public static int getPdfPagesCount(String pdfPath) throws IOException {
        try (PDDocument document = PDDocument.load(new File(pdfPath))) {
            return document.getNumberOfPages();
        }
    }
}
