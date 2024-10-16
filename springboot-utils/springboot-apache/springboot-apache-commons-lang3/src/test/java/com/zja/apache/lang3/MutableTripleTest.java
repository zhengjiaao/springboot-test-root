package com.zja.apache.lang3;

import org.apache.commons.lang3.tuple.MutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.util.Date;

/**
 * Pair对象对 与 Triple 三元对象组： Pair 可以同时封装两个对象，Triple 可以同时封装三个对象。
 *
 * @Author: zhengja
 * @Date: 2024-10-16 9:47
 */
public class MutableTripleTest {

    // Pair 可以同时封装两个对象
    @Test
    public void Pair_of_test() {
        // 同时封装 32个对象
        Pair<Integer, String> pair = Pair.of(212, "蚩尤后裔");
        // 212,蚩尤后裔
        System.out.println(pair.getLeft() + "," + pair.getRight());
    }

    /**
     * MutableTriple：可变三元对象组，里面有 left、middle、right 三个元素，用于封装三个对象。
     * MutableTriple 是可变的，构建对象后，可以再 setXxx 修改值
     * 如果其中的元素对象都是线程安全的，则是线程安全的。
     */
    @Test
    public void MutableTriple_test() {
        // 同时封装 3 个对象
        MutableTriple<Integer, String, Date> mutableTriple = MutableTriple.of(212, "蚩尤后裔", new Date());
        mutableTriple.setLeft(800);
        // 800,蚩尤后裔,Sun Jun 13 09:27:37 CST 2021
        System.out.println(mutableTriple.getLeft() + "," + mutableTriple.getMiddle() + "," + mutableTriple.getRight());
    }
}
