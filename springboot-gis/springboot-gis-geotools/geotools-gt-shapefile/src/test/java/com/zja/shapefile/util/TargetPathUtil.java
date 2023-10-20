/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-10 16:58
 * @Since:
 */
package com.zja.shapefile.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author: zhengja
 * @since: 2023/10/10 16:58
 */
@Slf4j
public class TargetPathUtil {

    @Test
    public void testSaveTemporaryFile() throws IOException {
        createTempFile("a.json");
        createTempFile("tmp", "a.json");
    }

    public static String getTempFilePath(String fileName) {
        return getTempFilePath(null, fileName);
    }

    @SneakyThrows
    public static String getTempFilePath(String dirName, String fileName) {
        // 获取 target 目录的路径
        Path targetPath = Paths.get(System.getProperty("user.dir"), "target");

        if (StringUtils.hasText(dirName)) {
            targetPath = targetPath.resolve(dirName);
        }

        Path tempFilePath = targetPath.resolve(fileName);
        Files.createDirectories(tempFilePath.getParent());

        return tempFilePath.toAbsolutePath().toString();
    }

    public static String createTempFile(String fileName) {
        return createTempFile(null, fileName);
    }

    @SneakyThrows
    public static String createTempFile(String dirName, String fileName) {
        // 获取 target 目录的路径
        Path targetPath = Paths.get(System.getProperty("user.dir"), "target");

        //存在上级目录
        if (StringUtils.hasText(dirName)) {
            targetPath = targetPath.resolve(dirName);
        }

        // 创建临时文件
        Path tempFilePath = targetPath.resolve(fileName);

        Files.createDirectories(tempFilePath.getParent());

        if (Files.exists(tempFilePath)) {
            Files.delete(tempFilePath);
        }

        Files.createFile(tempFilePath);

        // 打印临时文件路径
        log.info("临时文件路径：" + tempFilePath.toAbsolutePath());

        return tempFilePath.toAbsolutePath().toString();
    }

}
