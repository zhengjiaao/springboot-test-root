package com.zja.hanbian.封装.工具;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * @Author: zhengja
 * @Date: 2024-11-08 9:39
 */
public class 项目工具 {

    /**
     * 获取项目根目录
     *
     * @return 项目根目录
     */
    public static String getProjectRoot() throws URISyntaxException {
        String targetClassesPath = getClassesPath();
        File targetClassesDir = new File(targetClassesPath);
        File moduleRootDir = targetClassesDir.getParentFile().getParentFile();
        return moduleRootDir.getAbsolutePath();
    }

    /**
     * 获取项目路径\target\classes\
     *
     * @return 项目路径\target\classes\
     */
    private static String getClassesPath() throws URISyntaxException {
        ClassLoader classLoader = 项目工具.class.getClassLoader();
        URI javaSourceDirUri = Objects.requireNonNull(classLoader.getResource("")).toURI();
        return Paths.get(javaSourceDirUri).toAbsolutePath().toString();
    }
}
