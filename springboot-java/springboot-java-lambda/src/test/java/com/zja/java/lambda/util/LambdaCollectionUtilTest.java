package com.zja.java.lambda.util;

import java.util.*;
import java.util.function.BinaryOperator;

import org.junit.jupiter.api.Test;

import java.util.function.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author: zhengja
 * @Date: 2024-10-09 10:13
 */
public class LambdaCollectionUtilTest {

    @Test
    void testToMap() {
        // 测试将 Collection 转换为 Map
        Collection<String> collection = Arrays.asList("a", "b", "c");
        Function<String, String> keyMapper = String::toUpperCase;

        Map<String, String> result = LambdaCollectionUtil.toMap(collection, keyMapper);
        assertEquals(3, result.size());
        assertTrue(result.containsKey("A"));
        assertTrue(result.containsKey("B"));
        assertTrue(result.containsKey("C"));
        assertEquals("a", result.get("A"));
        assertEquals("b", result.get("B"));
        assertEquals("c", result.get("C"));
    }

    @Test
    void testToMapWithValues() {
        // 测试将 Collection 转换为 Map 并指定值映射器
        Collection<String> collection = Arrays.asList("a", "b", "c");
        Function<String, String> keyMapper = String::toUpperCase;
        Function<String, Integer> valueMapper = String::length;

        Map<String, Integer> result = LambdaCollectionUtil.toMap(collection, keyMapper, valueMapper);
        assertEquals(3, result.size());
        assertEquals(1, result.get("A"));
        assertEquals(1, result.get("B"));
        assertEquals(1, result.get("C"));
    }

    @Test
    void testToMapWithFilter() {
        // 测试将 Collection 转换为 Map 并过滤值
        Collection<String> collection = Arrays.asList("a", "b", "c");
        Function<String, String> keyMapper = String::toUpperCase;
        Function<String, Integer> valueMapper = String::length;
        Predicate<Integer> filter = x -> x > 0;

        Map<String, Integer> result = LambdaCollectionUtil.toMap(collection, keyMapper, valueMapper, filter);
        assertEquals(3, result.size());
        assertEquals(1, result.get("A"));
        assertEquals(1, result.get("B"));
        assertEquals(1, result.get("C"));
    }

    @Test
    void testToMapWithMergeFunction() {
        // 测试将 Collection 转换为 Map 并指定合并函数
        Collection<String> collection = Arrays.asList("a", "b", "c", "a");
        Function<String, String> keyMapper = String::toUpperCase;
        Function<String, Integer> valueMapper = String::length;
        BinaryOperator<Integer> mergeFunction = Integer::sum;

        Map<String, Integer> result = LambdaCollectionUtil.toMap(collection, keyMapper, valueMapper, mergeFunction);
        assertEquals(3, result.size());
        assertEquals(2, result.get("A")); // "a" 和 "a" 合并
        assertEquals(1, result.get("B"));
        assertEquals(1, result.get("C"));
    }

    @Test
    void testMapValue() {
        // 测试将 Map 的值转换
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        Function<String, Integer> mapper = String::length;

        Map<String, Integer> result = LambdaCollectionUtil.mapValue(map, mapper);
        assertEquals(2, result.size());
        assertEquals(6, result.get("key1"));
        assertEquals(6, result.get("key2"));
    }

    @Test
    void testMapValueWithMergeFunction() {
        // 测试将 Map 的值转换并指定合并函数
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        Function<String, Integer> mapper = String::length;
        BinaryOperator<Integer> mergeFunction = Integer::sum;

        Map<String, Integer> result = LambdaCollectionUtil.mapValue(map, mapper, mergeFunction);
        assertEquals(2, result.size());
        assertEquals(6, result.get("key1"));
        assertEquals(6, result.get("key2"));
    }

    @Test
    void testMapValueWithFilter() {
        // 测试将 Map 的值转换并过滤
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        Function<String, Integer> mapper = String::length;
        Predicate<Integer> filter = x -> x > 5;

        Map<String, Integer> result = LambdaCollectionUtil.mapValue(map, mapper, (a, b) -> a, filter);
        assertEquals(2, result.size());
        assertEquals(6, result.get("key1"));
        assertEquals(6, result.get("key2"));
    }

    @Test
    void testToList() {
        // 测试将 Collection 转换为 List
        Collection<String> collection = Arrays.asList("a", "b", "c");
        Function<String, Integer> mapper = String::length;

        List<Integer> result = LambdaCollectionUtil.toList(collection, mapper);
        assertEquals(3, result.size());
        assertEquals(1, result.get(0));
        assertEquals(1, result.get(1));
        assertEquals(1, result.get(2));
    }

    @Test
    void testToSet() {
        // 测试将 Collection 转换为 Set
        Collection<String> collection = Arrays.asList("a", "b", "c");
        Function<String, Integer> mapper = String::length;

        Set<Integer> result = LambdaCollectionUtil.toSet(collection, mapper);
        assertEquals(1, result.size());
        assertTrue(result.contains(1));
    }

    @Test
    void testToListWithFilter() {
        // 测试将 Collection 转换为 List 并过滤
        Collection<String> collection = Arrays.asList("a", "b", "c");
        Function<String, Integer> mapper = String::length;
        Predicate<Integer> filter = x -> x > 0;

        List<Integer> result = LambdaCollectionUtil.toList(collection, mapper, filter);
        assertEquals(3, result.size());
        assertEquals(1, result.get(0));
        assertEquals(1, result.get(1));
        assertEquals(1, result.get(2));
    }

    @Test
    void testToCollection() {
        // 测试将 Collection 转换为 Collection
        Collection<String> collection = Arrays.asList("a", "b", "c");

        Collection<String> result = LambdaCollectionUtil.toCollection(collection);
        assertEquals(3, result.size());
        assertTrue(result.contains("a"));
        assertTrue(result.contains("b"));
        assertTrue(result.contains("c"));
    }

    @Test
    void testToListFromCollection() {
        // 测试将 Collection 转换为 List
        Collection<String> collection = Arrays.asList("a", "b", "c");

        List<String> result = LambdaCollectionUtil.toList(collection);
        assertEquals(3, result.size());
        assertEquals("a", result.get(0));
        assertEquals("b", result.get(1));
        assertEquals("c", result.get(2));
    }

    @Test
    void testToSetFromCollection() {
        // 测试将 Collection 转换为 Set
        Collection<String> collection = Arrays.asList("a", "b", "c");

        Set<String> result = LambdaCollectionUtil.toSet(collection);
        assertEquals(3, result.size());
        assertTrue(result.contains("a"));
        assertTrue(result.contains("b"));
        assertTrue(result.contains("c"));
    }

}
