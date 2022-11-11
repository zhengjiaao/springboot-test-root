/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-11 10:30
 * @Since:
 */
package com.zja.util;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.*;

/**
 * Jdk 操作 zip 文件/文件夹
 * java 提供：ZipOutputStream(压缩) 和 ZipInputStream(解压)
 */
public class JdkZipCompressUtil {

    /**
     * 压缩文件/文件夹
     * @param zipFilePath    zip路径          例：C:\temp\test.zip
     * @param srcPath       文件/文件夹路径   例：C:\temp\test or 例：C:\temp\a.txt
     */
    public static void zip(String zipFilePath, String srcPath) throws IOException {
        File file = new File(srcPath);
        if (!file.exists()) {
            throw new RuntimeException("source file or directory " + srcPath + " does not exist.");
        }

        if (file.isDirectory()) {
            zipFolder(zipFilePath, srcPath);
        } else {
            zipFile(zipFilePath, srcPath);
        }
    }

    /**
     * 解压zip
     * @param zipFilePath     zip路径          例：C:\temp\test.zip
     * @param destFolderPath 目标文件夹路径    例：C:\temp\test
     */
    public static void unzip(String zipFilePath, String destFolderPath) throws IOException {
        File file = new File(zipFilePath);
        ZipFile zipFile = new ZipFile(file);
        Enumeration<?> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            if (entry.isDirectory()) {
                new File(destFolderPath + "/" + entry.getName()).mkdirs();
            } else {
                File targetFile = new File(destFolderPath + "/" + entry.getName());
                if (!targetFile.getParentFile().exists()) {
                    targetFile.getParentFile().mkdirs();
                }
                targetFile.createNewFile();
                InputStream is = zipFile.getInputStream(entry);
                FileOutputStream fos = new FileOutputStream(targetFile);
                int len;
                byte[] buf = new byte[5 * 1024];
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                }
                fos.close();
                is.close();
            }
        }
    }

    /**
     * 压缩文件
     * @param zipFilePath    zip路径   例：C:\temp\a.zip
     * @param srcFilePath   文件路径   例：C:\temp\a.txt
     */
    private static void zipFile(String zipFilePath, String srcFilePath) throws IOException {
        File file = new File(srcFilePath);
        if (!file.exists()) {
            throw new RuntimeException("source file " + srcFilePath + " does not exist.");
        }

        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFilePath));
        FileInputStream zis = new FileInputStream(file);
        zos.putNextEntry(new ZipEntry(file.getName()));
        int len;
        byte[] buffer = new byte[5 * 1024];
        while ((len = zis.read(buffer)) != -1) {
            zos.write(buffer, 0, len);
        }
        zis.close();
        zos.closeEntry();
        zos.close();
    }

    /**
     * 压缩文件夹
     * @param zipFilePath    zip路径      例：C:\temp\test.zip
     * @param srcFolderPath 文件夹路径    例：C:\temp\test
     */
    private static void zipFolder(String zipFilePath, String srcFolderPath) throws IOException {
        File file = new File(srcFolderPath);
        if (!file.exists()) {
            throw new RuntimeException("source directory " + srcFolderPath + " does not exist.");
        }
        if (!file.isDirectory()) {
            throw new RuntimeException(srcFolderPath + " not isDirectory.");
        }

        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFilePath));

        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                //包含根节点目录
                //zipFolder(zos, f, file.getName());
                zipFolder(zos, f, "");
            }
        }

        zos.close();
    }

    /**
     * 压缩文件夹
     * 会则把文件夹下的所有子文件及子文件夹(含空文件夹)都压缩
     */
    private static void zipFolder(ZipOutputStream zos, File file, String zipPath) throws IOException {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null || files.length == 0) {
                //处理 空文件夹
                zos.putNextEntry(new ZipEntry(zipPath + "/" + file.getName() + "/"));
                zos.closeEntry();
            } else {
                for (File f : files) {
                    zipFolder(zos, f, zipPath + "/" + file.getName());
                }
            }
        } else {
            FileInputStream zis = new FileInputStream(file);
            zos.putNextEntry(new ZipEntry(zipPath + "/" + file.getName()));
            int len;
            byte[] buffer = new byte[5 * 1024];
            while ((len = zis.read(buffer)) != -1) {
                zos.write(buffer, 0, len);
            }
            zos.closeEntry();
            zis.close();
        }
    }


    public static void main(String[] args) throws IOException {
        //压缩文件
        JdkZipCompressUtil.zipFile("D:\\temp\\zip\\存储目录\\test.zip", "D:\\temp\\zip\\测试文件.txt");
        JdkZipCompressUtil.unzip("D:\\temp\\zip\\存储目录\\test.zip", "D:\\temp\\zip\\存储目录");

        //压缩文件夹
//        JdkZipCompressUtil.zipFolder("D:\\temp\\zip\\存储目录\\test.zip", "D:\\temp\\zip\\测试目录");
//        JdkZipCompressUtil.unzip("D:\\temp\\zip\\存储目录\\test.zip", "D:\\temp\\zip\\存储目录");
    }

}
