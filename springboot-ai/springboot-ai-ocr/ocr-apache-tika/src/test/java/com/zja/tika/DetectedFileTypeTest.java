package com.zja.tika;

import org.apache.tika.Tika;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.junit.jupiter.api.Test;

/**
 * 检测文件类型: MIME类型与扩展名
 * @Author: zhengja
 * @Date: 2025-05-09 16:31
 */
public class DetectedFileTypeTest {

    @Test
    public void testDetectedFileType() {
        // String filePath = "src/test/resources/file/test.txt";
        String filePath = "D:\\temp\\txt\\test.txt";

        Tika tika = new Tika();
        String detectedType = tika.detect(filePath); // 直接传入文件路径进行检测
        System.out.println("文件类型：" + detectedType); // 输出：text/plain
    }

    @Test
    public void testGetFileExtension() throws MimeTypeException {
        String mimeType = "application/pdf";
        String fileExtension = getFileExtension(mimeType);
        System.out.println("文件扩展名：" + fileExtension); // 输出：.pdf
    }

    public String getFileExtension(String mimeType) throws MimeTypeException {
        return MimeTypes.getDefaultMimeTypes().forName(mimeType).getExtension();
    }
}
