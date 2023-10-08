/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 16:32
 * @Since:
 */
package com.zja.bridge.tv;

/**
 * 在应用程序中动态地创建不同品牌的电视和遥控器，并将它们组合起来
 *
 * @author: zhengja
 * @since: 2023/10/08 16:32
 */
public class Client {
    public static void main(String[] args) {
        TV sonyTV = new SonyTV();
        TV samsungTV = new SamsungTV();

        Remote sonyRemote = new SonyRemote(sonyTV);
        Remote samsungRemote = new SamsungRemote(samsungTV);

        sonyRemote.power();
        sonyRemote.volumeUp();
        sonyRemote.nextChannel();

        samsungRemote.power();
        samsungRemote.volumeDown();
        samsungRemote.previousChannel();

        //输出结果：
        //Sony TV is powered on.
        //Sony TV volume is set to: 1
        //Sony TV switches to next channel.
        
        //Samsung TV is powered on.
        //Samsung TV volume is set to: -1
        //Samsung TV switches to previous channel.
    }

}
