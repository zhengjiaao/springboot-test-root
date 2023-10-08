/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 16:54
 * @Since:
 */
package com.zja.composite.file;

/**
 * @author: zhengja
 * @since: 2023/10/08 16:54
 */
public class Client {
    public static void main(String[] args) {
        // 创建文件
        FileSystemComponent file1 = new File("file1.txt");
        FileSystemComponent file2 = new File("file2.txt");
        FileSystemComponent file3 = new File("file3.txt");

        // 创建目录
        Directory dir1 = new Directory("dir1");
        Directory dir2 = new Directory("dir2");
        Directory dir3 = new Directory("dir3");

        // 组合文件和目录
        dir1.add(file1);
        dir1.add(dir2);
        dir2.add(file2);
        dir2.add(dir3);
        dir3.add(file3);

        // 显示文件系统层次结构
        dir1.display();

        //输出结果：
        //Directory: dir1
        //File: file1.txt
        //Directory: dir2
        //File: file2.txt
        //Directory: dir3
        //File: file3.txt
    }
}
