package com.zja.commonsio;

import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Test;

/**
 * @author: zhengja
 * @since: 2024/01/22 16:39
 */
public class FilenameUtilsExample {

    @Test
    public void test() {
        String filePath = "source.txt";

        // 获取文件名（包括扩展名）
        String fileName = FilenameUtils.getName(filePath);
        System.out.println("文件名：" + fileName);

        // 获取文件扩展名
        String fileExtension = FilenameUtils.getExtension(filePath);
        System.out.println("文件扩展名：" + fileExtension);

        // 构建新的文件名（替换扩展名）
        String newFilePath = FilenameUtils.removeExtension(filePath) + ".bak";
        System.out.println("新文件名：" + newFilePath);
    }
}
