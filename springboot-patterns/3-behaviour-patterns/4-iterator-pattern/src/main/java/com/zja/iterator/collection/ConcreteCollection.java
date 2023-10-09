/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 13:41
 * @Since:
 */
package com.zja.iterator.collection;

/**
 * @author: zhengja
 * @since: 2023/10/09 13:41
 */
// 具体集合
class ConcreteCollection implements Collection {
    private String[] elements;

    public ConcreteCollection(String[] elements) {
        this.elements = elements;
    }

    @Override
    public Iterator createIterator() {
        return new ConcreteIterator(elements);
    }
}