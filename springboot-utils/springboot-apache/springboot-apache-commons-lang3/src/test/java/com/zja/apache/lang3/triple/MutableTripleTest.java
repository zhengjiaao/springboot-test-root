package com.zja.apache.lang3.triple;

import com.zja.apache.lang3.UserDO;
import org.apache.commons.lang3.tuple.MutableTriple;
import org.junit.jupiter.api.Test;

import java.util.Date;

/**
 * MutableTriple 是可变的，原因在于它提供了公共的设置（set）方法，允许在创建后修改其内部值。
 * <p>
 * MutableTriple 被明确标记为非线程安全，如果其中的元素对象都是线程安全的，则是线程安全的。
 * MutableTriple：可变三元对象组，里面有 left、middle、right 三个元素，用于封装三个对象。
 * MutableTriple 是可变的，构建对象后，可以再 setXxx 修改值
 *
 * @Author: zhengja
 * @Date: 2024-10-16 9:47
 */
public class MutableTripleTest {

    @Test
    public void test_1() {
        // 同时封装 3 个对象
        MutableTriple<Integer, String, Date> mutableTriple = MutableTriple.of(212, "蚩尤后裔", new Date());
        // 212,蚩尤后裔,Sun Jun 13 09:27:37 CST 2021
        System.out.println(mutableTriple);

        // 800,蚩尤后裔,Sun Jun 13 09:27:37 CST 2021
        mutableTriple.setLeft(800);
        System.out.println(mutableTriple.getLeft() + "," + mutableTriple.getMiddle() + "," + mutableTriple.getRight());
    }


    // 测试可变Truple
    @Test
    public void test_2() {
        UserDO userDO = new UserDO();
        userDO.setId("123456");
        userDO.setName("小芳");
        userDO.setAge(18);

        MutableTriple<String, Integer, UserDO> mutableTriple = handleUserInfo1(userDO);
        System.out.println("mutableTriple改变前的值：" + mutableTriple);
        mutableTriple.setMiddle(20);
        System.out.println("mutableTriple改变后的值：" + mutableTriple);
    }

    /**
     * 返回可变Truple
     */
    private static MutableTriple<String, Integer, UserDO> handleUserInfo1(UserDO userDO) {
        return MutableTriple.of(userDO.getName(), userDO.getAge(), userDO);
    }

}
