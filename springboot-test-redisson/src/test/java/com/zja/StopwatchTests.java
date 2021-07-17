package com.zja;

import com.google.common.base.Stopwatch;

import java.util.concurrent.TimeUnit;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-06-21 13:49
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
public class StopwatchTests {

    /**
     * 计时工具类
     */
//    public static Stopwatch stopwatch = Stopwatch.createUnstarted();


    public static void main(String[] args) throws Exception{

        // 创建stopwatch并开始计时
        Stopwatch stopwatch = Stopwatch.createStarted();
        System.out.println("-- 开始计时 --");
        Thread.sleep(1950L);
        System.out.println(stopwatch);// 1.955 s
        // 向下取整 单位：秒
        System.out.println(stopwatch.elapsed(TimeUnit.SECONDS));// 1

        // 停止计时
        System.out.println("-- 停止计时 --");
        stopwatch.stop();
        Thread.sleep(2000L);
        System.out.println(stopwatch);// stop()不在计时 1.959 s
        System.out.println(stopwatch.elapsed(TimeUnit.SECONDS));// 1

        // 再次计时
        System.out.println("-- 再次计时 --");
        stopwatch.start();
        Thread.sleep(100L);
        System.out.println(stopwatch);// 2.067 s
        System.out.println(stopwatch.elapsed(TimeUnit.SECONDS));// 2

        // 重置并开始
        System.out.println("-- 重置并开始 --");
        stopwatch.reset().start();
        Thread.sleep(1500);
        System.out.println(stopwatch);// 1.505 s
        System.out.println(stopwatch.elapsed(TimeUnit.SECONDS));// 1

        // 检查isRunning
        System.out.println("-- 检查isRunning --");
        System.out.println(stopwatch.isRunning());// true

        // 打印
        System.out.println("-- 打印 --");
        System.out.println(stopwatch.toString());// 1.506 s

    }
}
