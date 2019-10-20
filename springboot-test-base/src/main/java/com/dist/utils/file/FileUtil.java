package com.dist.utils.file;

import com.dist.constant.EnvironmentContants;
import com.dist.constant.FileContants;
import com.dist.util.exception.BusinessException;
import com.dist.utils.office.OfficeToPdfUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具
 * @author xupp
 * @date 2018/12/21
 */
public class FileUtil {

    private FileUtil(){
        super();
    }

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
    private static final List<String> NO_NEED_TRANSFORM_FILTER = new ArrayList<>();
    private static final List<String> NEED_TRANSFORM_CHARSET_FILTER = new ArrayList<>();
    private static final List<String> NEED_DOWNLOAD_FILTER = new ArrayList<>();

    static {
        NO_NEED_TRANSFORM_FILTER.add("pdf");
        NO_NEED_TRANSFORM_FILTER.add("jpg");
        NO_NEED_TRANSFORM_FILTER.add("png");
        NO_NEED_TRANSFORM_FILTER.add("jpeg");
        NO_NEED_TRANSFORM_FILTER.add("gif");
        NO_NEED_TRANSFORM_FILTER.add("tif");


        NEED_TRANSFORM_CHARSET_FILTER.add("txt");
        NEED_TRANSFORM_CHARSET_FILTER.add("xml");
        NEED_TRANSFORM_CHARSET_FILTER.add("dwg");
        NEED_TRANSFORM_CHARSET_FILTER.add("doc");
        NEED_TRANSFORM_CHARSET_FILTER.add("docx");
        NEED_TRANSFORM_CHARSET_FILTER.add("ppt");
        NEED_TRANSFORM_CHARSET_FILTER.add("pptx");
        NEED_TRANSFORM_CHARSET_FILTER.add("xlsx");
        NEED_TRANSFORM_CHARSET_FILTER.add("xls");

        NEED_DOWNLOAD_FILTER.add("zip");
        NEED_DOWNLOAD_FILTER.add("rar");

    }

    public static final String CADTRANSIMGEXEPATH=System.getProperty(EnvironmentContants.SYSTEM_CAD_TRANS_EXEPATH);
    public static final String CADTRANSIMGHEIGHT=System.getProperty(EnvironmentContants.SYSTEM_CAD_TRANS_IMGHEIGHT);
    public static final String CADTRANSIMGWIDTH=System.getProperty(EnvironmentContants.SYSTEM_CAD_TRANS_IMGWIDTH);
    public static final String CADTRANSIMGSUFFIX=System.getProperty(EnvironmentContants.SYSTEM_CAD_TRANS_IMGSUFFIX);


