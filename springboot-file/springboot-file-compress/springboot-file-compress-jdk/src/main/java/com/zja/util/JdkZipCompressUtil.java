/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-11 10:30
 * @Since:
 */
package com.zja.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Jdk 操作 zip 文件/文件夹
 * java 提供：ZipOutputStream(压缩) 和 ZipInputStream(解压)
 */
public class JdkZipCompressUtil {

    private static final Logger log = LoggerFactory.getLogger(JdkZipCompressUtil.class);

    private static final int BYTE_BUFFER = 5 * 1024;

    private JdkZipCompressUtil() {
    }

    /**
     * 压缩文件/文件夹
     *
     * @param zipFilePath zip路径          例：C:\temp\test.zip
     * @param filePath    文件/文件夹路径   例：C:\temp\test or 例：C:\temp\a.txt
     */
    public static void zip(String filePath, String zipFilePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException("source file or directory " + filePath + " does not exist.");
        }

        if (file.isDirectory()) {
            zipFolder(zipFilePath, filePath);
        } else {
            zipFile(zipFilePath, filePath);
        }
    }

    /**
     * 解压zip
     *
     * @param zipFilePath    zip路径          例：C:\temp\test.zip
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
                byte[] buf = new byte[BYTE_BUFFER];
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
     *
     * @param zipFilePath zip路径   例：C:\temp\a.zip
     * @param srcFilePath 文件路径   例：C:\temp\a.txt
     */
    private static void zipFile(String zipFilePath, String srcFilePath) throws IOException {
        File file = new File(srcFilePath);
        if (!file.exists()) {
            throw new RuntimeException("source file " + srcFilePath + " does not exist.");
        }

        ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(Paths.get(zipFilePath)));
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
     *
     * @param zipFilePath   zip路径      例：C:\temp\test.zip
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

        ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(Paths.get(zipFilePath)));

        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                // 包含根节点目录
                // zipFolder(zos, f, file.getName());
                // 不包含根节点目录
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
                // 处理 空文件夹
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
            byte[] buffer = new byte[BYTE_BUFFER];
            while ((len = zis.read(buffer)) != -1) {
                zos.write(buffer, 0, len);
            }
            zos.closeEntry();
            zis.close();
        }
    }

    public static List<ZipEntry> listZipEntries(String zipFilePath) {
        List<ZipEntry> zipEntries = new ArrayList<>();
        try (ZipFile zipFile = new ZipFile(zipFilePath)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String entryName = entry.getName();
                long entrySize = entry.getSize();
                log.info("Entry Name: {} , Entry Size: {}", entryName, entrySize);
                // 还可以获取更多条目信息，例如压缩大小、修改时间等
                // 如果需要读取条目内容，可以使用zipFile.getInputStream(entry)获取输入流
                zipEntries.add(entry);
            }
            return zipEntries;
        } catch (IOException e) {
            throw new RuntimeException("获取zip条目列表失败：" + e);
        }
    }

    public static void main(String[] args) throws IOException {
        // 压缩文件
        // JdkZipCompressUtil.zipFile("D:\\temp\\zip\\存储目录\\test.zip", "D:\\temp\\zip\\测试文件.txt");
        // JdkZipCompressUtil.unzip("D:\\temp\\zip\\存储目录\\test.zip", "D:\\temp\\zip\\存储目录");

        // 压缩文件夹
//        JdkZipCompressUtil.zipFolder("D:\\temp\\zip\\存储目录\\test.zip", "D:\\temp\\zip\\测试目录");
//        JdkZipCompressUtil.unzip("D:\\temp\\zip\\存储目录\\test.zip", "D:\\temp\\zip\\存储目录");

        //获取zip条目
        List<ZipEntry> zipEntryList = listZipEntries("D:\\temp\\zip\\存储目录\\test.zip");
        for (ZipEntry zipEntry : zipEntryList) {
            System.out.println(zipEntry.getName());
        }
    }

}
