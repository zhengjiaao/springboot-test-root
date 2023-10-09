/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 14:21
 * @Since:
 */
package com.zja.command.light;

/**
 * @author: zhengja
 * @since: 2023/10/09 14:21
 */
// Receiver（接收者）
class Light {
    public void turnOn() {
        System.out.println("Light is on");
    }

    public void turnOff() {
        System.out.println("Light is off");
    }
}