    /**
     * 复制单个文件
     *
     * @param oldPath
     *            String 原文件路径 如：c:/fqf.txt
     * @param newPath
     *            String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static boolean copyFile(String oldPath, String newPath) {
        try ( InputStream inStream = new FileInputStream(oldPath);
              FileOutputStream fs = new FileOutputStream(newPath);){

            int byteread = 0;
            File oldfile = new File(oldPath);
            File newDir = new File(newPath.replace('\\', '/').substring(0,
                    newPath.replace('\\', '/').lastIndexOf('/')));
            if (!newDir.exists()) {
                newDir.mkdirs();
            }
            if (oldfile.exists()) { // 文件存在时
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                fs.flush();
            }
            return true;
        } catch (Exception e) {
            logger.error("复制单个文件操作出错");
            return false;
        }
    }

    /**
     * 删除文件
     *
     * @param filePath
     */
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 创建目录
     *
     * @param dirPath
     */
    public static void createDirs(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dirPath 将要删除的文件目录
     */
    public static void deleteDir(String dirPath) {
        try {
            FileUtils.deleteDirectory(new File(dirPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *通过文件url 获取文件后缀
     * @param fileUrl
     * @return
     */
    public static String getFileSufix(String fileUrl){
        int splitIndex = fileUrl.lastIndexOf('.');
        return fileUrl.substring(splitIndex + 1);
    }

    /**
     * 获取文件后缀
     *
     * @param file
     * @return
     */
    public static String getFileSuffix(File file) {
        String fileName = file.getName();
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * 获取文件后缀
     *
     * @param fileName
     * @return
     */
    public static String getFileSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 获取文件名的后缀名，注意图片后缀的获取
     * @param fileName 文件名
     * @return 后缀名，没有带点号“.”
     */
    public static String getSuffix(String fileName) {

        Assert.hasLength(fileName);

        String result = "";
        try {

            String hex = "";
            File file = new File(fileName);
            if (!file.exists()) {
                return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
            }
            InputStream is = new FileInputStream(file);
            byte[] bt = new byte[2];
            is.read(bt);

            hex = bytesToHexString(bt);
            is.close();
            if (hex.equals("ffd8")) {
                result = "jpg";
            } else if (hex.equals("4749")) {
                result = "gif";
            } else if (hex.equals("8950")) {
                result = "png";
            } else {
                result = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage());

        }

        return result;
    }

    /**
     * 修改文件的后缀
     *
     * @param fileName      文件名
     * @param replaceSuffix 要修改的后缀名，不带.
     * @return
     */
    public static String replaceFileSuffix(String fileName, String replaceSuffix) {
        if (fileName.contains(".")) {
            int i = fileName.lastIndexOf(".");
            String substring = fileName.substring(0, i);
            fileName = substring + "." + replaceSuffix;
        }
        return fileName;
    }

    /**
     * 删除文件后缀
     *
     * @param fileName
     * @return
     */
    public static String delFileNameSuffix(String fileName) {
        if (fileName == null) {
            return null;
        }
        return fileName.substring(0, fileName.lastIndexOf("."));
    }


    /**
     * 获取文件名称（包括文件的后缀）
     * @param fileUrl  文件路径
     * @return
     */
    public static String getFileName(String fileUrl){
        int splitIndex = fileUrl.lastIndexOf('/');
        return fileUrl.substring(splitIndex + 1);
    }

    /**
     * 检查文件类型是否合法
     *
     * @param file
     * @return
     */
    public static final boolean isLegalFileType(File file) {
        boolean isLegal = false;
        if (null == file) {
            return isLegal;
        }
        String fileSuffix = getFileSuffix(file);
        for (String s : NO_NEED_TRANSFORM_FILTER) {
            if (s.equalsIgnoreCase(fileSuffix)) {
                isLegal = true;
                break;
            }
        }
        return isLegal;
    }

    /**
     * 根据文件路径检测文件的合法性
     *
     * @param filePath
     * @return
     */
    public static boolean legalFile(String filePath) {

        File file = new File(filePath);
        FileInputStream fis = null;
        try {
            if (file.exists() && file.isFile()) {
                fis = new FileInputStream(file);
                if (fis.available() > 0) {
                    return true;
                } else {
                    file.delete();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 将二进制 转出 java.io.file
     *
     * @param srcFile
     * @param path
     */
    public static final boolean byte2File(byte[] srcFile, String path) {
        FileOutputStream out = null;
        String targetFolder = path.substring(0, path.lastIndexOf(File.separator));
        try {
            File folder = new File(targetFolder);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            out = new FileOutputStream(path);
            out.write(srcFile);
            out.flush();
        } catch (Exception e) {
            return false;
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    /**
     * file 转 byte[]
     *
     * @param filePath
     * @return
     */
    public static byte[] File2byte(String filePath) {
        byte[] buffer = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;

        try {
            File file = new File(filePath);
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fis) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (null != bos) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return buffer;
    }


    /**file 转 byte[]
     *
     * 获取文件的所有字节
     * @param filePath
     * @return
     * @throws Exception
     */
    public byte[] file2byte(String filePath){
        byte[] buffer =null;
        try(FileInputStream inputStream = new FileInputStream(filePath)) {
            buffer = new byte[(int)new File(filePath).length()];
            int count=0;
            while((count= inputStream.read(buffer))>0){
                //一次向数组中写入数组长度的字节
                System.out.println("文件下载中。。。。");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return buffer;
    }

    /**
     * InputStream 转 byte[]
     *
     * @param inStream
     * @return
     */
    private byte[] readStream(InputStream inStream) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        try {
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return outStream.toByteArray();
    }

    /**
     * 转换文档
     * @param srcFile
     * @return 返回转换后的文件的 路径
     */
    public static synchronized String converseFile(
            String srcFile) throws BusinessException {

        //首先获取文件的后缀
        boolean flag=false;
        String extension = getFileSufix(srcFile).toLowerCase();

        String transformFile=srcFile.substring
                (0,srcFile.lastIndexOf(FileContants.FILE_SUFFIX_SEPARATION)+1)+
                FileContants.SYSTEM_FILE_TRANSFORM_EXTENSION;

        if(new File(transformFile).exists()){
            return transformFile;
        }

        if("cad".endsWith(extension)){
            return converseCadFile(srcFile);
        }
        if(NO_NEED_TRANSFORM_FILTER.contains(extension)){
            // 不需要转换
            flag= true;
        }else if(NEED_TRANSFORM_CHARSET_FILTER.contains(extension)){
            long startTime = System.currentTimeMillis();
            flag= OfficeToPdfUtil.convert2PDF(srcFile,transformFile);
            long endTime = System.currentTimeMillis();
            logger.info("INFO>>>>转换文档花费时间：{} ms",(endTime-startTime));
        }else if(NEED_DOWNLOAD_FILTER.contains(extension)){
            return srcFile;
        }else {
            throw new BusinessException("格式未知");
        }
        if(!flag){
            throw new BusinessException("文档转换失败");
        }
        return transformFile;
    }

    /**
     * 将cad 文件进行转换
     * @param localFileName
     * @return 返回转换后的文件的 路径
     */
    public static synchronized String converseCadFile(
            String localFileName) {

        //首先获取文件的后缀
        String cadTransImgFileName=localFileName.substring
                (0,localFileName.lastIndexOf(FileContants.FILE_SUFFIX_SEPARATION)+1)+
                CADTRANSIMGSUFFIX;
        //cad转图片文件名称
        Runtime runtime = Runtime.getRuntime();
        String [] param = {
                CADTRANSIMGEXEPATH,
                localFileName,cadTransImgFileName,
                CADTRANSIMGHEIGHT,
                CADTRANSIMGWIDTH};
        try {
            long beginTime  = System.currentTimeMillis();
            final Process process = runtime.exec(param);
            /** 0表示成功*/
            int exitcode = process.waitFor();
            logger.info(">>CAD转图片耗时>> {}" , (System.currentTimeMillis() - beginTime));
            logger.info("{} >>CAD转图片是否成功[0表示成功]>> {}",localFileName , exitcode);
            return cadTransImgFileName;
        } catch (IOException e) {
            logger.error("CAD转图片失败>> {}" , e);
        }catch (InterruptedException e){
            logger.error("CAD转图片失败>> {}" , e);
            Thread.currentThread().interrupt();
        }
        return null;
    }

}
