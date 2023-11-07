/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-07 10:38
 * @Since:
 */
package com.zja.collections;

import com.google.common.collect.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author: zhengja
 * @since: 2023/11/07 10:38
 */
public class CollectionsUnitTest {

    @Test
    public void testCreateList() {
        // 创建不可变列表
        List<Integer> immutableList = ImmutableList.of(1, 2, 3);

        // 创建可变列表
        List<String> mutableList = Lists.newArrayList("a", "b", "c");

        // 验证列表内容和长度
        assertEquals(Lists.newArrayList(1, 2, 3), immutableList);
        assertEquals(Lists.newArrayList("a", "b", "c"), mutableList);
    }

    @Test
    public void testCreateSet() {
        // 创建不可变集合
        Set<Integer> immutableSet = ImmutableSet.of(1, 2, 3);

        // 创建可变集合
        Set<String> mutableSet = Sets.newHashSet("a", "b", "c");

        // 验证集合内容和大小
        assertEquals(Sets.newHashSet(1, 2, 3), immutableSet);
        assertEquals(Sets.newHashSet("a", "b", "c"), mutableSet);
    }

    @Test
    public void testCreateMap() {
        // 创建不可变映射
        Map<String, Integer> immutableMap = ImmutableMap.of("one", 1, "two", 2, "three", 3);

        // 创建可变映射
        Map<Integer, String> mutableMap = Maps.newHashMap();
        mutableMap.put(1, "one");
        mutableMap.put(2, "two");
        mutableMap.put(3, "three");

        // 验证映射内容和大小
        assertEquals(ImmutableMap.of("one", 1, "two", 2, "three", 3), immutableMap);
        assertEquals(ImmutableMap.of(1, "one", 2, "two", 3, "three"), mutableMap);
    }

    @Test
    public void testCollectionOperations() {
        // 创建列表
        List<Integer> numbers1 = Lists.newArrayList(1, 2, 3, 4, 5);
        List<Integer> numbers2 = Lists.newArrayList(4, 5, 6, 7, 8);

        // 列表的并集
        List<Integer> union = Lists.newArrayList(Sets.union(Sets.newHashSet(numbers1), Sets.newHashSet(numbers2)));

        // 验证并集的结果：要验证列表并集的结果，可以使用 assertEquals 方法结合 Lists.newArrayList() 来比较两个列表。虽然列表的顺序是不确定的，但我们可以使用 containsAll 方法来确保两个列表包含相同的元素。
        assertTrue(union.containsAll(numbers1));
        assertTrue(union.containsAll(numbers2));
    }

    @Test
    public void testCollectionOperations2() {
        // 创建列表和集合
        List<Integer> numbers = Lists.newArrayList(1, 2, 3, 4, 5);
        Set<Integer> numberSet = Sets.newHashSet(4, 5, 6, 7, 8);

        // 列表和集合的交集
        List<Integer> intersection = Lists.newArrayList(Sets.intersection(Sets.newHashSet(numbers), numberSet));

        // 验证交集的结果
        assertEquals(Lists.newArrayList(4, 5), intersection);

        // 列表和集合的并集
        List<Integer> union = Lists.newArrayList(Sets.union(Sets.newHashSet(numbers), numberSet));

        // 验证并集的结果
        assertTrue(union.containsAll(numbers));
        assertTrue(union.containsAll(numberSet));
    }

    @Test
    @Deprecated
    public void testCollectionOperations3() {
        // 创建列表和集合
        List<Integer> numbers = Lists.newArrayList(1, 2, 3, 4, 5);
        Set<Integer> numberSet = Sets.newHashSet(4, 5, 6, 7, 8);

        // 列表和集合的交集
        List<Integer> intersection = Lists.newArrayList(Sets.intersection(Sets.newHashSet(numbers), numberSet));

        // 列表和集合的并集
        List<Integer> union = Lists.newArrayList(Sets.union(Sets.newHashSet(numbers), numberSet));

        // 验证交集和并集的结果
        assertEquals(Lists.newArrayList(4, 5), intersection);

        // todo 错误的验证：在集合的并集操作中，元素的顺序是不确定的，因为集合是无序的数据结构。因此，使用 assertEquals 来验证并集的顺序是不可靠的。
        assertEquals(Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8), union);
    }
}
