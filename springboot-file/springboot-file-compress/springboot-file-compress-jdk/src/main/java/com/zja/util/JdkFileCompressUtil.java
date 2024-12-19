package com.zja.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.*;

/**
 * 文件解压缩工具类
 * <p>
 * Description: JDK自带的压缩工具类，解决类似存储本地大JSON等文件压缩解压的问题，缩小文件存储的大小，并加快读取文件。
 * 场景：例如：存储用户上传的JSON数据到数据库之前进行压缩，读取时再解压缩等。
 *
 * @Author: zhengja
 * @Date: 2024-12-19 16:42
 */
public class JdkFileCompressUtil {

    public static void compressFile(String sourceFilePath, String destFilePath) {
        try (FileInputStream fis = new FileInputStream(sourceFilePath); FileOutputStream fos = new FileOutputStream(destFilePath); GZIPOutputStream gzos = new GZIPOutputStream(fos)) {

            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                gzos.write(buffer, 0, len);
            }
        } catch (IOException e) {
            throw new RuntimeException("压缩文件失败", e);
        }
    }

    public static void decompressFile(String sourceFilePath, String destFilePath) {
        try (FileInputStream fis = new FileInputStream(sourceFilePath); GZIPInputStream gzis = new GZIPInputStream(fis); FileOutputStream fos = new FileOutputStream(destFilePath)) {

            byte[] buffer = new byte[1024];
            int len;
            while ((len = gzis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
        } catch (IOException e) {
            throw new RuntimeException("解压文件失败", e);
        }
    }

    public static void main(String[] args) {
        // String sourceFilePath = "source.txt";
        String sourceFilePath = "D:\\temp\\json\\test.json";
        String compressFilePath = "D:\\temp\\json\\compressed.gz";
        String decompressFilePath = "D:\\temp\\json\\decompressed.json";

        compressFile(sourceFilePath, compressFilePath);
        System.out.println("File compressed successfully.");

        decompressFile(compressFilePath, decompressFilePath);
        System.out.println("File decompressed successfully.");
    }
}
