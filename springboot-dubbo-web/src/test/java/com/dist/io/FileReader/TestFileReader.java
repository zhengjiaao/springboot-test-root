package com.dist.io.FileReader;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @program: jms-spring
 * @Date: 2018/12/5 14:19
 * @Author: Mr.Zheng
 * @Description:
 */
public class TestFileReader {
    @Test
    public void test(){
        //fileReader();   //字符输入——FileReader
        //fileWriter();   //字符输出流——FileWriter
        copyFile();
    }

    //copy文件
    public void copyFile(){
        FileReader fr = null;
        FileWriter fw = null;

        try {
            fr = new FileReader("d:\\aaa\\bbbb.txt");
            fw = new FileWriter("d:\\aaa\\copy\\b.txt");

            char[] ch =new char[1024];
            int len =0;
            while ((len=fr.read(ch)) != -1){
                fw.write(ch,0,len);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (fw !=null)
                    fw.close();
                if (fr !=null)
                    fr.close();
            } catch (IOException e) {
                throw new RuntimeException("释放资源失败");
            }
        }

    }


    //字符输入流只能读取文本文件，所有字符输入流的超类为java.io.Reader类
    //字符输入——FileReader类
    //int read() 读取1个字符
    //int read(char[] c) 读取字符数组
    public void fileReader(){
        FileReader fr=null;
        try {
            fr=new FileReader("d:\\aaa\\bbb.txt");
            //读取单个字符
          /*  int len =0;
            while ((len =fr.read()) !=-1){
                System.out.print((char)len);
            }*/

          //读取字符数组
            char[] ch =new char[1024];
            int leng =0;
            while ((leng =fr.read(ch)) != -1){
                System.out.println(new String(ch,0,leng));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //所有字符输出流的超类为java.io.Writer
    //字符输出流——FileWriter
    /* write(int c) 写1个字符
     write(char[] c)写字符数组
     write(char[] c,int,int)字符数组一部分,开始索引,写几个
     write(String s) 写入字符串*/

    public void fileWriter(){
        FileWriter fw =null;
        try {
            //加true续写，不覆盖原来的内容
            fw =new FileWriter("d:\\aaa\\bbbb.txt",true);
            //写1个字符
            fw.write(67);
            fw.flush();

            //写1个字符数组
            char[] c = {'a','b','c','d','e'};
            fw.write(c);
            fw.flush();

            //写字符数组一部分
            fw.write(c, 2, 2);
            fw.flush();

            //写入字符串
            fw.write("hello");
            fw.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (fw !=null)
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
