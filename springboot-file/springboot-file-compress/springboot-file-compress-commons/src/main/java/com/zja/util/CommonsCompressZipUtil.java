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
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;

/**
 * Zip 加压缩工具
 * apache.commons-compress
 *
 * @author: zhengja
 * @since: 2023/03/14 10:15
 */
public class CommonsCompressZipUtil {

    private static final Logger log = LoggerFactory.getLogger(CommonsCompressZipUtil.class);

    private static final String CHARSET_GBK = "GBK";
    private static final String CHARSET_UTF8 = "UTF8";


    /**
     * 压缩文件/目录
     * <p>注：空目录会被压缩</p>
     * <p>注：路径模式，默认是跟随服务器主机，设置路径模式为Windows，即使用反斜杠（\）;设置路径模式为Unix，即使用正斜杠（/）</p>
     *
     * @param filePath 文件或目录 如：/root/test or /root/test.txt
     * @param zipPath  zip文件 如：/root/test.zip or /root/test.txt.zip
     */
    public static void zip(String filePath, String zipPath) {
        long startTime = System.currentTimeMillis();
        try (OutputStream fos = Files.newOutputStream(Paths.get(zipPath));
             OutputStream bos = new BufferedOutputStream(fos);
             ArchiveOutputStream aos = new ZipArchiveOutputStream(bos)) {
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
                    ArchiveEntry entry = new ZipArchiveEntry(file.toFile(), dirPath.relativize(file).toString());
                    aos.putArchiveEntry(entry);
                    IOUtils.copy(Files.newInputStream(file.toFile().toPath()), aos);
                    aos.closeArchiveEntry();
                    return super.visitFile(file, attrs);
                }
            });
        } catch (IOException e) {
            log.error("zip fail，reason：" + e.getMessage());
            throw new RuntimeException("zip fail，reason：" + e.getMessage());
        }
        log.info("zip success，time：" + (System.currentTimeMillis() - startTime) + " ms");
    }

    /**
     * 解压 zip
     * <p>注：空目录也会被解压   </p>
     * <p>注：默认解压编码：UTF8 </p>
     *
     * @param zipPath   /root/test.zip
     * @param outputDir /tmp/output
     */
    public static void unzip(String zipPath, String outputDir) {
        try {
            log.warn("正在尝试以[UTF8]编码解压,zipPath={}", zipPath);
            unzip(zipPath, outputDir, CHARSET_UTF8);
        } catch (Exception e) {
            log.warn("正在尝试以[GBK]编码解压,zipPath={}", zipPath);
            unzip(zipPath, outputDir, CHARSET_GBK);
        }
        log.info("成功解压,zipPath={},outputDir={}", zipPath, outputDir);
    }

    /**
     * 解压 zip
     * <p>注：空目录也会被解压</p>
     *
     * @param zipPath   /root/test.zip
     * @param outputDir /tmp/output
     * @param encoding  编码：UTF8、GBK 等
     */
    public static void unzip(String zipPath, String outputDir, String encoding) {
        long startTime = System.currentTimeMillis();
        try (InputStream fis = Files.newInputStream(Paths.get(zipPath)); InputStream bis = new BufferedInputStream(fis); ArchiveInputStream ais = new ZipArchiveInputStream(bis, encoding)) {
            unzipArchive(outputDir, ais);
        } catch (IOException e) {
            log.error("unzip fail，reason：" + e.getMessage());
            throw new RuntimeException("unzip fail，reason：" + e.getMessage());
        }
        log.info("unzip success，time：" + (System.currentTimeMillis() - startTime) + " ms");
    }

    /**
     * 解压 zip
     * <p>注：空目录也会被解压   </p>
     * <p>注：自动动态切换解压编码：UTF8、GBK </p>
     *
     * @param inputStream 流
     * @param outputDir   /tmp/output
     */
    public static void unzip(InputStream inputStream, String outputDir) {
        try {
            log.warn("正在尝试以[UTF8]编码解压,outputDir={}", outputDir);
            unzip(inputStream, outputDir, CHARSET_UTF8);
        } catch (Exception e) {
            log.warn("正在尝试以[GBK]编码解压,outputDir={}", outputDir);
            unzip(inputStream, outputDir, CHARSET_GBK);
        }
        log.info("成功解压,outputDir={}", outputDir);
    }

    /**
     * 解压 zip
     * <p>注：空目录也会被解压</p>
     *
     * @param inputStream 流
     * @param outputDir   /tmp/output
     */
    public static void unzip(InputStream inputStream, String outputDir, String encoding) {
        long startTime = System.currentTimeMillis();
        try (InputStream bis = new BufferedInputStream(inputStream); ArchiveInputStream ais = new ZipArchiveInputStream(bis, encoding)) {
            unzipArchive(outputDir, ais);
        } catch (IOException e) {
            log.error("zip fail，reason：" + e.getMessage());
            throw new RuntimeException("unzip fail，reason：" + e.getMessage());
        }
        log.info("unzip success，time：" + (System.currentTimeMillis() - startTime) + " ms");
    }

    private static void unzipArchive(String outputDir, ArchiveInputStream ais) throws IOException {
        ArchiveEntry entry;
        while (Objects.nonNull(entry = ais.getNextEntry())) {
            if (!ais.canReadEntryData(entry)) {
                continue;
            }
            String filePath = outputDir + File.separator + entry.getName();
            File outputFile = new File(filePath);
            if (entry.isDirectory()) {
                if (!outputFile.isDirectory() && !outputFile.mkdirs()) {
                    outputFile.mkdirs();
                }
            } else {
                File parent = outputFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("failed to create directory " + parent);
                }
                try (OutputStream o = Files.newOutputStream(outputFile.toPath())) {
                    IOUtils.copy(ais, o);
                }
            }
        }
    }

    public static List<ZipArchiveEntry> listZipEntries(String zipFilePath) throws IOException {
        List<ZipArchiveEntry> zipEntries = new ArrayList<>();
        try (ZipFile zipFile = new ZipFile(zipFilePath)) {
            Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                String entryName = entry.getName();
                long entrySize = entry.getSize();
                // boolean directory = entry.isDirectory(); // 是目录，否则是文件
                // 还可以获取更多条目信息，例如压缩大小、修改时间等
                log.info("Entry Name: {} , Entry Size: {}", entryName, entrySize);
                zipEntries.add(entry);
            }
            return zipEntries;
        }
    }

    public static void main(String[] args) throws IOException {

        // 压缩目录
//        CommonsCompressZipUtil.zip("D:\\temp\\zip\\测试目录", "D:\\temp\\zip\\测试目录.zip");
//        CommonsCompressZipUtil.unzip("D:\\temp\\zip\\测试目录.zip", "D:\\temp\\zip\\测试目录zip");

        // 压缩文件
        // CommonsCompressZipUtil.zip("D:\\temp\\zip\\测试目录2\\新建文本文档.txt", "D:\\temp\\zip\\测试目录2\\新建文本文档.txt.zip");
        // CommonsCompressZipUtil.unzip("D:\\temp\\zip\\测试目录2\\新建文本文档.txt.zip", "D:\\temp\\zip\\测试目录2\\新建文本文档2.txt");

        // 获取zip条目
        List<ZipArchiveEntry> zipEntryList = listZipEntries("D:\\temp\\zip\\存储目录\\test.zip");
        for (ZipEntry zipEntry : zipEntryList) {
            System.out.println(zipEntry.getName());
        }
    }

}
