package com.zja.java.util;

/**
 * 文件名工具类
 * @Author: zhengja
 * @Date: 2024-09-02 15:20
 */
public class FileNameUtil {

    private FileNameUtil() {

    }

    /**
     * 获取文件名(包含扩展名)
     * @param filePath 文件路径，如：/Users/zhengja/Documents/test.txt
     * @return test.txt
     */
    public static String getFileName(String filePath) {
        filePath = filePath.replace("\\", "/");
        return filePath.substring(filePath.lastIndexOf("/") + 1);
    }

    /**
     * 获取文件名(不包含扩展名)
     * @param filePath 文件路径，如：/Users/zhengja/Documents/test.txt
     * @return test
     */
    public static String getFileNameWithoutExtension(String filePath) {
        return getFileName(filePath).replace(getFileExtension(filePath, true), "");
    }

    /**
     * 移除文件扩展名
     * @param filePath 文件路径，如：/Users/zhengja/Documents/test.txt
     * @return test
     */
    public static String removeExtension(String filePath) {
        if (filePath == null) {
            return null;
        } else {
            String fileExtension = getFileExtension(filePath, true);
            return filePath.substring(0, filePath.length() - fileExtension.length());
        }
    }

    /**
     * 获取文件后缀，不包含.
     * @param fileName 文件名，如：test.txt
     * @return txt
     */
    public static String getFileExtension(String fileName) {
        return getFileExtension(fileName, false);
    }

    /**
     * 获取文件名（不包含后缀）和文件后缀
     * @param fileName 文件名，如：test.txt
     * @param includeDot 是否返回带点的扩展名, 如：true返回.txt，false返回txt
     * @return .txt/txt
     */
    public static String getFileExtension(String fileName, boolean includeDot) {
        fileName = getFileName(fileName);

        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            // 如果文件名中没有后缀
            return "";
        }

        return includeDot ? ("." + fileName.substring(lastDotIndex + 1)) : fileName.substring(lastDotIndex + 1);
    }

    /**
     * 获取文件路径，不包含文件名
     * @param filePath 文件路径, 如：/Users/zhengja/Documents/test.txt
     * @return /Users/zhengja/Documents/
     */
    public static String getFilePathWithoutFileName(String filePath) {
        // 查找最后一个斜杠的位置
        int lastSlashIndex = filePath.lastIndexOf("/");

        if (lastSlashIndex != -1) {
            return filePath.substring(0, lastSlashIndex);
        } else {
            int lastBackslashIndex = filePath.lastIndexOf("\\");
            if (lastBackslashIndex != -1) {
                return filePath.substring(0, lastBackslashIndex);
            } else {
                return "";
            }
        }
    }

    public static void main(String[] args) {
        // String filePath = "D:\\temp\\excel\\input";
        // String filePath = "D:\\temp\\excel\\input.xlsx";
        // String filePath = "D:/temp/excel/input.xlsx";
        // String filePath = "input.xlsx";
        String filePath = "input";
        System.out.println(getFileName(filePath));
        System.out.println(getFileNameWithoutExtension(filePath));
        System.out.println(removeExtension(filePath));
        System.out.println(getFileExtension(filePath));
        System.out.println(getFileExtension(filePath, true));
        System.out.println(getFilePathWithoutFileName(filePath));
    }


}
