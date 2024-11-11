package com.zja.hanbian.封装.数据结构;

import java.util.HashMap;

/**
 * @Author: zhengja
 * @Date: 2024-09-29 9:19
 */
public class 哈希字典<K, V> extends HashMap<K, V> implements 字典<K, V> {

    public static <K, V> 字典<K, V> 新建() {
        return new 哈希字典<>();
    }

    @Override
    public boolean 包含密钥(Object 键) {
        return super.containsKey(键);
    }

    @Override
    public boolean 含密钥(Object 键) {
        return super.containsKey(键);
    }

    @Override
    public V 获取(Object 键) {
        return super.get(键);
    }

    @Override
    public V 添加(K 键, V 值) {
        return super.put(键, 值);
    }

    @Override
    public V 删除(Object 键) {
        return super.remove(键);
    }

    @Override
    public void 删除所有() {
        super.clear();
    }
}
