/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 16:30
 * @Since:
 */
package com.zja.bridge.tv;

/**
 * 品牌的电视类-三星电视（SamsungTV）
 *
 * @author: zhengja
 * @since: 2023/10/08 16:30
 */
public class SamsungTV extends TV {
    @Override
    public void powerOn() {
        System.out.println("Samsung TV is powered on.");
    }

    @Override
    public void powerOff() {
        System.out.println("Samsung TV is powered off.");
    }

    @Override
    public void setVolume(int volume) {
        System.out.println("Samsung TV volume is set to: " + volume);
    }

    @Override
    public void nextChannel() {
        System.out.println("Samsung TV switches to next channel.");
    }

    @Override
    public void previousChannel() {
        System.out.println("Samsung TV switches to previous channel.");
    }
}