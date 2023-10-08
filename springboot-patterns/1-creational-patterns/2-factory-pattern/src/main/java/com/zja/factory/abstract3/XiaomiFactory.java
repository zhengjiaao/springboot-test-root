/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 9:37
 * @Since:
 */
package com.zja.factory.abstract3;

/**
 * 实现具体的工厂类
 *
 * @author: zhengja
 * @since: 2023/10/08 9:37
 */
// 小米工厂
class XiaomiFactory implements DeviceFactory {
    @Override
    public Screen createScreen() {
        return new XiaomiScreen();
    }

    @Override
    public Processor createProcessor() {
        return new XiaomiProcessor();
    }
}