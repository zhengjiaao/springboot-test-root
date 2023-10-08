/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 11:23
 * @Since:
 */
package com.zja.prototype.shallow;

/**
 * @author: zhengja
 * @since: 2023/10/08 11:23
 */
// 具体原型类
class ConcretePrototype implements Prototype {
    private int value;

    public ConcretePrototype(int value) {
        this.value = value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public Prototype clone() {
        try {
            return (Prototype) super.clone();
        } catch (CloneNotSupportedException e) {
            // 处理异常
            return null;
        }
    }
}
