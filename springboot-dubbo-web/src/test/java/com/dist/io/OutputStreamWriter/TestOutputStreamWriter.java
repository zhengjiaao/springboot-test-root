package com.dist.io.OutputStreamWriter;

import org.junit.Test;

import java.io.*;

/**
 * 转换流
 *FileOutputStream/FileInputStream保存的文件格式为当前系统的编码格式，
 * 如果我们想转换成其他的格式就要使用转换流
 *
 */
public class TestOutputStreamWriter {
    @Test
    public void test(){
        //testOutputStreamWriter();    // 输出转换流
        testInputStreamReader();     //输入转换流
    }

    /**
     *二、输入转换流
     * InputStreamReader类
     * 继承于Reader类，接受的是不同形式编码格式的字节，根据不同的编码表转换成字符格式（不同格式的字节==>字符）
     *
     * 使用格式：
     *
     * InputStreamReader(InputStream in,String charsetName)
     */
    public void testInputStreamReader(){

        //转换流,InputSteamReader读取文本,采用UTF-8编码表,读取文件utf

        //创建自己输入流,传递文本文件
        FileInputStream fis=null;
        try {
            fis= new FileInputStream("d:\\aaa\\bbb.txt");

            //创建转换流对象,构造方法中,包装字节输入流,同时写编码表名
            InputStreamReader isr =new InputStreamReader(fis,"UTF-8");

            char[] ch =new char[1024];
            int len =0;

            while ((len =isr.read(ch)) !=-1){
                System.out.println(new String(ch,0,len));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (fis!=null)
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 一、输出转换流
     * OutputStreamWriter类
     * 继承于Writer类，接受的是字符数组、字符串、int型，转换成不同形式编码格式的字节（字符==>不同格式的字节），然后存入文件中。
     *
     * 使用格式：
     *
     * OutputStreamWriter(OutputStream out, String charsetName)
     */
    public void testOutputStreamWriter(){

        //转换流对象OutputStreamWriter写文本,采用UTF-8编码表写入

        //创建字节输出流，绑定文件
        FileOutputStream fos =null;
        OutputStreamWriter osw=null;
        try {
            fos =new FileOutputStream("d:\\aaa\\bbb.txt",true);
            //创建转换流对象，构造方法保装字节输出流，并指定编码表是UTF-8
            osw= new OutputStreamWriter(fos,"UTF-8");
            osw.write("你好");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (osw!=null)
                osw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
