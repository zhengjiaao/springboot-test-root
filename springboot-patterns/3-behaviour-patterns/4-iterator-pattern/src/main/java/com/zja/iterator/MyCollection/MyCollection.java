/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 14:02
 * @Since:
 */
package com.zja.iterator.MyCollection;

import java.util.Iterator;

/**
 * @author: zhengja
 * @since: 2023/10/09 14:02
 */
// 自定义集合类
class MyCollection implements Iterable<String> {
    private String[] elements; //存储集合元素
    private int size; //当前元素的数量

    public MyCollection(int capacity) {
        elements = new String[capacity];
        size = 0;
    }

    public void add(String element) {
        if (size < elements.length) {
            elements[size] = element;
            size++;
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new MyIterator();
    }

    // 内部迭代器类
    private class MyIterator implements Iterator<String> {
        private int currentIndex; //currentIndex变量来跟踪当前元素的索引

        public MyIterator() {
            currentIndex = 0;
        }

        @Override
        public boolean hasNext() { //hasNext()方法检查是否还有下一个元素
            return currentIndex < size;
        }

        @Override
        public String next() { //next()方法返回下一个元素并将索引指向下一个位置。
            String element = elements[currentIndex];
            currentIndex++;
            return element;
        }
    }
}