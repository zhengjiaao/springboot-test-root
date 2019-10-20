package com.dist.util;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

/**
 * 压缩文件 无中文乱码问题
 * @author qyp
 * 2018-3-24 下午05:12:30
 */
public class ZipUtils {

    private final static Logger logger = LoggerFactory.getLogger(ZipUtils.class);

	public static void main(String[] args) throws Exception {

        //D:\doc\ca 下，只能是多个文件，不能有文件夹
        File rootFile = new File("D:\\doc\\ca");

        List<InputStream> ins = new ArrayList<>();
        List<String> names = new ArrayList<>();
        List<String> namePaths = Lists.newArrayList();
        File[] list = rootFile.listFiles();
        for (File f : list) {
            names.add(f.getName());
            namePaths.add(f.getAbsolutePath());
            //如果是文件夹，获取流失败，提示 “拒绝访问”
            ins.add(new FileInputStream(f));
        }
        //zipByInStream("e:\\temp\\a.zip", ins, names);
        //zipByFileNames("e:\\temp\\a.zip", namePaths);
        zipByFiles("d:\\doc\\caa.zip", Arrays.asList(list));
	}

    /**
     * 将文件的流集合代表的文件压缩成一个压缩包并保存到指定位置
     * @param savePath  生成的压缩包的位置
     * @param inputStreams 文件流集合
     * @param names     文件名称集合
     * @throws Exception 如果文件路径不存在或者压缩失败，那么久会抛异常
     */
	public static void zipByInStream(String savePath, List<InputStream> inputStreams, List<String> names) throws Exception {
        ZipOutputStream zos = getOutPutStream(savePath);
        BufferedReader bufr = null;
		// 缓存输出流
        try (BufferedOutputStream out = new BufferedOutputStream(zos)) {
            for (int i = 0, len = inputStreams.size(); i < len; i++) {
                //添加一个条目到压缩包
                zos.putNextEntry(new ZipEntry(names.get(i)));
                int c;
                // StandardCharsets.ISO_8859_1 防止文件内容乱码
                bufr = new BufferedReader(new InputStreamReader(inputStreams.get(i), StandardCharsets.ISO_8859_1));
                while ((c = bufr.read()) != -1) {
                    out.write(c);
                }
                out.flush();
                if (bufr != null) {
                    bufr.close();
                }
            }
        } catch (IOException r) {
            throw new RuntimeException("文件压缩失败");
        } finally {
            if (bufr != null) {
                bufr.close();
            }
        }
	}

    /**
     * 根据文件名集合获取文件并压缩
     * @param savePath          保存的压缩文件路径
     * @param targetFileNames   被压缩的文件名集合（全路径）
     * @throws Exception
     */
    public static void zipByFileNames(String savePath, List<String> targetFileNames) throws Exception {
        Function<Object, String> nameFun = (obj) -> getFileName((String) obj);
        Function<Object, InputStream> inFun = (obj) -> {
            try {
                return new FileInputStream((String) obj);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        };
        zipFile(savePath, targetFileNames, nameFun, inFun);
    }

    /**
     * 根据文件集合生成压缩文件
     * @param savePath      保存的压缩文件路径
     * @param targetFiles   被压缩的文件集合
     * @throws Exception
     */
    public static void zipByFiles(String savePath, List<File> targetFiles) throws Exception {
	    Function<Object, String> nameFun = (obj) -> ((File)obj).getName();
        Function<Object, InputStream> inFun = (obj) -> {
            try {
                return new FileInputStream((File) obj);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        };
        zipFile(savePath, targetFiles, nameFun, inFun);
    }

    /**
     * 压缩文件
     * @param savePath  保存的压缩文件路径
     * @param targets   被压缩的对象（文件名集合（全路径），或者文件集合）
     * @param nameFun   获取文件名的Function对象
     * @param inFun     获取InputStream的Function对象
     * @throws Exception
     */
    private static void zipFile(String savePath, List<?> targets, Function<Object, String> nameFun, Function<Object, InputStream> inFun) throws Exception {
        ZipOutputStream zos = getOutPutStream(savePath);
        BufferedReader bufr = null;
        // 缓存输出流
        try (BufferedOutputStream out = new BufferedOutputStream(zos)) {
            for (int i = 0, len = targets.size(); i < len; i++) {
                zos.putNextEntry(new ZipEntry(nameFun.apply(targets.get(i))));
                int c;
                bufr = new BufferedReader(new InputStreamReader(inFun.apply(targets.get(i)), StandardCharsets.ISO_8859_1));
                while ((c = bufr.read()) != -1) {
                    out.write(c);
                }
                out.flush();
                if (bufr != null) {
                    bufr.close();
                }
            }
            logger.info("成功压缩{}个文件，保存地址为【{}】", targets.size(), savePath);
        } catch (IOException r) {
            throw new RuntimeException("文件压缩失败");
        } finally {
            if (bufr != null) {
                bufr.close();
            }
        }
    }

    /**
     * 获取apache的ZipOutputStream流
     * @param savePath 保存压缩文件的路径
     * @return
     * @throws FileNotFoundException 如果保存路径不存在，那么将会抛异常
     */
    public static ZipOutputStream getOutPutStream(String savePath) throws FileNotFoundException {
        //输出的压缩原始文件流fileDir + "\\" + "详情.zip"
        FileOutputStream f = new FileOutputStream(new File(savePath));
        // 计算和校验文件
        CheckedOutputStream csum = new CheckedOutputStream(f, new CRC32());
        // 输出的压缩文件流
        ZipOutputStream zos = new ZipOutputStream(csum);
        zos.setComment("dist文件压缩流");
        //设置压缩工具右边的文字编码  防止右边的说明文字乱码
        zos.setEncoding(System.getProperty("sun.jnu.encoding"));
        return zos;
    }

    /**
     * 获取文件名
     * @param filePath
     * @return
     */
    public static String getFileName(String filePath) {
        int idx = StringUtils.lastIndexOf(filePath, File.separator);
        if (idx != -1) {
            return StringUtils.substring(filePath, idx + 1);
        }
        return filePath;
    }

}
