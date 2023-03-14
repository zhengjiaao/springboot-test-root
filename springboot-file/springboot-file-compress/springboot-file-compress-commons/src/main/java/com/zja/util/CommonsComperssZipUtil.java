/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-03-14 10:15
 * @Since:
 */
package com.zja.util;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

/**
 * Zip 加压缩工具
 * apache.commons-compress
 * @author: zhengja
 * @since: 2023/03/14 10:15
 */
public class CommonsComperssZipUtil {

    private static Logger log = LoggerFactory.getLogger(CommonsComperssZipUtil.class);


    /**
     * 压缩文件/目录
     * @param filePath  文件或目录 如：/root/test or /root/test.txt
     * @param zipPath   zip文件 如：/root/test.zip or /root/test.txt.zip
     * 注：空目录会被压缩
     */
    public static void zip(String filePath, String zipPath) {
        long startTime = System.currentTimeMillis();
        try (OutputStream fos = new FileOutputStream(zipPath);
             OutputStream bos = new BufferedOutputStream(fos);
             ArchiveOutputStream aos = new ZipArchiveOutputStream(bos)
        ) {
            Path dirPath = Paths.get(filePath);
            Files.walkFileTree(dirPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    ArchiveEntry entry = new ZipArchiveEntry(dir.toFile(), dirPath.relativize(dir).toString());
                    aos.putArchiveEntry(entry);
                    aos.closeArchiveEntry();
                    return super.preVisitDirectory(dir, attrs);
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    ArchiveEntry entry = new ZipArchiveEntry(
                            file.toFile(), dirPath.relativize(file).toString());
                    aos.putArchiveEntry(entry);
                    IOUtils.copy(new FileInputStream(file.toFile()), aos);
                    aos.closeArchiveEntry();
                    return super.visitFile(file, attrs);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            log.error("zip fail，reason：" + e.getMessage());
        }

        log.info("zip success，time：" + (System.currentTimeMillis() - startTime) + " ms");
    }

    /**
     * 加压 zip
     * @param zipPath   /root/test.zip
     * @param descDir   /tmp/output
     * 注：空目录也会被解压
     */
    public static void unzip(String zipPath, String descDir) {
        long startTime = System.currentTimeMillis();
        try (InputStream fis = Files.newInputStream(Paths.get(zipPath));
             InputStream bis = new BufferedInputStream(fis);
             ArchiveInputStream ais = new ZipArchiveInputStream(bis)
        ) {
            ArchiveEntry entry;
            while (Objects.nonNull(entry = ais.getNextEntry())) {
                if (!ais.canReadEntryData(entry)) {
                    continue;
                }
                String name = descDir + File.separator + entry.getName();
                File f = new File(name);
                if (entry.isDirectory()) {
                    if (!f.isDirectory() && !f.mkdirs()) {
                        f.mkdirs();
                    }
                } else {
                    File parent = f.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs()) {
                        throw new IOException("failed to create directory " + parent);
                    }
                    try (OutputStream o = Files.newOutputStream(f.toPath())) {
                        IOUtils.copy(ais, o);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("unzip fail，reason：" + e.getMessage());
        }

        log.error("unzip success，time：" + (System.currentTimeMillis() - startTime) + " ms");
    }

    public static void main(String[] args) {

        //压缩目录
//        CommonsComperssZipUtil.zip("D:\\temp\\zip\\测试目录", "D:\\temp\\zip\\测试目录.zip");
//        CommonsComperssZipUtil.unzip("D:\\temp\\zip\\测试目录.zip", "D:\\temp\\zip\\测试目录zip");

        //压缩文件
        CommonsComperssZipUtil.zip("D:\\temp\\zip\\测试目录2\\新建文本文档.txt", "D:\\temp\\zip\\测试目录2\\新建文本文档.txt.zip");
        CommonsComperssZipUtil.unzip("D:\\temp\\zip\\测试目录2\\新建文本文档.txt.zip", "D:\\temp\\zip\\测试目录2\\新建文本文档2.txt");
    }

}
