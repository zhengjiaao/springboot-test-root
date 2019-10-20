package com.dist.javabasis;

import org.junit.Test;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/7/26 10:13
 */
public class StringTest {
    @Test
    public void test1(){
        String Str = new String("www.google.com");
        //regex -- 匹配此字符串的正则表达式。
        //replacement -- 用来替换每个匹配项的字符串

        //成功则返回替换的字符串，失败则返回原始字符串。
        System.out.print("匹配成功返回值 :" );
        System.out.println(Str.replaceAll("(.*)google(.*)", "runoob" ));
        System.out.print("匹配失败返回值 :" );
        System.out.println(Str.replaceAll("(.*)taobao(.*)", "runoob" ));


        String str1 = new String("<p><img src='http://baidu.com/132456.jpg'></img></p>");
        System.out.println("匹配成功则返回替换的字符串："+str1.replaceAll("http://baidu.com/","http://gitlab.cn/"));
        System.out.println("匹配失败则返回原始字符串："+str1.replaceAll("http://asdtasd.re/","http://gitlab.cn/"));
    }
}
