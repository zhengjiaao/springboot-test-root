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
// 具体迭代器
class ConcreteIterator implements Iterator {
    private String[] elements;
    private int position = 0;

    public ConcreteIterator(String[] elements) {
        this.elements = elements;
    }

    @Override
    public boolean hasNext() {
        return position < elements.length;
    }

    @Override
    public Object next() {
        if (hasNext()) {
            String element = elements[position];
            position++;
            return element;
        }
        return null;
    }
}