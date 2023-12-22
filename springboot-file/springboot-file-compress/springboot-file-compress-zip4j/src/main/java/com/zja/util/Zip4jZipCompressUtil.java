/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-11 10:30
 * @Since:
 */
package com.zja.util;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Zip4j 操作 zip 文件/文件夹
 */
public class Zip4jZipCompressUtil {

    /**
     * 把 文件/文件夹 添加到现有 zip
     *
     * @param zipFilePath zip路径      例：C:\temp\test.zip
     * @param srcPathName 文件/文件夹   例：C:\temp\test or 例：C:\temp\a.txt
     */
    public static void zip(String srcPathName, String zipFilePath) throws ZipException {
        File file = new File(srcPathName);
        if (!file.exists()) {
            throw new RuntimeException("source file or directory " + srcPathName + " does not exist.");
        }

        if (file.isDirectory()) {
            // 压缩目录
            // 若是文件夹，则把文件夹下的所有子文件及子文件夹(含空文件夹)都压缩
            ZipParameters parameters = new ZipParameters();
            parameters.setIncludeRootFolder(false); // 不包含根目录
            new ZipFile(zipFilePath).addFolder(file, parameters);
        } else {
            new ZipFile(zipFilePath).addFile(file);
        }
    }

    /**
     * 加压 从 zip 中提取所有文件
     *
     * @param zipFilePath    zip路径          例：C:\temp\test.zip
     * @param destFolderPath 目标文件夹路径     例：C:\temp\test
     */
    public static void unzip(String zipFilePath, String destFolderPath) throws ZipException {
        new ZipFile(zipFilePath).extractAll(destFolderPath);
    }

    public static List<FileHeader> listZipEntries(String zipFilePath) {
        List<FileHeader> zipEntries = new ArrayList<>();
        try (ZipFile zipFile = new ZipFile(zipFilePath)) {
            List<FileHeader> fileHeaders = zipFile.getFileHeaders();
            zipEntries.addAll(fileHeaders);
            return zipEntries;
        } catch (IOException e) {
            throw new RuntimeException("获取zip条目列表失败：" + e);
        }
    }

    public static void main(String[] args) throws ZipException {

        // 解压文件
//        Zip4jZipCompressUtil.zip("D:\\temp\\zip\\存储目录\\测试文件.zip", "D:\\temp\\zip\\测试文件.txt");
//        Zip4jZipCompressUtil.unzip("D:\\temp\\zip\\存储目录\\测试文件.zip", "D:\\temp\\zip\\存储目录");

        // 解压文件夹
        // Zip4jZipCompressUtil.zip("D:\\temp\\zip\\存储目录\\test.zip", "D:\\temp\\zip\\测试目录");
        // Zip4jZipCompressUtil.unzip("D:\\temp\\zip\\存储目录\\test.zip", "D:\\temp\\zip\\存储目录");

        //获取zip条目
        List<FileHeader> zipEntryList = listZipEntries("D:\\temp\\zip\\存储目录\\test.zip");
        for (FileHeader zipEntry : zipEntryList) {
            System.out.println(zipEntry.getFileName());
        }
    }

}
