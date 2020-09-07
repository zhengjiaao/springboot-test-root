package com.dist.io.File;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @program: jms-spring
 * @Date: 2018/12/5 10:25
 * @Author: Mr.Zheng
 * @Description:
 */
public class TestFile {
    @Test
    public  void  test(){
        //testFile();   //文件目录分割符及名称分割
        //FilePath();   //传递路径,传递File类型父路径,字符串子路径
        //newFile();    //创建文件及文件夹
        //deleteFile();   //删除文件及文件夹
        //ObtainFile();   //获取文件及文件夹名字
        //getFileBytes();   //获取文件字节数
        //FileAbsolutPath();  //获取文件路径及父路径
        //FilePathExist();  //判断路径是否存在
        //determineFile();   //判断是否为文件/文件夹
        //DirectoryFile();    //遍历目录下所有的文件
        //FileFilter();     //根据文件后缀，文件过滤
        getAllDir(new File("d:\\aaa"));   //用递归的方式遍历目录下所有的文件

    }

    //用递归的方式遍历目录下所有的文件
    public void getAllDir(File file){
        System.out.println(file);
        //调用方法listFiles()对目录,dir进行遍历
        File[] files =file.listFiles();
        for (File f : files){
            //判断变量f表示的路径是不是文件夹
            if (f.isDirectory()){
                getAllDir(f); ////是一个目录,就要去遍历这个目录
            }else {
                System.out.println("文件=="+f);
            }
        }
    }

    //根据文件后缀，文件过滤
    public void FileFilter(){

        File file = new File("d:\\aaa");
        File[] fIles =file.listFiles(new MyFileFilter());
        for (File f :fIles){
            System.out.println("aaa下所有.txt文件="+f);
        }
    }


    //遍历目录下所有的文件
    public void DirectoryFile(){

        //返回的是目录或者文件的全路径
        File file =new File("D:\\aaa");
        File[] files =file.listFiles();
        for (File f :files){
            System.out.println("aaa下文件"+f); //aaa下文件D:\aaa\sd.txt
        }

        System.out.println("==========================");

        //返回只有名字
        File file1 =new File("D:\\aaa");
        String[] ff =file1.list();
        for (String flename :ff){
            System.out.println("aaa下所有文件名"+flename);
        }
    }


    //判断是否为文件/文件夹
    public void determineFile(){

        File file =new File("D:\\aaa\\bbb.txt");
        if (file.exists()){
            boolean b = file.isDirectory();
            System.out.println("是否为文件夹=="+b); //D:\aaa   true

            boolean b1 =file.isFile();
            System.out.println("是否为文件="+b1); //D:\aaa\bbb.txt   true
        }
    }

    //判断路径是否存在
    public void FilePathExist(){
        File file=new File("src");
        boolean b =file.exists();
        System.out.println("文件路径是否存在==="+b);

    }

    //获取文件路径及父路径
    public void FileAbsolutPath(){
        File file = new File("src");
        File absolute = file.getAbsoluteFile();
        System.out.println("获取文件绝对路径="+absolute);

        File file1 = new File("d:\\aaa\\bbb.txt");
        File parent = file1.getParentFile();
        System.out.println("获取文件父路径=="+parent);

    }

    //获取文件字节数
    public void getFileBytes(){

        File file =new File("D:\\aaa\\bbb.txt");
       long FileBytes = file.length();
        System.out.println("文件字节数="+FileBytes);
    }

    //获取文件及文件夹名字
    public void ObtainFile(){

        File file =new File("D:\\aaa\\bbb.txt"); //D:\\aaa
        String name =file.getName();
        System.out.println("文件或文件夹名称"+name); //aaa或bbb.txt
    }


    //删除文件及文件夹,不走回收站,直接从硬盘中删除
    public void deleteFile(){

        File file =new File("D:\\aaa\\asdf.txt");
        System.out.println("删除文件="+file.delete());
    }


    //创建文件及文件夹
    public void newFile(){
        File file =new File("D:\\aaa\\asdf.txt");
        boolean b = false;
        try {
            b = file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("创建文件asdf.txt=="+b);

        File file1 = new File("d:\\asd");
        boolean b1 =file1.mkdirs();
        System.out.println(b1+"=创建文件夹b1");

    }


    /*
     *  File(File parent,String child)
     *  传递路径,传递File类型父路径,字符串子路径
     */
    public static void FilePath(){

        //3种构造方式

        //好处: 父路径是File类型,父路径可以直接调用File类方法
        File parent =new File("D:");
        File file = new File(parent,"ccc");
        System.out.println("file="+file);

        //好处: 单独操作父路径和子路径
        File file1 =new File("D:","aa");
        System.out.println("file1="+file1);

        //传递路径名: 可以写到文件夹,可以写到一个文件.将路径封装File类型对象.
        File file2 =new File("D:\\bbb");
        System.out.println("file2="+file2);

    }

    //文件目录分割符及名称分割
    public void testFile(){
        //File类静态成员变量
        //与系统有关的路径分隔符
        String Separator = File.pathSeparator;
        System.out.println(Separator); //    ";",目录的分割 Linux :

        //与系统有关的默认名称分隔符
        Separator =File.separator;
        System.out.println(Separator); //   "\",目录名称分割  Linux /
    }
}
