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
public interface DeviceVisitor {
    void visit(Phone phone);
    void visit(TV tv);
    void visit(Computer computer);
    // 添加其他设备类型的访问方法
}