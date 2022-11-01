package com.zja.util.file;

import java.io.File;

/**
 * 文件工具
 */
public class FileUtil {

    /**
     * 删除文件
     *
     * @param filePath
     */
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 获取文件名称（包括文件的后缀）
     * @param fileUrl  文件路径
     * @return
     */
    public static String getFileName(String fileUrl){
        int splitIndex = fileUrl.lastIndexOf('/');
        return fileUrl.substring(splitIndex + 1);
    }
}
