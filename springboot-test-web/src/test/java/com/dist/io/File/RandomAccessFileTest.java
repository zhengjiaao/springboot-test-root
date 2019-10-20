package com.dist.io.File;

import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/9/2 10:27
 */
public class RandomAccessFileTest {

    public static void main(String[] args) {

        try {
            File file = new File("D:\\FileTest\\test1.txt");//创建一个txt文件内容是123456789
            RandomAccessFile ras = new RandomAccessFile(file, "rw");
            //System.out.println("ras当前指针位置：" + ras.getFilePointer() + "  length" + ras.length());
            //默认情况下ras的指针为0，即从第1个字节读写到
            ras.seek(1);//将ras的指针设置到8，则读写ras是从第9个字节读写到
            //System.out.println("ras当前指针位置-：" + ras.getFilePointer());

            File file2 = new File("D:\\FileTest\\test2.txt");
            RandomAccessFile ras2 = new RandomAccessFile(file2, "rw");
            //System.out.println("ras2当前指针位置：" + ras2.getFilePointer());
            ras2.setLength(10);
            ras2.seek(5);
            byte[] buffer = new byte[32];
            int len = 0;
            while ((len = ras.read(buffer)) != -1) {
                ras2.write(buffer, 0, len);//从ras2的第6个字节被写入，因为前面设置ras2的指针为5
                //ras2的写入结果是:pp.txt的内容为前5位是空格，第6位是9
                //待写入的位置如果有内容将会被新写入的内容替换
            }
            //System.out.println("ras2当前指针位置-：" + ras2.getFilePointer());
            //System.out.println("ras当前指针位置：" + ras.getFilePointer());
            ras.close();
            ras2.close();
            System.out.println("ok");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /****获取文件总的字节***/
    @Test
    public void test() throws Exception {
        long begin = System.currentTimeMillis();
        byte[] bytes = downloadFile("D:\\FileTest\\移动电子底图.tpk");
        System.out.println("bytes.length = " + bytes.length);
        long end = System.currentTimeMillis();
        System.out.println("用时" + (end - begin));
    }

    /****获取文件总的字节***/
    public byte[] downloadFile(String filePath) throws Exception {
        byte[] buffer = null;
        try (FileInputStream inputStream = new FileInputStream(filePath);) {
            buffer = new byte[(int) new File(filePath).length()];
            int count = 0;
            while ((count = inputStream.read(buffer)) > 0) {
                // 一次向数组中写入数组长度的字节
                System.out.println("文件下载中。。。。");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return buffer;
    }

    /**电脑会卡**/
    @Test
    public void test2() throws IOException {
        long begin = System.currentTimeMillis();
        FileInputStream is = new FileInputStream("D:\\FileTest" + File.separator + "移动电子底图.tpk");
        byte[] bytes = new byte[64 * 1024 * 1024];//64MB
        int length = 0;
        while ((length = is.read(bytes)) != -1) {
            System.out.println(new String(bytes, 0, length));
        }
        is.close();
        long end = System.currentTimeMillis();
        System.out.println("用时" + (end - begin));
        //当缓冲数组的大小是1GB时，会有如下异常
        // Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
        //at com.westos.test.Readerfile.main(Readerfile.java:11)
        //java虚拟机默认的最大内存是64MB,所以此异常是超出异常
        //若缓冲内存大小为64MB,则用时为35937
    }


    @Test
    public void test3() throws IOException {
        long begin = System.currentTimeMillis();
        int n = 3;
        List<String> lines = new ArrayList<>();
        try (RandomAccessFile f = new RandomAccessFile("D:\\FileTest\\移动电子底图.tpk", "r")) {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            for (long length = f.length(), p = length - 1; p > 0 && lines.size() < n; p--) {
                f.seek(p);
                int b = f.read();
                if (b == 10) {
                    if (p < length - 1) {
                        lines.add(0, getLine(bout));
                        bout.reset();
                    }
                } else if (b != 13) {
                    bout.write(b);
                }
            }
        }
        System.out.println(lines);
        long end = System.currentTimeMillis();
        System.out.println("用时" + (end - begin));
    }

    public String getLine(ByteArrayOutputStream bout) {
        byte[] a = bout.toByteArray();
        // reverse bytes
        for (int i = 0, j = a.length - 1; j > i; i++, j--) {
            byte tmp = a[j];
            a[j] = a[i];
            a[i] = tmp;
        }
        return new String(a);
    }



    /**分片读取**/
    public static byte[] getBlock(long offset, File file, int blockSize) {
        byte[] result = new byte[blockSize];
        RandomAccessFile accessFile = null;
        try {
            accessFile = new RandomAccessFile(file, "r");
            accessFile.seek(offset);
            int readSize = accessFile.read(result);
            if (readSize == -1) {
                return null;
            } else if (readSize == blockSize) {
                return result;
            } else {
                byte[] tmpByte = new byte[readSize];
                System.arraycopy(result, 0, tmpByte, 0, readSize);
                return tmpByte;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (accessFile != null) {
                try {
                    accessFile.close();
                } catch (IOException e1) {
                }
            }
        }
        return null;
    }

}
