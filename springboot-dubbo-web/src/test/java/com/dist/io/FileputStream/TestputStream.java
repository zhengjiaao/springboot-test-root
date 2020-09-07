package com.dist.io.FileputStream;

import org.junit.Test;

import java.io.*;

/**
 *
 * FileOutputStream类:即文件输出流，是用于将数据写入File的输出流。
 * OutputStream有很多子类，其中子类FileOutputStream可用来写入数据到文件。
 *
 *字节输出流：
 * OutputStream抽象类
 * 此抽象类，是表示输出字节流的所有类的超类。操作的数据都是字节，定义了输出字节流的基本共性功能方法。
 *
 * 字节: 这样流每次只操作文件中的1个字节
 * 流对象：操作文件的时候，自己不做依赖操作系统
 * 作用：从Java程序，写入文件（可以写任意文件）
 *
 * 方法：
 *
 * write(int b) 写入1个字节
 * write(byte[] b) 写入字节数组
 * write(byte[] b,int,int)写入字节数组,int 开始写入的索引, int 写几个
 * close() 方法,关闭流对象,释放与此流相关的资源
 *
 * 流对象使用步骤：
 *
 * 1. 创建流子类的对象,绑定数据目的
 * 2. 调用流对象的方法write写
 * 3. close释放资源
 */
public class TestputStream {

    @Test
    public void test(){
        //fileOutputStream(); //创建新文件并写数据，文件续写加true
        fileInputStream();  //读取文件内容
        //copyFile();     //复制文件

    }


    //复制文件
    public void copyFile(){
        long s =System.currentTimeMillis();
        FileInputStream fis =null;
        FileOutputStream fos = null;

        try {
            fis=new FileInputStream("d:\\aaa\\bbb.txt");
            fos=new FileOutputStream("d:\\aaa\\copy\\bbb.txt");

            //定义字节数组，缓冲
            byte[] bytes =new byte[1024*10];
            //读取数组
            int len =0;
            while ((len=fis.read(bytes)) !=-1){
                fos.write(bytes);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (fos !=null)
                fos.close();
                if (fis !=null)
                fis.close();
            } catch (IOException e) {
                throw new RuntimeException("释放资源失败");
            }
        }
        long e = System.currentTimeMillis();
        System.out.println(e-s);
    }

    //读取文件内容的两种方式
    public void fileInputStream(){
        FileInputStream fis =null;
        try {
            fis=new FileInputStream("d:\\aaa\\asd.txt");

            //创建转换流对象,构造方法中,包装字节输入流,同时写编码表名
            InputStreamReader isr =new InputStreamReader(fis,"UTF-8");

/*            System.out.println("===========读取单个字节============");
            //读取一个字节,调用方法read 返回int
            int len =0; //接收read方法的返回值
            // 使用循环方式,读取文件,  循环结束的条件  read()方法返回-1
            while ((len=fis.read()) != -1){     // 返回值保存到len中
                System.out.print((char) len); //中文乱码
            }*/
            System.out.println(" ");
            System.out.println("===========读取一个字节数组============");
            //创建字节数组
            byte[] b =new byte[1024];
            char[] chars=new char[1024];
            int leng =0;
            while ((leng =fis.read(b)) != -1){ //保存到字节数组b中
                System.out.println(new String (b,0,leng));  //乱码
            }
 /*           while ((leng =isr.read(chars)) != -1){ //保存到字节数组b中
                System.out.println(new String (chars,0,leng));  //解决了乱码。如果还是没解决，把文件另存为UTF-8
            }*/


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            throw new RuntimeException("读取内容失败，重试");
        }finally {
            try {
                if (fis !=null)
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //创建新文件并写数据
    public void fileOutputStream(){
        FileOutputStream fos =null;
        try {
            //流对象的构造方法,可以创建文件,如果文件存在,直接覆盖
            //fos = new FileOutputStream("d:\\aaa\\bbb.txt");
            //文件续写加true
            fos = new FileOutputStream("d:\\aaa\\bbb.txt",true);
            fos.write(98); // write写数据,写1个字节，查询编码表

            byte[] bytes ={65,66,67,68,97};
            fos.write(bytes);   //写字节数组

            fos.write(bytes, 1, 2); //写字节数组的一部分,开始索引,写几个

            fos.write("hello".getBytes());  //写字符串；字符串要转byte数组
        } catch (FileNotFoundException e) {
            throw new RuntimeException("文件创建失败");
        }catch (IOException e) {
            throw new RuntimeException("文件写入失败,重试");
        }finally {
            try {
                if(fos !=null)
                fos.close();
            } catch (IOException e) {
                throw new RuntimeException("关闭资源失败");
            }
        }
    }
}
