package com.zja.juc.shangguigu1.sync;

//第一步  创建资源类，定义属性和和操作方法
class Ticket {
    //票数
    private int number = 30;
    //操作方法：卖票
    public synchronized void sale() {
        //判断：是否有票
        if(number > 0) {
            System.out.println(Thread.currentThread().getName()+" : 卖出："+(number--)+" 剩下："+number);
        }
    }
}

public class SaleTicket {
    //第二步 创建多个线程，调用资源类的操作方法
    public static void main(String[] args) {
        //创建Ticket对象
        Ticket ticket = new Ticket();
        //创建三个线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                //调用卖票方法
                for (int i = 0; i < 40; i++) {
                    ticket.sale();
                }
            }
        },"AA").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                //调用卖票方法
                for (int i = 0; i < 40; i++) {
                    ticket.sale();
                }
            }
        },"BB").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                //调用卖票方法
                for (int i = 0; i < 40; i++) {
                    ticket.sale();
                }
            }
        },"CC").start();
    }
}
