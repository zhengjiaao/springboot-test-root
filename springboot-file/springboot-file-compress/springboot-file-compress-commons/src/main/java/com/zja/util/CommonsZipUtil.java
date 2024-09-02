package com.zja.util;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * zip 工具包
 * @Author: zhengja
 * @Date: 2024-08-09 14:56
 */
public class CommonsZipUtil {

    private static final Logger log = LoggerFactory.getLogger(CommonsZipUtil.class);

    private CommonsZipUtil() {
    }

    public static void main(String[] args) throws IOException {
        String zipFilePath = "D:\\temp\\zip\\测试目录.zip";

        String saveFilePath = "D:\\temp\\zip\\二级文件.txt";
        String entryName = "一级目录-非空/二级文件.txt"; // "一级目录-非空/二级文件.txt"
        //
        // try (ZipFile zipFile = new ZipFile(zipFilePath)) {
        //     ZipArchiveEntry entry = zipFile.getEntry(entryName);
        //     if (entry != null) {
        //         InputStream inputStream = zipFile.getInputStream(entry);
        //         if (inputStream != null) {
        //             log.info("文件大小：{}", inputStream.read());
        //         }
        //         // 保存到本地
        //         FileUtils.copyInputStreamToFile(inputStream, new File(saveFilePath));
        //         log.info("文件保存成功！");
        //     } else {
        //         throw new RuntimeException("Entry not found in ZIP file: " + entryName);
        //     }
        // }

        // boolean exists = existsInZipFile(zipFilePath, "一级文件.txt", false, true);
        // System.out.println(exists);

        // InputStream inputStream = getInputStream(zipFilePath, "一级文件.txt");
        // System.out.println(inputStream.available());

        // List<ZipArchiveEntry> zipArchiveEntries = listSubsetLevelZipEntries(zipFilePath, 2);
        // zipArchiveEntries.forEach(System.out::println);

        // List<ZipArchiveEntry> zipArchiveEntries = listZipEntriesByDirEntryName(zipFilePath, "一级目录-非空/二级目录-非空/");
        // zipArchiveEntries.forEach(System.out::println);

        // List<ZipArchiveEntry> zipArchiveEntries = listFirstLevelEntriesByDirEntryName(zipFilePath, "一级目录-非空/二级目录-非空");
        // zipArchiveEntries.forEach(System.out::println);

        // boolean exists = existsInZipFile(zipFilePath, "一级目录-非空/二级目录-非空/");
        // System.out.println(exists);

        // boolean notExists = notExistsInZipFile(zipFilePath, "一级目录-非空/二级目录-非空/三级文件.txt");
        // System.out.println(notExists);

        // boolean exists = existsEndsWith(zipFilePath, ".png", 2);
        // System.out.println(exists);

        // boolean notExists = notExistsEndsWith(zipFilePath, ".png", 3);
        // System.out.println(notExists);

        // try (InputStream inputStream = getInputStream(zipFilePath, entryName)) {
        //     if (inputStream != null) {
        //         int data = inputStream.read();
        //         log.info("读取结果: {}", data);
        //     }
        // } catch (IOException e) {
        //     log.error("读取文件时发生错误", e);
        // }

    }

    /**
     * 判断文件是否为ZIP文件。
     * @param zipFilePath ZIP文件的路径
     * @return true 如果是ZIP文件，否则为false
     */
    public static boolean isZipFile(String zipFilePath) {
        return zipFilePath.toLowerCase().endsWith(".zip");
    }

    /**
     * 列出ZIP文件的所有条目。
     *
     * @param zipFilePath ZIP文件的路径。
     * @return 包含所有条目的列表。
     */
    public static List<ZipArchiveEntry> listZipEntries(String zipFilePath) throws IOException {
        return listZipEntries(zipFilePath, 0);
    }

    /**
     * 列出指定层级的所有zip条目
     * @param zipFilePath ZIP文件的路径
     * @param level 0 为全部层级，1 为第一层级，2 为第二层级，以此类
     * @return 包含指定层级的条目的列表
     * @throws IOException 读取ZIP文件时发生错误
     */
    public static List<ZipArchiveEntry> listZipEntries(String zipFilePath, int level) throws IOException {
        List<ZipArchiveEntry> zipEntries = new ArrayList<>();
        Enumeration<? extends ZipArchiveEntry> entries = getZipEntries(zipFilePath);

        while (entries.hasMoreElements()) {
            ZipArchiveEntry entry = entries.nextElement();
            String entryName = entry.getName();

            // 如果是根目录或符合层级要求，则添加到结果列表中
            if (isEntryAtLevel(entryName, level)) {
                zipEntries.add(entry);
            }
        }

        return zipEntries;
    }

    private static Enumeration<? extends ZipArchiveEntry> getZipEntries(String zipFilePath) throws IOException {
        try (ZipFile zipFile = new ZipFile(zipFilePath)) {
            return zipFile.getEntries();
        }
    }

    private static boolean isEntryAtLevel(String entryName, int level) {
        String[] pathComponents = entryName.split("/");
        int pathLength = pathComponents.length;

        // 根据层级判断
        if (level == 0) {
            return true; // 全部层级
        } else if (level == 1) {
            return pathLength == 1; // 第一层级
        } else if (level > 1) {
            return pathLength <= level; // 指定层级
        } else {
            return false;
        }
    }

