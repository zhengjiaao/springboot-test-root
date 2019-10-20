package com.dist.java8;

import java.util.ArrayList;
import java.util.List;

/**方法引用实例
 * @author zhengja@dist.com.cn
 * @data 2019/8/6 16:43
 */
public class Java8Supplier {
    public static void main(String args[]){
        List names = new ArrayList();

        names.add("Google");
        names.add("Runoob");
        names.add("Taobao");
        names.add("Baidu");
        names.add("Sina");

        names.forEach(System.out::println);
    }
}
