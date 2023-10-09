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
public class DeviceOperationVisitor implements DeviceVisitor {
    @Override
    public void visit(Phone phone) {
        System.out.println("Turning on the phone: " + phone.getModel());
    }

    @Override
    public void visit(TV tv) {
        System.out.println("Turning off the TV: " + tv.getModel());
    }

    @Override
    public void visit(Computer computer) {
        System.out.println("Charging the computer: " + computer.getModel());
    }

    // 添加其他设备类型的访问方法的具体实现
}
