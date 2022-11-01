package com.zja.utils;

import java.io.*;

/**
 * java.io.file工具
 */
public abstract class FileUtil {

    //合法文件类型
    public static final String[] LEGAL_FILE_TYPE = new String[]{"jpg", "png", "jpeg", "pdf"};

    /**
     * 获取文件后缀
     *
     * @param file
     * @return
     */
    public static final String getFileSuffix(File file) {
        String fileName = file.getName();
        String suffixName = fileName.substring(fileName.lastIndexOf(".") + 1)
                .toLowerCase();
        return suffixName;
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
        for (String s : LEGAL_FILE_TYPE) {
            if (s.equalsIgnoreCase(fileSuffix)) {
                isLegal = true;
                break;
            }
        }
        return isLegal;
    }

    /**
     * 将二进制 转出 java.io.file
     *
     * @param srcFile
     * @param path
     */
    public static final boolean byte2File(byte[] srcFile, String path) {
        FileOutputStream out = null;
        String targetFolder = path.substring(0,path.lastIndexOf(File.separator));
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
     * @param filePath
     * @return
     */
    public static byte[] File2byte(String filePath)
    {
        byte[] buffer = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;

        try {
            File file = new File(filePath);
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1){
                bos.write(b, 0, n);
            }
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }finally {
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

    /**
     * InputStream 转 byte[]
     * @param inStream
     * @return
     */
    private byte[] readStream(InputStream inStream){
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        try {
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
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
}
