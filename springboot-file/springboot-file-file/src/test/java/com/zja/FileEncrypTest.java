package com.zja;

import java.io.*;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-04-28 18:05
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：文件内容加密
 */
public class FileEncrypTest {

    public static void main(String[] args) throws Exception {
        //要加密的文件路径
        //String path = "D:\\TestFile\\test.txt";
        //String path = "D:\\TestFile\\行政区.tpk";

        //要解密文件路径
        String path = "D:\\TestFile\\test-Encrypted.txt";
        //String path = "D:\\TestFile\\行政区-Encrypted.tpk";
        String basePath = "C:\\SourceFile";

        //密钥
        String key = "2020Mbzfst0`bmmmbzfst0M150S1291D1291/cvoemyQL\u0002\u0003\u0015\u0001\u000B\u0001\u0001\u0001\u0001\u0001";

        /*boolean result = readFileLastByte(path, key);
        // 验证文件内容是否已被加密
        if (!result) {
            // 文件内容加密
            encrypt(path, key);
        } else {
            // 文件内容解密
            decrypt(path, key);
        }*/


        //必须时目录
        File file = new File(basePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        folder(file, key);
    }

    /**
     * 存放源文件：要加密的文件
     *
     * @param file 目录文件夹
     */
    private static void folder(File file, String key) throws Exception {
        File[] files = file.listFiles();
        if (null != files && files.length > 0) {
            for (File subFile : files) {
                if (subFile.isDirectory()) {
                    folder(subFile, key);
                } else {
                    //文件内容加密
                    encrypt(subFile.getAbsolutePath(), key);
                }
            }
        }
    }

    /**
     * 文件内容加密操作
     *
     * @param fileUrl 源文件路径：要加密的文件路径
     * @param key     密钥
     */
    public static String encrypt(String fileUrl, String key) throws Exception {
        File file = new File(fileUrl);
        String path = file.getPath();
        if (!file.exists()) {
            return null;
        }

        //验证是否加密 true /false: 已加密/未加密
        if (readFileLastByte(fileUrl, key)) {
            return null;
        }

        int index = path.lastIndexOf("\\");
        String destFile = path.substring(0, index) + "\\" + "temporaryFiles";
        File dest = new File(destFile);
        InputStream in = new FileInputStream(fileUrl);
        OutputStream out = new FileOutputStream(destFile);
        byte[] buffer = new byte[1024];
        int r;
        byte[] buffer2 = new byte[1024];
        while ((r = in.read(buffer)) > 0) {
            for (int i = 0; i < r; i++) {
                byte b = buffer[i];
                buffer2[i] = b == 255 ? 0 : ++b;
            }
            out.write(buffer2, 0, r);
            out.flush();
        }
        in.close();
        out.close();
        //file.delete();

        // 文件根路径
        String rootPath = fileUrl.substring(0, fileUrl.lastIndexOf("."));
        // 文件后缀
        String fileExtension = fileUrl.substring(fileUrl.lastIndexOf(".") + 1);
        // 加密后的文件路径
        String filePath = rootPath + "-Encrypted." + fileExtension;
        File encryptedFile = new File(filePath);
        if (encryptedFile.exists()) {
            encryptedFile.delete();
        }
        dest.renameTo(encryptedFile);
        //二次加密：追加加密的密文
        boolean appendMethodA = appendMethodA(filePath, key);
        System.out.println("源文件路径：" + fileUrl);
        System.out.println("加密成功-加密后的文件路径：" + filePath);
        return filePath;
    }

    /**
     * 文件内容解密操作
     *
     * @param fileUrl 源文件：要解密的文件路径
     * @param key     密钥
     */
    public static boolean decrypt(String fileUrl, String key) throws Exception {

        boolean result = false;

        File file = new File(fileUrl);
        if (!file.exists()) {
            return result;
        }

        //验证是否加密
        if (readFileLastByte(fileUrl, key)) {
            return false;
        }

        // 文件根路径
        String rootPath = fileUrl.substring(0, fileUrl.lastIndexOf("-"));
        // 文件后缀
        String fileExtension = fileUrl.substring(fileUrl.lastIndexOf(".") + 1);
        // 解密后的文件路径
        String filePath = rootPath + "-Decrypt." + fileExtension;

        File dest = new File(filePath);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        InputStream is = new FileInputStream(fileUrl);
        OutputStream out = new FileOutputStream(filePath);

        //密钥长度
        int keyLength = key.length();

        byte[] buffer = new byte[1024];
        byte[] buffer2 = new byte[1024];
        byte bMax = (byte) 255;
        long size = file.length() - keyLength;
        int mod = (int) (size % 1024);
        int div = (int) (size >> 10);
        int count = mod == 0 ? div : (div + 1);
        int k = 1, r;
        while ((k <= count && (r = is.read(buffer)) > 0)) {
            if (mod != 0 && k == count) {
                r = mod;
            }

            for (int i = 0; i < r; i++) {
                byte b = buffer[i];
                buffer2[i] = b == 0 ? bMax : --b;
            }
            out.write(buffer2, 0, r);
            k++;
        }
        out.close();
        is.close();
        System.out.println("源文件路径: " + fileUrl);
        System.out.println("解密成功-解密后的文件路径: " + filePath);
        return result;
    }

    /**
     * 在源文件尾部追加密钥
     *
     * @param fileName 源文件：要加密的文件路径
     * @param key      密钥
     */
    public static boolean appendMethodA(String fileName, String key) {
        boolean result = false;
        try {
            // 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            //将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            randomFile.writeBytes(key);
            randomFile.close();
            result = true;
        } catch (IOException e) {
            result = false;
            System.out.println(e.getMessage());
        } finally {
            return result;
        }
    }

    /**
     * 判断文件是否加密
     *
     * @param fileName 源文件：已加密的文件路径
     * @param key      密钥
     */
    public static boolean readFileLastByte(String fileName, String key) throws IOException {
        boolean result = false;
        File file = new File(fileName);
        if (!file.exists()) {
            return result;
        }
        // 打开一个随机访问文件流，按读写方式
        RandomAccessFile randomFile = new RandomAccessFile(fileName, "r");
        // 文件长度，字节数
        long fileLength = randomFile.length();
        // 密钥长度
        int keyLength = key.length();
        // 文件长度小于 密钥长度，则认为文件没有被加密
        if (fileLength < keyLength) {
            System.out.println(file.getName() + " = 不安全-未加密 0~0 ！");
            return result;
        }
        // 指针位置
        long pointerPosition = fileLength - keyLength;
        // 从指针位置开始读
        randomFile.seek(pointerPosition);
        // 读取字节大小
        byte[] buffer = new byte[keyLength];
        randomFile.read(buffer);

        //获取密钥
        String ciphertext = new String(buffer);
        randomFile.close();
        if (key.equals(ciphertext)) {
            result = true;
            System.out.println(file.getName() + " = 安全-已加密！");
        } else {
            System.out.println(file.getName() + " = 不安全-未加密 0~0 ！");
        }
        return result;
    }

}
