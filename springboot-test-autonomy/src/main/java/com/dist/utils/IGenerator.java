package com.dist.utils;


import java.util.List;
import java.util.Set;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/5/30 15:19
 */
public interface IGenerator {

    /**
     * @Description: 单个对象的深度复制及类型转换，vo/domain , po
     * @param s 数据对象
     * @param clz 复制目标类型
     * @return
     * @author banjuer@outlook.com
     * @Time 2018年5月9日 下午3:53:24
     */
    <T, S> T convert(S s, Class<T> clz);

    /**
     * @Description: list深度复制
     * @param s 数据对象
     * @param clz 复制目标类型
     * @return
     * @author banjuer@outlook.com
     * @Time 2018年5月9日 下午3:54:08
     */
    <T, S> List<T> convert(List<S> s, Class<T> clz);

    /**
     * @Description: set深度复制
     * @param s 数据对象
     * @param clz 复制目标类型
     * @return
     * @author banjuer@outlook.com
     * @Time 2018年5月9日 下午3:54:39
     */
    <T, S> Set<T> convert(Set<S> s, Class<T> clz);

    /**
     * @Description: 数组深度复制
     * @param s 数据对象
     * @param clz 复制目标类型
     * @return
     * @author banjuer@outlook.com
     * @Time 2018年5月9日 下午3:54:57
     */
    <T, S> T[] convert(S[] s, Class<T> clz);

}