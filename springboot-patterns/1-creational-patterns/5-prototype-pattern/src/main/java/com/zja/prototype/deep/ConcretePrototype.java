/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 11:25
 * @Since:
 */
package com.zja.prototype.deep;

import java.io.*;

/**
 * @author: zhengja
 * @since: 2023/10/08 11:25
 */
class ConcretePrototype implements Serializable {
    private int value;
    private ReferenceType reference;

    public ConcretePrototype(int value, ReferenceType reference) {
        this.value = value;
        this.reference = reference;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setReference(ReferenceType reference) {
        this.reference = reference;
    }

    public ReferenceType getReference() {
        return reference;
    }

    public ConcretePrototype deepClone() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(this);

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        return (ConcretePrototype) ois.readObject();
    }
}