    /**
     * 列出指定目录下指定的子集条目
     *
     * @param zipFilePath ZIP文件的路径。
     * @param dirEntryName     目录名称。示例："folder"、"folder/"、"folder/subfolder/" 等。
     * @return 包含指定层级子集条目的列表。
     * @throws IOException 读取ZIP文件时发生错误。
     */
    public static List<ZipArchiveEntry> listZipEntriesByDirEntryName(String zipFilePath, String dirEntryName) throws IOException {
        if (!dirEntryName.endsWith("/")) {
            dirEntryName += "/";
        }

        List<ZipArchiveEntry> zipEntries = new ArrayList<>();
        Enumeration<? extends ZipArchiveEntry> entries = getZipEntries(zipFilePath);

        while (entries.hasMoreElements()) {
            ZipArchiveEntry entry = entries.nextElement();
            String entryName = entry.getName();

            if (entryName.startsWith(dirEntryName) && !entryName.equals(dirEntryName)) {
                zipEntries.add(entry);
            }
        }

        return zipEntries;
    }

    /**
     * 列出指定目录下的第一级条目列表。
     *
     * @param zipFilePath ZIP文件的路径。
     * @param dirEntryName 目录名称。示例："folder"、"folder/"、"folder/subfolder/" 等。
     * @return 包含指定目录下第一级条目的列表。
     * @throws IOException 读取ZIP文件时发生错误。
     */
    public static List<ZipArchiveEntry> listFirstLevelEntriesByDirEntryName(String zipFilePath, String dirEntryName) throws IOException {
        if (!dirEntryName.endsWith("/")) {
            dirEntryName += "/";
        }

        int countSlashes = countSlashes(dirEntryName);

        List<ZipArchiveEntry> zipEntries = new ArrayList<>();
        Enumeration<? extends ZipArchiveEntry> entries = getZipEntries(zipFilePath);

        while (entries.hasMoreElements()) {
            ZipArchiveEntry entry = entries.nextElement();
            String entryName = entry.getName();

            if (entryName.startsWith(dirEntryName) && !entryName.equals(dirEntryName)) {
                if (countSlashes(entryName) == countSlashes || countSlashes(entryName) == countSlashes + 1) {
                    zipEntries.add(entry);
                }
            }
        }

        return zipEntries;
    }

    private static int countSlashes(String entryName) {
        String[] parts = entryName.split("/");
        return parts.length - 1;
    }

    /**
     * 获取ZIP文件中的条目。
     * @param zipFilePath ZIP文件的路径
     * @param entryName 要查找的条目名称，例如："一级目录/"、"一级目录/文件.txt"、"一级目录/二级目录/文件.txt"
     * @return ZipArchiveEntry 如果找到，则返回条目；否则返回null
     * @throws IOException 读取ZIP文件时发生错误
     */
    public static ZipArchiveEntry getZipArchiveEntryByName(String zipFilePath, String entryName) throws IOException {
        Enumeration<? extends ZipArchiveEntry> entries = getZipEntries(zipFilePath);

        while (entries.hasMoreElements()) {
            ZipArchiveEntry entry = entries.nextElement();
            if (entry.getName().equals(entryName)) {
                return entry;
            }
        }

        return null;
    }

    /**
     * 判断文件是否在压缩包中。
     *
     * @param zipFilePath ZIP文件的路径。
     * @param entryName 要查找的条目名称，例如："一级目录/"、"一级目录/文件.txt"、"一级目录/二级目录/文件.txt"
     * @return 如果存在则返回true，否则返回false。
     */
    public static boolean existsInZipFile(String zipFilePath, String entryName) throws IOException {
        return getZipArchiveEntryByName(zipFilePath, entryName) != null;
    }

    public static boolean notExistsInZipFile(String zipFilePath, String entryName) throws IOException {
        return !existsInZipFile(zipFilePath, entryName);
    }

    /**
     * 判断文件是否在压缩包中
     * @param zipFilePath ZIP文件的路径
     * @param entryNameEndsWith 文件后缀，例如：".txt"、".jpg"
     * @param level 0 为全部层级，1 为第一层级，2 为第二层级，以此类
     * @return 如果存在则返回true，否则返回false。
     */
    public static boolean existsEndsWith(String zipFilePath, String entryNameEndsWith, int level) throws IOException {
        List<ZipArchiveEntry> zipEntries = listZipEntries(zipFilePath, level);
        for (ZipArchiveEntry entry : zipEntries) {
            if (entry.getName().endsWith(entryNameEndsWith)) {
                return true;
            }
        }
        return false;
    }

    public static boolean notExistsEndsWith(String zipFilePath, String entryNameEndsWith, int level) throws IOException {
        return !existsEndsWith(zipFilePath, entryNameEndsWith, level);
    }

    /**
     * 获取指定文件流
     * @param zipFilePath ZIP文件的路径
     * @param entryName 文件路径，示例：text.txt、test/text.txt
     * @return InputStream 注意 “关闭流”
     */
    public static InputStream getInputStream(String zipFilePath, String entryName) throws IOException {
        ZipArchiveEntry zipArchiveEntry = getZipArchiveEntryByName(zipFilePath, entryName);
        if (zipArchiveEntry == null) {
            throw new RuntimeException("Entry not found in ZIP file: " + entryName);
        }
        if (zipArchiveEntry.isDirectory()) {
            throw new RuntimeException("Entry is a directory: " + entryName);
        }

        return new ZipFile(zipFilePath).getInputStream(zipArchiveEntry);
    }

}
