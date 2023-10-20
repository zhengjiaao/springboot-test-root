/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-19 11:28
 * @Since:
 */
package com.zja.shapefile.util;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

/**
 * @author: zhengja
 * @since: 2023/10/19 11:28
 */
public class ResourceUtil {

    @Test
    public void test() throws FileNotFoundException {
        File file = ResourceUtil.getResourceFile("geojson.json");
        System.out.println(ResourceUtil.getResourceFilePath("geojson.json"));
        if (file.exists()){
            System.out.println("exists");
        }
        String url = ResourceUtil.getResourcePath("geojson.json");
        System.out.println(url);
    }

    public static String getResourcePath(String resourceName) {
        URL resourceUrl = ResourceUtil.class.getClassLoader().getResource(resourceName);
        if (resourceUrl != null) {
            return resourceUrl.getPath();
        } else {
            throw new IllegalArgumentException("Resource not found: " + resourceName);
        }
    }

    public static String getResourceFilePath(String resourceName) {
        return getResourceFile(resourceName).getAbsolutePath();
    }

    public static File getResourceFile(String resourceName) {
        URL resourceUrl = ResourceUtil.class.getClassLoader().getResource(resourceName);
        if (resourceUrl != null) {
            return new File(resourceUrl.getFile());
        } else {
            throw new IllegalArgumentException("Resource not found: " + resourceName);
        }
    }
}

