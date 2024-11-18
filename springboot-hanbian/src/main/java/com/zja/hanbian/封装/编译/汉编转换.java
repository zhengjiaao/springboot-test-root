package com.zja.hanbian.封装.编译;

import com.zja.hanbian.封装.工具.项目工具;
import lombok.Getter;
import lombok.Setter;
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
        // Java关键词
        content = keyWordToJava(content);
        // Java数据类型
        content = dataTypeToJava(content);
        // Java数据结构
        // content = dataStructureToJava(content);
        // Java时间
        content = timeToJava(content);
        // lombok注解
        content = lombokToJava(content);
        // jpa注解
        content = jpaEntityToJava(content);
        content = jpaDaoToJava(content);

        String destFilePath = FilenameUtils.removeExtension(hbFile.toString()) + ".java";
        Path targetJavaFile = Paths.get(destFilePath);
        Files.write(targetJavaFile, content.getBytes());

        System.out.println("汉编转换 toJava: " + hbFile + " -> " + targetJavaFile);
        // Files.delete(hbFile);
    }

    // Java关键词 转换器
    private static String keyWordToJava(String content) {
        content = content.replace("包", "package");
        content = content.replace("导入", "import");
        content = content.replace("类", "class");
        content = content.replace("接口", "interface");
        content = content.replace("枚举", "enum");
        content = content.replace("公共", "public");
        content = content.replace("私有", "private");
        // content = content.replace("新", "new");
        content = content.replace("返回", "return");

        return content;
    }

    // Java内置数据结构 转换器(无需导入依赖)
    private static String dataTypeToJava(String content) {

        content = content.replace("长整型", "long");
        // content = content.replace("长整型", "Long");
        content = content.replace("整型", "int");
        // content = content.replace("整型", "Integer");
        content = content.replace("浮点型", "double");
        content = content.replace("布尔型", "boolean");
        content = content.replace("字符型", "String");

        return content;
    }

    // Java 非基础数据结构 转换器(需导入依赖)
    private static String dataStructureToJava(String content) {
        // 字典
        content = content.replace("新建一个字典", "new HashMap()");
        content = content.replace("哈希字典", "HashMap");
        content = content.replace("字典键", "Map.Key");
        content = content.replace("字典值", "Map.Value");
        content = content.replace("字典键值", "Map.Entry");
        content = content.replace("字典.读值", "Map.get");
        content = content.replace("字典.写值", "Map.put");
        content = content.replace("字典", "Map");

        // List
        content = content.replace("新建一个列表", "new ArrayList()");
        content = content.replace("数组列表", "ArrayList");
        content = content.replace("列表", "List");

        // Set
        // content = content.replace("散列表", "HashTable");
        content = content.replace("哈希表", "HashSet");

        // content = content.replace("集合", "Collection");
        // content = content.replace("队列", "Queue");
        // content = content.replace("栈", "Stack");
        // content = content.replace("树", "Tree");
        // content = content.replace("图", "Graph");

        // Object
        // content = content.replace("对象", "Object");
        // content = content.replace("数组", "Array");

        return content;
    }

    // Java 时间 转换器
    private static String timeToJava(String content) {
        content = content.replace("本地日期时间", "LocalDateTime");
        // content = content.replace("日期时间戳", "Timestamp");
        // content = content.replace("日期时间", "DateTime");
        // content = content.replace("日期", "Date");
        // content = content.replace("时间", "Time");

        return content;
    }

    // lombok 注解 转换器
    private static String lombokToJava(String content) {
        boolean isLombok = content.contains("@属性数据") || content.contains("@属性值获取") || content.contains("@属性值设置");

        if (isLombok) {
            content = importToJava(content, "lombok.*");

            content = content.replace("@属性数据", "@Data");
            content = content.replace("@属性值获取", "@Getter");
            content = content.replace("@属性值设置", "@Setter");
        }

        return content;
    }

    // 数据库 jpa注解 转换器
    private static String jpaEntityToJava(String content) {
        boolean isJpa = content.contains("@实体");

        if (isJpa) {
            content = importToJava(content, "javax.persistence.*");
            content = importToJava(content, "javax.persistence.Id");
            content = importToJava(content, "org.springframework.data.annotation.*");
            content = importToJava(content, "org.springframework.data.jpa.domain.support.AuditingEntityListener");

            content = content.replace("@实体主键", "@Id");
            content = content.replace("@实体字段(名称", "@Column(name");
            content = content.replace("@实体", "@Entity");
            content = content.replace("@表(名称", "@Table(name");
            content = content.replace("@审计监听器", "@EntityListeners(value = AuditingEntityListener.class)");
            content = content.replace("@创建日期", "@CreatedDate");
            content = content.replace("@最后修改日期", "@LastModifiedDate");
        }

        return content;
    }

    // 数据库 jpa注解 转换器
    private static String jpaDaoToJava(String content) {
        boolean isJpa = content.contains("@存储库");

        if (isJpa) {
            // content = importToJava(content, "org.springframework.data.jpa.repository.*");
            // content = importToJava(content, "org.springframework.data.repository.CrudRepository");
            content = importToJava(content, "org.springframework.stereotype.Repository");

            // content = importToJava(content, "java.util.Optional");

            content = content.replace("@存储库", "@Repository");
        }

        return content;
    }

    private static String toJava3(String content) {

        return content;
    }

    // 第二行，插入 import 包，例如： java.lang.*
    private static String importToJava(String content, String packageName) {
        content = content.substring(0, content.indexOf("\n")) + "\nimport " + packageName + ";" + content.substring(content.indexOf("\n"));
        return content;
    }
}
