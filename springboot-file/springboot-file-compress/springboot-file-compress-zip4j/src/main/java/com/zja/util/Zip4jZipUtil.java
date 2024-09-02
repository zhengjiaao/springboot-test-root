package com.zja.util;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @Author: zhengja
 * @Date: 2024-08-09 9:48
 */
public class Zip4jZipUtil {

    private Zip4jZipUtil() {
    }

    public static boolean isZipFile(String filePath) {
        return filePath.endsWith(".zip");
    }

    public static void main(String[] args) throws ZipException {
        ZipFile zipFile = new ZipFile("D:\\temp\\zip\\建筑方案比选\\方案1.zip");
        zipFile.setCharset(Charset.forName("GBK"));
        List<FileHeader> fileHeaders = zipFile.getFileHeaders();
        for (FileHeader fileHeader : fileHeaders) {
            System.out.println(fileHeader.getFileName());
        }

        boolean exists = exists(zipFile, "建筑方案.zip"); // todo 注意，存在编码问题
        System.out.println(exists);
    }

    /**
     * 判断文件是否在压缩包中
     * @param zipFile zip包
     * @param fileName 文件名，包含路径
     */
    public static boolean exists(ZipFile zipFile, String fileName) throws ZipException {
        return zipFile.getFileHeaders().stream().anyMatch(header ->
                header.getFileName().equals(fileName)
        );
    }

    /**
     * 获取压缩包中指定扩展名的文件数量
     * @param zipFile zip包
     * @param extensionName 待检查的文件扩展名, 如：.txt
     */
    public static long existsSpecificExtension(ZipFile zipFile, String extensionName) throws ZipException {
        return zipFile.getFileHeaders().stream().filter(header -> header.getFileName().endsWith(extensionName)).count();
    }

    /**
     * 获取压缩包中指定扩展名的文件
     * @param zipFile zip包
     * @param extensionName 待检查的文件扩展名, 如：.txt
     */
    public static Optional<FileHeader> getFileHeader(ZipFile zipFile, String extensionName) throws ZipException {
        return zipFile.getFileHeaders().stream().filter(header -> header.getFileName().endsWith(extensionName)).findFirst();
    }

    /**
     * 获取压缩包中指定文件夹下的所有文件的FileHeader列表。
     *
     * @param zipFile zip包
     * @param folderName 文件夹名称
     * @return 包含指定文件夹下所有文件的FileHeader列表
     * @throws ZipException 如果处理ZIP文件时出现错误
     */
    public static List<FileHeader> getFolderFileHeaders(ZipFile zipFile, String folderName) throws ZipException {
        return zipFile.getFileHeaders().stream().filter(header -> header.getFileName().startsWith(folderName + "/")).collect(Collectors.toList());
    }

}
