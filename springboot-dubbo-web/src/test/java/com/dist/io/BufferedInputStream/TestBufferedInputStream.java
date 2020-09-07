package com.dist.io.BufferedInputStream;

import org.junit.Test;

import java.io.*;

/**
 * 缓冲流（字节和字符缓冲输入流和输出流）
 *
 *之前的读取文件中数据的操作，当读取数据量大的文件时，读取的速度会很慢，很影响我们程序的效率，
 * 为了提高速度，Java中提高了一套缓冲流，
 * 它的存在，可提高IO流的读写速度。
 *
 * 主要是缓冲一部分数据，然后在调用底层系统（Windows/Linux）进行IO操作
 *
 * 字节缓冲输出流
 * 使用方法：
 * BufferedInputStream(InputStream in)
 *
 * 字符缓冲输出流
 * 使用方法：
 * BufferedWriter(Writer w)
 */
public class TestBufferedInputStream {
    @Test
    public void test(){
        //testBufferedOutputStream(); //字节缓冲输出流
        //testBufferedInputStream();     //字节缓冲输入流

        //testBufferedWriter();     //字符缓冲输出流
        //testBufferedReader();       //字符缓冲输入流

        long s = System.currentTimeMillis();
        //copyBufferedFile();       //实现文件复制：节流缓冲区  最快的复制
        long e = System.currentTimeMillis();
        System.out.println(e-s);

        copyBufferedFileLine();     //使用缓冲区流对象,复制文本文件

    }

    /**
     *使用缓冲区流对象,复制文本文件
     * 数据源  BufferedReader+FileReader 读取
     * 数据目的 BufferedWriter+FileWriter 写入
     * 读取文本行, 读一行,写一行,写换行
     */
    public void copyBufferedFileLine(){
        BufferedReader br=null;
        BufferedWriter bw=null;
        try {
            br=new BufferedReader(new FileReader("d:\\aaa\\bbb.txt"));
            bw=new BufferedWriter(new FileWriter("d:\\asd\\bbb.txt"));
            String line=null;

            //读取文本行, 读一行,写一行,写换行
            while ((line=br.readLine()) !=null){
                bw.write(line);
                bw.newLine();
                bw.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bw.close();
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     *方法,实现文件复制
     *
     * 节流缓冲区流读写字节数组
     */
    public void copyBufferedFile(){
        BufferedInputStream bis=null;
        BufferedOutputStream bos=null;
        try {
            bis=new BufferedInputStream(new FileInputStream(new File("d:\\aaa\\bbb.txt")));
            bos=new BufferedOutputStream(new FileOutputStream(new File("d:\\asd\\bb.txt")));

            int len=0;
            byte[] bytes =new byte[1024];
            while ((len=bis.read(bytes)) !=-1){
                bos.write(bytes,0,len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                bos.close();
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 字符缓冲输入流
     *
     * BufferedReader(Reader r)
     *可以传递任意的字符输入流，传递是谁,就提高谁的效率
     */
    public void testBufferedReader(){

        FileReader fr =null;
        BufferedReader br=null;
        try {
            fr=new FileReader("d:\\aaa\\bbb.txt");
            br=new BufferedReader(fr);

            int len =0;
            char[] chars =new char[1024];
            String line =null;

            //BufferedReader的特有方法readline
            while ((line =br.readLine())!=null){
                System.out.println("line="+line);
            }

            /*while ((len=br.read()) !=-1){
                System.out.print((char)len);
            }*/

            /*while ((len=br.read(chars)) !=-1){
                System.out.print("chars="+new String(chars,0,len));
            }*/

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     *字符缓冲输出流
     *
     *使用方法：BufferedWriter(Writer w)
     *可以传递任意的字符输出流，传递是谁,就提高谁的效率
     */
    public void testBufferedWriter(){
        FileWriter fw =null;
        BufferedWriter bw =null;
        try {
            fw=new FileWriter("d:\\aaa\\bbb.txt");
            bw= new BufferedWriter(fw);

            bw.write("---你好世界".toCharArray());
            bw.newLine(); //换行，新行，BufferedWriter的特有的方法，具有平台无关性
            bw.write(65);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bw.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }




    //字节缓冲流

    /**
     * 字节缓冲输出流
     *
     * BufferedOutputStream (InputStream in)
     * 可以传递任意的字节输入流，传递是谁，就提高谁的效率
     */
    public void testBufferedOutputStream(){

        //创建字节输出流
        FileOutputStream fos =null;
        try {
            fos=new FileOutputStream("d:\\aaa\\bbb.txt");
            //创建字节输出流缓冲流的对象
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(79);
            bos.write("爱你一万年".getBytes());
            bos.write("你好，世界！".getBytes(),3,3);
            //bos.flush();
            bos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *字节缓冲输入流
     *
     *可以传递任意的字节输入流,传递是谁,就提高谁的效率。
     */
    public void testBufferedInputStream(){

        //创建输入流对象
        FileInputStream fis=null;
        BufferedInputStream bis =null;
        try {
            fis=new FileInputStream("d:\\aaa\\bbb.txt");
            //创建字节输入流的缓冲流对象
            bis =new BufferedInputStream(fis);
            int len =0;
            byte[] bytes =new byte[10];
            while ((len =bis.read(bytes)) !=-1){
                System.out.print(new String(bytes,0,len));  //注意print和println，当byte[5]
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bis.close();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
