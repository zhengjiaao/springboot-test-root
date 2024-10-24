package com.zja.apache.lang3.pair;

import com.zja.apache.lang3.UserDO;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.Test;

/**
 * ImmutablePair 是不可变的，并且是线程安全的
 * <p>
 * 注：ImmutablePair 它的左值以及右值都是final的，不可更改的。并且调用它的setValue会抛出UnsupportedOperationException。
 *
 * @Author: zhengja
 * @Date: 2024-10-24 10:37
 */
public class ImmutablePairTest {

    @Test
    public void test_1() {
        ImmutablePair<String, String> pair = ImmutablePair.of("key", "value");
        // ImmutablePair<Integer, String> pair = ImmutablePair.of(123, "value");
        System.out.println(pair);
        System.out.println(pair.getLeft() + "," + pair.getRight());
    }

    // 测试不可变Pair
    @Test
    public void test_2() {
        UserDO userDO = new UserDO();
        userDO.setId("123456");
        userDO.setName("小芳");
        userDO.setAge(18);

        ImmutablePair<String, UserDO> immutablePair = handleUserInfo2(userDO);
        System.out.println(" ImmutablePair改变前的值：" + immutablePair);
        UserDO userFromTriple = immutablePair.right;
        userFromTriple.setAge(20); //  ImmutablePair 本身是不可变的,但存储对象是可变的，则整个三元组就变得可变。
        System.out.println(" ImmutablePair改Right键值对象的值：" + immutablePair);
        // 因 ImmutablePair 不可变 final标记，无法通过set方法修改键值。
    }

    /**
     * 返回不可变Triple
     */
    private static ImmutablePair<String, UserDO> handleUserInfo2(UserDO userDO) {
        return ImmutablePair.of(userDO.getName(), userDO);
    }
}
