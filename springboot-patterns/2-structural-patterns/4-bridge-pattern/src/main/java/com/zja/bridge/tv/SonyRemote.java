/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 16:31
 * @Since:
 */
package com.zja.bridge.tv;

/**
 * 实现具体的遥控器类-索尼遥控器
 *
 * @author: zhengja
 * @since: 2023/10/08 16:31
 */
public class SonyRemote extends Remote {
    public SonyRemote(TV tv) {
        super(tv);
    }

    @Override
    public void power() {
        tv.powerOn();
    }

    @Override
    public void volumeUp() {
        int volume = getCurrentVolume();
        tv.setVolume(volume + 1);
    }

    @Override
    public void volumeDown() {
        int volume = getCurrentVolume();
        tv.setVolume(volume - 1);
    }

    @Override
    public void nextChannel() {
        tv.nextChannel();
    }

    @Override
    public void previousChannel() {
        tv.previousChannel();
    }

    private int getCurrentVolume() {
        // 从遥控器中获取当前音量
        return 0;
    }
}
