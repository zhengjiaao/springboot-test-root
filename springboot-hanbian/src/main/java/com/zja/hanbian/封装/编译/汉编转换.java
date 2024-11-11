package com.zja.hanbian.封装.编译;

import com.zja.hanbian.封装.工具.项目工具;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @Author: zhengja
 * @Date: 2024-11-07 16:08
 */
public class 汉编转换 {

    private 汉编转换() {

    }

    public static void main(String[] args) throws URISyntaxException, IOException {
        String projectRoot = 项目工具.getProjectRoot();
        Path hbDir = Paths.get(projectRoot, "src", "main", "java");
        Path toJavaDir = Paths.get(projectRoot, "src", "main", "java");

        // 从中文到英文文件的转换
        conversionFromChineseToEnglishFiles(hbDir, toJavaDir, ".hb");
    }

    public static void conversionFromChineseToEnglishFiles(Path sourceDir, Path targetDir, String... fileExtensions) throws IOException {
        if (fileExtensions.length == 0) {
            fileExtensions = new String[]{".hb"};
        }

        for (String fileExtension : fileExtensions) {
            Files.walkFileTree(sourceDir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.toString().endsWith(fileExtension)) {
                        // 计算目标文件路径
                        Path targetFile = targetDir.resolve(sourceDir.relativize(file));
                        // 转为 Java 文件
                        toJava(targetFile);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

    public static void toJava(Path hbFile) throws IOException {
        // 替换targetFile目标文件的内容的关键词，并转为Java文件
        String content = new String(Files.readAllBytes(hbFile));
        content = content.replace("包", "package");
        content = content.replace("导入", "import");
        content = content.replace("类", "class");
        content = content.replace("接口", "interface");
        content = content.replace("枚举", "enum");
        content = content.replace("公共", "public");
        content = content.replace("私有", "private");
        // content = content.replace("新", "new");
        content = content.replace("返回", "return");

        String destFilePath = FilenameUtils.removeExtension(hbFile.toString()) + ".java";
        Path targetJavaFile = Paths.get(destFilePath);
        Files.write(targetJavaFile, content.getBytes());

        System.out.println("汉编转换 toJava: " + hbFile + " -> " + targetJavaFile);
        // Files.delete(hbFile);
    }

}
