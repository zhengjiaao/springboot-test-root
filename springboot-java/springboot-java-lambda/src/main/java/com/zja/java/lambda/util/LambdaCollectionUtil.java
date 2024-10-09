package com.zja.java.lambda.util;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * @Author: zhengja
 * @Date: 2024-10-09 9:49
 */
public class LambdaCollectionUtil {

    private LambdaCollectionUtil() {
    }

    // Collection 转化为 Map
    public static <T, K> Map<K, T> toMap(Collection<T> collection, Function<T, K> keyMapper) {
        if (collection == null) {
            throw new IllegalArgumentException("Collection cannot be null");
        }
        return collection.stream().collect(Collectors.toMap(keyMapper, t -> t));
    }

    public static <T, K, V> Map<K, V> toMap(Collection<T> collection, Function<T, K> keyMapper, Function<T, V> valueMapper) {
        if (collection == null) {
            throw new IllegalArgumentException("Collection cannot be null");
        }
        return collection.stream().collect(Collectors.toMap(keyMapper, valueMapper));
    }

    public static <T, K, V> Map<K, V> toMap(Collection<T> collection, Function<T, K> keyMapper, Function<T, V> valueMapper, Predicate<V> filter) {
        if (collection == null) {
            throw new IllegalArgumentException("Collection cannot be null");
        }
        return collection.stream()
                .collect(Collectors.toMap(keyMapper, valueMapper))
                .entrySet().stream()
                .filter(e -> filter.test(e.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static <T, K, V> Map<K, V> toMap(Collection<T> collection, Function<T, K> keyMapper, Function<T, V> valueMapper, BinaryOperator<V> mergeFunction) {
        if (collection == null) {
            throw new IllegalArgumentException("Collection cannot be null");
        }
        return collection.stream().collect(Collectors.toMap(keyMapper, valueMapper, mergeFunction));
    }

    // 转换 Map 的 Value
    public static <T, K, V> Map<K, V> mapValue(Map<K, T> map, Function<T, V> mapper) {
        if (map == null) {
            throw new IllegalArgumentException("Map cannot be null");
        }
        if (map.isEmpty()) {
            return new HashMap<>();
        }
        return map.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> mapper.apply(e.getValue())));
    }

    public static <T, K, V> Map<K, V> mapValue(Map<K, T> map, Function<T, V> mapper, BinaryOperator<V> mergeFunction) {
        if (map == null) {
            throw new IllegalArgumentException("Map cannot be null");
        }
        if (map.isEmpty()) {
            return new HashMap<>();
        }
        return map.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> mapper.apply(e.getValue()), mergeFunction));
    }

    public static <T, K, V> Map<K, V> mapValue(Map<K, T> map, Function<T, V> mapper, BinaryOperator<V> mergeFunction, Predicate<V> filter) {
        if (map == null) {
            throw new IllegalArgumentException("Map cannot be null");
        }
        if (map.isEmpty()) {
            return new HashMap<>();
        }
        return map.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> mapper.apply(e.getValue()), mergeFunction))
                .entrySet().stream()
                .filter(e -> filter.test(e.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    // List、Set 类型之间的转换
    public static <T, R> List<R> toList(Collection<T> collection, Function<T, R> mapper) {
        if (collection == null) {
            throw new IllegalArgumentException("Collection cannot be null");
        }
        return collection.stream().map(mapper).collect(Collectors.toList());
    }

    public static <T, R> Set<R> toSet(Collection<T> collection, Function<T, R> mapper) {
        if (collection == null) {
            throw new IllegalArgumentException("Collection cannot be null");
        }
        return collection.stream().map(mapper).collect(Collectors.toSet());
    }

    public static <T, R> List<R> toList(Collection<T> collection, Function<T, R> mapper, Predicate<R> filter) {
        if (collection == null) {
            throw new IllegalArgumentException("Collection cannot be null");
        }
        return collection.stream().map(mapper).filter(filter).collect(Collectors.toList());
    }

    // Collection 和 List、Set 的转化
    public static <T> Collection<T> toCollection(Collection<T> collection) {
        if (collection == null) {
            return new ArrayList<>();
        }
        return collection;
    }

    public static <T> List<T> toList(Collection<T> collection) {
        if (collection == null) {
            return new ArrayList<>();
        }
        if (collection instanceof List) {
            return (List<T>) collection;
        }
        return new ArrayList<>(collection);
    }

    public static <T> Set<T> toSet(Collection<T> collection) {
        if (collection == null) {
            return new HashSet<>();
        }
        if (collection instanceof Set) {
            return (Set<T>) collection;
        }
        return new HashSet<>(collection);
    }
}

