/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 16:26
 * @Since:
 */
package com.zja.bridge.tv;

/**
 * 电视（TV）的抽象类，其中包含基本的功能方法
 *
 * @author: zhengja
 * @since: 2023/10/08 16:26
 */
public abstract class TV {
    public abstract void powerOn(); // 电源接通

    public abstract void powerOff(); //电源关闭

    public abstract void setVolume(int volume); //设置音量

    public abstract void nextChannel(); // 下一个频道

    public abstract void previousChannel(); //上一个频道
}
