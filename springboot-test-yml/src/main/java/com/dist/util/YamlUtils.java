package com.dist.util;

import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.reader.UnicodeReader;

import java.io.IOException;
import java.util.*;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-08-03 16:26
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
public class YamlUtils {
    /**
     * 单个yaml文件处理
     * @param resource
     * @return
     * @throws IOException
     */
    public static Map<String, Object> yamlHandler(@NonNull Resource resource) throws IOException {
        //返回的结果
        Map<String, Object> result = new LinkedHashMap<>();
        //读取方式
        UnicodeReader reader = new UnicodeReader(resource.getInputStream());
        //单文件处理
        Yaml yaml = new Yaml();
        Object object = yaml.load(reader);
        //这里只是简单处理，需要多个方式可以自己添加
        if (object instanceof Map) {
            Map map = (Map) object;
            buildFlattenedMap(result, map, null);
        }
        reader.close();
        return result;
    }

    /**
     * 单个yaml文件处理
     * @param resources
     * @return
     * @throws IOException
     */
    public static Map<String, Object> yamlHandler(@NonNull Resource[] resources) throws IOException {

        //返回的结果
        Map<String, Object> result = new LinkedHashMap<>();
        Yaml yaml = new Yaml();
        //多个文件处理
        Iterator<Resource> iterator = Arrays.stream(resources).iterator();
        while (iterator.hasNext()) {
            Resource resource = iterator.next();
            UnicodeReader reader = new UnicodeReader(resource.getInputStream());
            Object object = yaml.load(reader);
            //这里只是简单处理，需要多个方式可以自己添加
            if (object instanceof Map) {
                Map map = (Map) object;
                buildFlattenedMap(result, map, null);
            }
            reader.close();
        }
        return result;
    }

    /**
     * 这部分代码来至springboot源码部分对yaml的解析
     * YamlProcessor.java buildFlattenedMap方法
     * @param result
     * @param source
     * @param path
     */
    private static void buildFlattenedMap(Map<String, Object> result, Map<String, Object> source, @Nullable String path) {
        //循环读取原数据
        source.forEach((key, value) -> {
            //如果存在路径进行拼接
            if (StringUtils.hasText(path)) {
                if (key.startsWith("[")) {
                    key = path + key;
                } else {
                    key = path + '.' + key;
                }
            }
            //数据类型匹配
            if (value instanceof String) {
                result.put(key, value);
            } else if (value instanceof Map) {
                //如果是map,就继续读取
                Map<String, Object> map = (Map)value;
                buildFlattenedMap(result, map, key);
            } else if (value instanceof Collection) {
                Collection<Object> collection = (Collection)value;
                if (collection.isEmpty()) {
                    result.put(key, "");
                } else {
                    int count = 0;
                    Iterator var7 = collection.iterator();

                    while(var7.hasNext()) {
                        Object object = var7.next();
                        buildFlattenedMap(result, Collections.singletonMap("[" + count++ + "]", object), key);
                    }
                }
            } else {
                result.put(key, value != null ? value : "");
            }
        });
    }
}
