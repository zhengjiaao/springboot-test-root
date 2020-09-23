package com.zja.util.zip;

import net.lingala.zip4j.core.ZipFile;
import org.apache.tools.zip.ZipEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

/**
 * zip解压工具
 *
 * @author yinxp@dist.com.cn
 * @date 2018/11/19
 */
public abstract class ZipDeCompressUtil {

    private static Logger log = LoggerFactory.getLogger(ZipDeCompressUtil.class);

    private static final String CHARSET_UTF8 = "utf-8";


    /**
     * zip解压,默认utf-8编码
     *
     * @param srcFilePath 压缩文件源路径
     * @param destDirPath 解压后路径
     */
    public static String deCompress(String srcFilePath, String destDirPath,String charset) throws RuntimeException {
        File srcFile = new File(srcFilePath);
        if (!srcFile.exists()) {
            log.error("压缩文件{}不存在", srcFilePath);
            throw new RuntimeException("压缩文件不存在");
        }
        File destDir = new File(destDirPath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        try {

            ZipFile zFile = new ZipFile(srcFile);
            zFile.setFileNameCharset(charset);
            zFile.extractAll(destDirPath);      // 将文件抽出到解压目录(解压)
        } catch (Exception e) {
            log.error("解压失败",e);
            throw new RuntimeException("解压失败");
        }
        return destDirPath;
    }


    /**
     * zip解压,默认utf-8编码
     *
     * @param srcFilePath 压缩文件源路径
     * @param destDirPath 解压后路径
     */
    public static String deCompress(String srcFilePath, String destDirPath) throws RuntimeException {
        File srcFile = new File(srcFilePath);
        if (!srcFile.exists()) {
            log.error("压缩文件{}不存在", srcFilePath);
            throw new RuntimeException("压缩文件不存在");
        }
        return deCompress(srcFilePath,destDirPath,CHARSET_UTF8);
    }

    /**
     * zip解压,默认解压到当前目录
     *
     * @param srcFilePath  压缩文件源路径
     */
    public static String deCompress(String srcFilePath) throws RuntimeException {
        File srcFile = new File(srcFilePath);
        if (!srcFile.exists()) {
            log.error("压缩文件{}不存在", srcFilePath);
            throw new RuntimeException("压缩文件不存在");
        }
        String destDirPath = srcFilePath.substring(0, srcFilePath.lastIndexOf("."));
        return deCompress(srcFilePath, destDirPath);
    }

    /**
     * zip解压（org.apache.ant）
     * @param srcFilePath 压缩文件路径
     * @param destDirPath 解压缩后的文件路径
     * @param encoding 编码
     * @return  解压缩后的文件路径
     */
    public static String unZip(String srcFilePath, String destDirPath,String encoding) throws RuntimeException{
        File srcFile = new File(srcFilePath);
        if (!srcFile.exists()) {
            log.error("压缩文件{}不存在", srcFilePath);
            throw new RuntimeException("压缩文件不存在");
        }
        File destDir = new File(destDirPath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        try {
            org.apache.tools.zip.ZipFile zip = new org.apache.tools.zip.ZipFile(srcFile,encoding);
            for (Enumeration enumeration = zip.getEntries(); enumeration.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) enumeration.nextElement();
                String zipEntryName = entry.getName();
                InputStream in = zip.getInputStream(entry);

                if (entry.isDirectory()) {      //处理压缩文件包含文件夹的情况
                    String path = destDirPath + File.separator + zipEntryName;
                    boolean mkdirs = new File(path).mkdirs();
                    continue;
                }

                File file = new File(destDirPath, zipEntryName);
                file.createNewFile();
                OutputStream out = new FileOutputStream(file);
                byte[] buff = new byte[1024];
                int len;
                while ((len = in.read(buff)) > 0) {
                    out.write(buff, 0, len);
                }
                in.close();
                out.close();
            }
        } catch (Exception e) {
            log.error("解压失败",e);
            throw new RuntimeException("解压失败");
        }
        return destDirPath;
    }


    /**
     * zip解压（org.apache.ant）,自动计算文件编码，默认utf-8
     * @param srcFilePath 压缩文件路径
     * @return
     * @throws RuntimeException
     */
    public static String unZip(String srcFilePath) throws RuntimeException {
        File srcFile = new File(srcFilePath);
        if (!srcFile.exists()) {
            log.error("压缩文件{}不存在", srcFilePath);
            throw new RuntimeException("压缩文件不存在");
        }
        String destDirPath = srcFilePath.substring(0, srcFilePath.lastIndexOf("."));
//        String encoding = FileCharsetDetectorUtil.guessFileEncoding(srcFilePath);
        String encoding = null;
        return unZip(srcFilePath, destDirPath, encoding != null ? encoding : CHARSET_UTF8);
    }
}
