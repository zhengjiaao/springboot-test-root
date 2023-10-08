/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 16:29
 * @Since:
 */
package com.zja.bridge.tv;

/**
 * 品牌的电视类-索尼电视（SonyTV）
 *
 * @author: zhengja
 * @since: 2023/10/08 16:29
 */
public class SonyTV extends TV {
    @Override
    public void powerOn() {
        System.out.println("Sony TV is powered on.");
    }

    @Override
    public void powerOff() {
        System.out.println("Sony TV is powered off.");
    }

    @Override
    public void setVolume(int volume) {
        System.out.println("Sony TV volume is set to: " + volume);
    }

    @Override
    public void nextChannel() {
        System.out.println("Sony TV switches to next channel.");
    }

    @Override
    public void previousChannel() {
        System.out.println("Sony TV switches to previous channel.");
    }
}