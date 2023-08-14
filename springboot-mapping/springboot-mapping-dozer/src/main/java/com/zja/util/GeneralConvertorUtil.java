/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-03 10:49
 * @Since:
 */
package com.zja.util;

import org.dozer.Mapper;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Component
public class GeneralConvertorUtil {

    @Resource
    Mapper mapper;

    /**
     * List  实体类 深度转换器
     */
    public <T, S> List<T> convertor(List<S> source, Class<T> clz) {
        if (source == null) {
            return null;
        }
        List<T> list = new ArrayList<>();
        for (S s : source) {
            list.add(mapper.map(s, clz));
        }
        return list;
    }

    /**
     * Set 实体类 深度转换器
     */
    public <T, S> Set<T> convertor(Set<S> source, Class<T> clz) {
        if (source == null) {
            return null;
        }
        Set<T> set = new TreeSet<>();
        for (S s : source) {
            set.add(mapper.map(s, clz));
        }
        return set;
    }

    /**
     * 实体类 深度转换器
     */
    public <T, S> T convertor(S source, Class<T> clz) {
        if (source == null) {
            return null;
        }
        return mapper.map(source, clz);
    }

    public void convertor(Object source, Object object) {
        mapper.map(source, object);
    }

    public <T> void copyConvertor(T source, Object object) {
        mapper.map(source, object);
    }

}
