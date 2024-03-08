package com.zja.java.optional;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author: zhengja
 * @since: 2024/03/08 16:18
 */
public class OptionalMapExample {

    // 从Map中获取特定键的值，如果值存在则返回Optional，否则返回空的Optional
    @Test
    public void test_1() {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        Optional<String> optionalValue = Optional.ofNullable(map.get("key1"));
        optionalValue.ifPresent(value -> System.out.println("Value: " + value));

        // or 获取Map中的值并执行操作，如果值不存在则返回默认值

        Map<String, Integer> map2 = new HashMap<>();
        map2.put("key1", 10);
        map2.put("key2", 20);

        int value = Optional.ofNullable(map2.get("key3")).orElse(0);
        System.out.println("Value: " + value);
    }

    // 使用Optional处理可能为空的Map值，并执行相应操作
    // 通过键获取Map中的值，并在值存在时进行转换操作
    @Test
    public void test_2() {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "10");
        map.put("key2", "20");

        int value = Optional.ofNullable(map.get("key1"))
                .map(Integer::parseInt)
                .orElse(0);
        System.out.println("Value: " + value);
    }

    // 将Optional与Map结合使用，获取特定键的值，如果值存在则进行转换操作，否则返回默认值
    @Test
    public void test_3() {
        Map<String, Integer> map = new HashMap<>();
        map.put("key1", 10);
        map.put("key2", null);

        Optional<Integer> optionalValue = Optional.ofNullable(map.get("key2"));
        int transformedValue = optionalValue.map(value -> value * 2)
                .orElse(0);
        System.out.println("Transformed value: " + transformedValue);
    }

    // 使用Optional处理嵌套的Map结构，从外部Map获取内部Map的值
    @Test
    public void test_4() {
        Map<String, Map<String, String>> outerMap = new HashMap<>();
        Map<String, String> innerMap = new HashMap<>();
        innerMap.put("innerKey", "innerValue");
        outerMap.put("outerKey", innerMap);

        Optional<String> optionalValue = Optional.ofNullable(outerMap.get("outerKey"))
                .flatMap(inner -> Optional.ofNullable(inner.get("innerKey")));
        optionalValue.ifPresent(value -> System.out.println("Value: " + value));
    }

    // 使用Optional处理嵌套的Map结构，链式获取多个键的值
    @Test
    public void test_5() {
        Map<String, Map<String, String>> outerMap = new HashMap<>();
        Map<String, String> innerMap = new HashMap<>();
        innerMap.put("innerKey", "innerValue");
        outerMap.put("outerKey", innerMap);

        String value = Optional.ofNullable(outerMap.get("outerKey"))
                .flatMap(inner -> Optional.ofNullable(inner.get("innerKey")))
                .flatMap(nested -> Optional.ofNullable(nested.toUpperCase()))
                .orElse("Default Value");
        System.out.println("Value: " + value);
    }

    // 当使用Optional结合Map处理多个键的值时，可以使用flatMap方法链式获取每个键的值，并在最后执行相应操作
    @Test
    public void test_6() {
        Map<String, Map<String, String>> dataMap = new HashMap<>();

        Map<String, String> innerMap = new HashMap<>();
        innerMap.put("key1", "value1");
        innerMap.put("key2", "value2");

        dataMap.put("outerKey", innerMap);

        String value = Optional.ofNullable(dataMap)
                .flatMap(map -> Optional.ofNullable(map.get("outerKey")))
                .flatMap(innerMap2 -> Optional.ofNullable(innerMap2.get("key2")))
                .orElse("Default Value");

        System.out.println("Value: " + value);
    }
}
