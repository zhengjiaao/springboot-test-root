/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 17:02
 * @Since:
 */
package com.zja.visitor.device;

/**
 * @author: zhengja
 * @since: 2023/10/09 17:02
 */
public class Client {
    public static void main(String[] args) {
        // 创建具体设备对象
        Phone phone = new Phone("iPhone 12");
        TV tv = new TV("Samsung QLED");
        Computer computer = new Computer("MacBook Pro");

        // 创建具体设备访问者对象
        DeviceVisitor deviceVisitor = new DeviceOperationVisitor();

        // 应用访问者操作
        phone.accept(deviceVisitor);
        tv.accept(deviceVisitor);
        computer.accept(deviceVisitor);

        //输出结果：
        //Turning on the phone: iPhone 12
        //Turning off the TV: Samsung QLED
        //Charging the computer: MacBook Pro
    }
}
