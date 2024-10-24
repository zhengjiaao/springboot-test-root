package com.zja.apache.lang3.triple;

import com.zja.apache.lang3.UserDO;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.junit.jupiter.api.Test;

/**
 * ImmutableTriple 是一个不可变的三元组类，由三个泛型元素（left、middle、right）组成。
 * <p>
 * 注：ImmutableTriple 不可变意味着一旦创建，其状态无法修改。该类被设计为线程安全的，但需要注意，如果存储在三元组中的对象是可变的，那么三元组本身实际上就不再是不可变的。
 *
 * @Author: zhengja
 * @Date: 2024-10-24 10:30
 */
public class ImmutableTripleTest {

    @Test
    public void test_1() {
        ImmutableTriple<String, String, String> triple = new ImmutableTriple<>("a", "b", "c");
        System.out.println(triple);
    }

    // 测试不可变Triple
    @Test
    public void test_2() {
        UserDO userDO = new UserDO();
        userDO.setId("123456");
        userDO.setName("小芳");
        userDO.setAge(18);

        ImmutableTriple<String, Integer, UserDO> immutableTriple = handleUserInfo2(userDO);
        System.out.println("ImmutableTriple改变前的值：" + immutableTriple);
        UserDO userFromTriple = immutableTriple.right;
        userFromTriple.setAge(20); // ImmutableTriple 本身是不可变的,但存储对象是可变的，则整个三元组就变得可变。
        System.out.println("ImmutableTriple改Right键值对象的值：" + immutableTriple);
        // 因ImmutableTriple 不可变 final标记，无法通过set方法修改键值。
    }

    /**
     * 返回不可变Triple
     */
    private static ImmutableTriple<String, Integer, UserDO> handleUserInfo2(UserDO userDO) {
        return ImmutableTriple.of(userDO.getName(), userDO.getAge(), userDO);
    }

}
