/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 13:19
 * @Since:
 */
package com.zja.template.beverage;

/**
 * @author: zhengja
 * @since: 2023/10/09 13:19
 */
// 抽象类
abstract class Beverage {
    // 模板方法，定义了饮料的制作过程
    public final void prepareBeverage() {
        boilWater();
        brew();
        pourIntoCup();
        addCondiments();
    }

    // 把水煮沸
    private void boilWater() {
        System.out.println("Boiling water...");
    }

    // 冲泡饮料
    abstract void brew();

    // 把饮料倒入杯子
    private void pourIntoCup() {
        System.out.println("Pouring into cup...");
    }

    // 添加调料
    abstract void addCondiments();
}