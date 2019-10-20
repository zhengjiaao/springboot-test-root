package com.dist.utils;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/5/30 15:21
 */
@Component
@Lazy(true)
public class EJBGenerator implements IGenerator {

    @Autowired
    protected Mapper mapper;

    /**
     * @Description: 单个对象的深度复制及类型转换，vo/domain , po
     * @param s 数据对象
     * @param clz 复制目标类型
     * @param <T>
     * @param <S>
     * @return
     */
    @Override
    public <T, S> T convert(S s, Class<T> clz) {
        if (s == null) {
            return null;
        }
        return this.mapper.map(s, clz);
    }

    /**
     *@Description: list深度复制
     * @param s 数据对象
     * @param clz 复制目标类型
     * @param <T>
     * @param <S>
     * @return
     */
    @Override
    public <T, S> List<T> convert(List<S> s, Class<T> clz) {
        if (s == null) {
            return null;
        }
        List<T> list = new ArrayList<T>();
        for (S vs : s) {
            list.add(this.mapper.map(vs, clz));
        }
        return list;
    }

    /**
     * @Description: Set深度复制
     * @param s 数据对象
     * @param clz 复制目标类型
     * @param <T>
     * @param <S>
     * @return
     */
    @Override
    public <T, S> Set<T> convert(Set<S> s, Class<T> clz) {
        if (s == null) {
            return null;
        }
        Set<T> set = new HashSet<T>();
        for (S vs : s) {
            set.add(this.mapper.map(vs, clz));
        }
        return set;
    }

    /**
     * @Description: 数组深度复制
     * @param s 数据对象
     * @param clz 复制目标类型
     * @param <T>
     * @param <S>
     * @return
     */
    @Override
    public <T, S> T[] convert(S[] s, Class<T> clz) {
        if (s == null) {
            return null;
        }
        @SuppressWarnings("unchecked")
        T[] arr = (T[]) Array.newInstance(clz, s.length);
        for (int i = 0; i < s.length; i++) {
            arr[i] = this.mapper.map(s[i], clz);
        }
        return arr;
    }
}

