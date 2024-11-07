package com.zja.hanbian.封装.编译;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @Author: zhengja
 * @Date: 2024-11-07 16:08
 */
public class 汉编转换工具 {

    private 汉编转换工具() {

    }

    public static void main(String[] args) {
        汉编转换工具.toJava("D:\\Zhengja\\project\\github\\w_java\\springboot-test-root\\springboot-hanbian\\src\\main\\java\\com\\zja\\hanbian\\controller");
        汉编转换工具.toJava("D:\\Zhengja\\project\\github\\w_java\\springboot-test-root\\springboot-hanbian\\src\\main\\java\\com\\zja\\hanbian\\service");
    }

    /**
     * 汉转Java
     *
     * @param hbDir 汉编文件路径
     */
    public static void toJava(String hbDir) {
        try {
            System.out.println("hbPath1: " + hbDir);
            File directory = new File(hbDir);
            if (directory.isDirectory()) {
                File[] javaFiles = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".hb"));
                if (javaFiles != null && javaFiles.length > 0) {
                    for (File file : javaFiles) {
                        Path filePath = file.toPath();
                        String content = new String(Files.readAllBytes(filePath));
                        // 替换第一行
                        // content = content.replaceFirst(".*", "package com.zja.hanbian.service;");

                        // 确保字符串替换的顺序不会导致意外的结果
                        content = content.replace("包", "package");
                        content = content.replace("导入", "import");
                        content = content.replace("类", "class");
                        content = content.replace("接口", "interface");
                        content = content.replace("枚举", "enum");
                        content = content.replace("公共", "public");
                        content = content.replace("私有", "private");
                        content = content.replace("新", "new");
                        content = content.replace("返回", "return");
                        System.out.println("hbPath2: " + hbDir);

                        String destFilePath = hbDir + File.separator + FilenameUtils.getBaseName(file.getName()) + ".java";
                        Path destPath = Paths.get(destFilePath);
                        Files.write(destPath, content.getBytes());

                        System.out.println("File modified and copied to test directory: " + file.getName());
                    }
                } else {
                    System.out.println("No Java files found in the specified directory.");
                }
            } else {
                System.out.println("The specified directory does not exist or is not a directory.");
            }
        } catch (IOException e) {
            System.err.println("An error occurred while processing the files: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
