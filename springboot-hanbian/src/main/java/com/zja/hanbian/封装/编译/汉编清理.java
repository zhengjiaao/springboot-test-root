package com.zja.hanbian.封装.编译;

import com.zja.hanbian.封装.工具.项目工具;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @Author: zhengja
 * @Date: 2024-11-08 16:58
 */
public class 汉编清理 {

    private 汉编清理() {

    }

    public static void main(String[] args) throws Exception {
        String projectRoot = 项目工具.getProjectRoot();
        Path hbDir = Paths.get(projectRoot, "src", "main", "java");

        deleteDir(hbDir);
    }

    private static void deleteDir(Path dir) throws IOException {
        if (!Files.exists(dir)) {
            return;
        }

        Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                // 若文件是以"*.hb"结尾，并且存在同名的"*.java"文件，则进行删除"*.java"文件
                if (file.toString().endsWith(".hb")) {
                    Path javaFilePath = file.resolveSibling(file.getFileName().toString().replace(".hb", ".java"));
                    Files.deleteIfExists(javaFilePath);
                }
                return FileVisitResult.CONTINUE;
            }

            /*@Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }*/
        });
    }
}
