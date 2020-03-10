package com.dist;

import org.junit.Test;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Date: 2020-01-02 9:25
 * Author: zhengja
 * Email: zhengja@dist.com.cn JaneBookBlogToPersonalBlog
 * Desc： 简书博客文章 转化适配为 搭建的个人博客文章 hexo-next7-blog
 */
public class JaneBookBlogToPersonalBlog {

    private static String filePath = "D:\\FileTest\\user"; //要操作的文件的目录
    private static DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Test
    public void test() {
        File file = new File(filePath);
        if (file.exists() && file.isDirectory()) {
            try {
                isDirectory(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("文件或目录路径不存在！");
        }
    }

    /**
     * title：文章名称=文件名称
     * tags：章标签=文件父级目录名称
     * categories：分类
     * resources：资源
     * copyright： true/false 版权
     * comments： true/false 评论
     * date：文章发表时间=本机的当前生成时间
     * top： 0 文章置顶，数值越大越在前
     * meta： 文章内容可引用网络图片链接地址
     * more： 显示 "阅读全文"
     *
     * @param files 文件目录
     */
    private static void isDirectory(File files) throws IOException {
        for (File file : files.listFiles()) {
            if (file.isDirectory()) {
                isDirectory(file);
            } else {
                String fileName = file.getName();

                if (fileName.endsWith("md")) {
                    //获取文件名称作为 文章名称
                    String articleName = fileName.substring(0, fileName.lastIndexOf("."));
                    //System.out.println("文章名称： " + articleName);

                    //获取本地当前时间作为 文章的发表时间
                    LocalDateTime time = LocalDateTime.now();
                    String localTime = df.format(time);
                    //System.out.println("发表时间： " + localTime);

                    //获取文件父级目录名称作为 文章的标签
                    File parentFile = file.getParentFile();
                    //System.out.println("文章标签: "+parentFile.getName());

                    //要插入的内容
                    String content = "---\n" +
                            "title: " + articleName + "\n" +
                            "tags:\n" +
                            "  - " + parentFile.getName() + "\n" +
                            "categories: ''\n" +
                            "resources: ''\n" +
                            "copyright: false\n" +
                            "comments: true\n" +
                            "date: " + localTime + "\n" +
                            "top:\n" +
                            "---\n" +
                            "<meta name=\"referrer\" content=\"no-referrer\"/>\n" +
                            "<!--more-->\n" +
                            " \n";

                    System.out.println(content);
                    insert(file, 0, content);
                    replacTextContent(file);
                }
            }
        }
    }

    /**
     * 向md文件中插入数据
     *
     * @param file          md文件
     * @param pos           指针 数据插入的位置
     * @param insertContent 数据内容
     */
    public static void insert(File file, long pos, String insertContent) throws IOException {
        File tmp = File.createTempFile("tmp", null);
        tmp.deleteOnExit();
        //使用临时文件保存插入点后的数据
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        FileOutputStream out = new FileOutputStream(tmp);
        FileInputStream in = new FileInputStream(tmp);
        raf.seek(pos);
        //----------下面代码将插入点后的内容读入临时文件中保存----------
        byte[] bbuf = new byte[64];
        //用于保存实际读取的字节数
        int hasRead = 0;
        //使用循环方式读取插入点后的数据
        while ((hasRead = raf.read(bbuf)) > 0) {
            //将读取的数据写入临时文件
            out.write(bbuf, 0, hasRead);
        }
        //-----------下面代码用于插入内容----------
        //把文件记录指针重写定位到pos位置
        raf.seek(pos);
        //追加需要插入的内容
        raf.write(insertContent.getBytes());
        //追加临时文件中的内容
        while ((hasRead = in.read(bbuf)) > 0) {
            raf.write(bbuf, 0, hasRead);
        }
    }

    /**
     * 替换文章中的 图片非法网络路径
     *
     * @param file
     * @throws IOException
     */
    public static void replacTextContent(File file) throws IOException {

        //原有的内容
        String srcStr = "\\?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240";
        //要替换的内容
        String replaceStr = "";

        String srcStr2 = "!\\[";
        String replaceStr2 = " ![";

        String srcStr3 = "\\[http\\:";
        String replaceStr3 = " [http:";

        String srcStr4 = "http\\:";
        String replaceStr4 = " http:";

        String srcStr5 = "\\，";
        String replaceStr5 = " ，";

        // 读
        FileReader in = new FileReader(file);
        BufferedReader bufIn = new BufferedReader(in);
        // 内存流, 作为临时流
        CharArrayWriter tempStream = new CharArrayWriter();
        // 替换
        String line = null;
        while ((line = bufIn.readLine()) != null) {
            // 替换每行中, 符合条件的字符串
            line = line.replaceAll(srcStr, replaceStr);
            line = line.replaceAll(srcStr2, replaceStr2);
            line = line.replaceAll(srcStr3, replaceStr3);
            line = line.replaceAll(srcStr4, replaceStr4);
            line = line.replaceAll(srcStr5, replaceStr5);

            // 将该行写入内存
            tempStream.write(line);
            // 添加换行符
            tempStream.append(System.getProperty("line.separator"));
        }
        // 关闭 输入流
        bufIn.close();
        // 将内存中的流 写入 文件
        FileWriter out = new FileWriter(file);
        tempStream.writeTo(out);
        out.close();
        System.out.println("fileName: " + file.getName());

    }
}
