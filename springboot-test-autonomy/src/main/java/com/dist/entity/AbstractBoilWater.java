package com.dist.entity;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/5/8 14:05
 */
public abstract class AbstractBoilWater {
    protected volatile boolean isReadyFlag = false;

    protected void a_addWater() {
        System.out.println("1.加水");
    }

    protected void b_on() {
        System.out.println("2.打开煤气");
    }

    protected void c_boiling() throws Exception {
        System.out.println("3-1.烧水中.....");
        Thread.sleep(5000); //模拟烧水的过程，比较耗时
        System.out.println("3-3.水开了");
        isReadyFlag = true;
    }

    protected void d_off() {
        System.out.println("4.关闭煤气");
    }

    //烧水方法
    public abstract void make() throws Exception;

    protected void playGame() throws Exception {
        Thread.sleep(1200);
        if (!isReadyFlag) {
            System.out.println("3-2,水还没烧开,玩一把游戏");
        }
    }
}
