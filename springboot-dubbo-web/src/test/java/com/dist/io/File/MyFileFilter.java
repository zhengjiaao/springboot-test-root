package com.dist.io.File;

import java.io.File;
import java.io.FileFilter;

/**
 * 自定义文件过滤器
 * @program: jms-spring
 * @Date: 2018/12/5 12:17
 * @Author: Mr.Zheng
 * @Description:
 */
public class MyFileFilter implements FileFilter {
    /**
     *文件的后缀结尾是.txt
     * @param pathname 接受到的是文件的全路径
     * @return 对路径进行判断,如果是txt文件,返回true,不是txt文件,返回false
     */
    public boolean accept(File pathname) {
        return pathname.getName().endsWith(".txt");
    }
}
