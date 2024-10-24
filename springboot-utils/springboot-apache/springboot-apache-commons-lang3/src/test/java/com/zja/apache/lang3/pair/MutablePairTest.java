package com.zja.apache.lang3.pair;

import com.zja.apache.lang3.UserDO;
import org.apache.commons.lang3.tuple.MutablePair;
import org.junit.jupiter.api.Test;

/**
 * MutablePair是一个可变的。它允许在创建后动态修改键和值，提供了更大的灵活性。但是它是线程不安全的。
 *
 * @Author: zhengja
 * @Date: 2024-10-24 10:36
 */
public class MutablePairTest {

    @Test
    public void test_1() {
        MutablePair<String, String> pair = new MutablePair<>("key", "value");
        System.out.println(pair);
    }

    // 测试可变 Pair
    @Test
    public void test_2() {
        MutablePair<String, String> pair = new MutablePair<>("key", "value");
        System.out.println(pair);
        pair.setLeft("newKey");
        pair.setRight("newValue");
        System.out.println(pair);

        UserDO userDO = new UserDO();
        userDO.setId("123456");
        userDO.setName("小芳");
        userDO.setAge(18);

        MutablePair<String, UserDO> mutablePair = handleUserInfo1(userDO);
        System.out.println("mutablePair改变前的值：" + mutablePair);
        mutablePair.setLeft("王小芳");
        mutablePair.getRight().setAge(20);
        System.out.println("mutablePair改变后的值：" + mutablePair);
    }

    /**
     * 返回可变Pair
     */
    private static MutablePair<String, UserDO> handleUserInfo1(UserDO userDO) {
        return MutablePair.of(userDO.getName(), userDO);
    }
}
