/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-02-21 15:52
 * @Since:
 */
package com.zja.juc.threadLocal;

public class ThreadLocalDemo {
    private static ThreadLocal<String> local = new ThreadLocal<String>();

    static void print(String str) {
        //打印当前线程中本地内存中变量的值
        System.out.println(str + " :" + local.get());
        //清除内存中的本地变量
        local.remove();
    }


    public static void main(String[] args) throws InterruptedException {

        new Thread(new Runnable() {
            @Override
            public void run() {
                ThreadLocalDemo.local.set("xdclass_A");
                print("A");
                //打印本地变量
                System.out.println("清除后：" + local.get());
            }
        },"A").start();
        Thread.sleep(1000);

        new Thread(new Runnable() {
            @Override
            public void run() {
                ThreadLocalDemo.local.set("xdclass_B");
                print("B");
                System.out.println("清除后 " + local.get());
            }
        },"B").start();
    }
}
