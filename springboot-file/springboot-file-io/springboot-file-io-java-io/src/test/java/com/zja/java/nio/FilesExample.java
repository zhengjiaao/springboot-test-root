package com.zja.java.nio;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

/**
 * Files 类处理文件,按 Path 对象操作
 *
 * @author: zhengja
 * @since: 2024/01/22 17:01
 */
public class FilesExample {

    static final String filePath = Paths.get("target", "example_nio.txt").toString();

    static final String filePath_destination = Paths.get("target", "example_nio_destination.txt").toString();

    static final String filePath_newLocation = Paths.get("target", "example_nio_destination_move.txt").toString();

    @Test
    public void test_Files() throws IOException {
        Path sourcePath = Paths.get(filePath);
        Path destinationPath = Paths.get(filePath_destination);

        // 复制文件, 如果目标文件已存在，则覆盖
        Path copy_path = Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("复制后的文件路径：" + copy_path);

        // 移动文件, 如果目标文件已存在，则覆盖
        Path newLocation = Paths.get(filePath_newLocation);
        Path move_path = Files.move(destinationPath, newLocation, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("移动后的文件路径：" + move_path);
    }

    @Test
    public void test_Files_read() throws IOException {
        Path sourcePath = Paths.get(filePath);
        // 读取文件内容
        try (InputStream inputStream = Files.newInputStream(sourcePath, StandardOpenOption.READ)) {
            // 读取文件内容(按字节读取)
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                // 处理读取到的字节数据
                System.out.println("文件内容：" + new String(buffer, 0, bytesRead));
            }
        }

        // 读取文件内容(按字节读取)
        byte[] fileBytes = Files.readAllBytes(sourcePath);
        System.out.println("文件内容：" + new String(fileBytes));

        // 读取文件内容(按行读取)
        List<String> fileContent = Files.readAllLines(sourcePath);
        System.out.println("文件内容：" + fileContent);
    }

    @Test
    public void test_Files_io() throws IOException {
        Path sourcePath = Paths.get(filePath);
        Path destinationPath = Paths.get(filePath_destination);

        // 读取文件内容
        try (InputStream inputStream = Files.newInputStream(sourcePath, StandardOpenOption.READ)) {
            // 读取文件内容(按字节读取)
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                // 处理读取到的字节数据
                System.out.println("文件内容：" + new String(buffer, 0, bytesRead));
            }
        }

        // 写入文件内容, 如果文件不存在，则创建，如果文件已存在，（APPEND）则在文件末尾追加，（CREATE） 如果文件不存在，则创建,存在则不覆盖,(TRUNCATE_EXISTING) 清空文件，覆盖
        try (OutputStream outputStream = Files.newOutputStream(destinationPath, StandardOpenOption.TRUNCATE_EXISTING)) {
            outputStream.write("Hello, world!".getBytes());
        }
    }

    // 创建目录
    @Test
    public void test_Files_create() throws IOException {
        // 创建目录, 如果父目录不存在，则创建，如果目录已存在，则不会覆盖
        Path example_nio_dir_path = Files.createDirectories(Paths.get("target", "example_nio_dir"));
        Path subdir_path = Files.createDirectories(Paths.get("target", "example_nio_dir", "subdir"));
        System.out.println(example_nio_dir_path);
        System.out.println(subdir_path);

        // 创建目录, 如果父目录不存在，则抛出异常，如果目录已存在，则抛出异常
        // Path subdir2_path = Files.createDirectory(Paths.get("target", "example_nio_dir", "subdir2"));
        // System.out.println(subdir2_path);

        // 创建临时目录，默认在系统临时目录下，随机名称，默认为可读可写，示例：C:\Users\CHANGJ~1\AppData\Local\Temp\prefix5697958607673745751
        // System.out.println(Files.createTempDirectory("prefix"));
    }

    // 创建文件
    @Test
    public void test_Files_create_file() throws IOException {

        Path file_path = Paths.get("target", "example_nio_dir", "file.txt");

        boolean exists = Files.exists(file_path);
        if (!exists) {
            // 创建文件, 如果文件已存在，则抛出异常，不会覆盖，父目录不存在，则抛出异常
            Files.createFile(file_path);
        }

        Path link_path = Paths.get("target", "example_nio_dir", "link.txt");

        boolean exists_link = Files.exists(link_path);
        if (!exists_link) {
            // 创建符号链接，默认为相对路径，如果文件不存在，则抛出异常，如果符号链接文件已存在，则抛出异常，父目录不存在，则抛出异常
            Files.createLink(link_path, file_path);
        }

        // 创建临时文件，默认在系统临时目录下，随机名称，默认为可读可写，示例：C:\Users\CHANGJ~1\AppData\Local\Temp\prefix5697958607673745751.tmp
        // System.out.println(Files.createTempFile("prefix", ".tmp"));
    }

    // 删除文件
    @Test
    public void test_Files_delete() throws IOException {
        Path destinationPath = Paths.get(filePath_destination);
        Path newLocation = Paths.get(filePath_newLocation);

        // 删除文件，如果文件不存在，会抛出异常
        Files.delete(destinationPath);

        // 删除文件，如果文件不存在，则返回false，不会抛出异常
        System.out.println(Files.deleteIfExists(destinationPath));
        System.out.println(Files.deleteIfExists(newLocation));
    }

    @Test
    public void test_Files_copy() throws IOException {
        Path sourcePath = Paths.get(filePath);
        Path destinationPath = Paths.get(filePath_destination);

        // 复制文件，如果目标文件已存在，则覆盖
        Path copy_path = Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("复制后的文件路径：" + copy_path);
    }

    @Test
    public void test_Files_move() throws IOException {
        Path sourcePath = Paths.get(filePath);
        Path destinationPath = Paths.get(filePath_destination);

        // 移动文件，如果目标文件已存在，则覆盖
        Path move_path = Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("移动后的文件路径：" + move_path);
    }

    @Test
    public void test_Files_walk() throws IOException {
        Path rootPath = Paths.get(".");
        Files.walk(rootPath)
                .filter(Files::isRegularFile)
                .forEach(System.out::println);
    }

    @Test
    public void test_Files_walkFileTree() throws IOException {
        Path rootPath = Paths.get(".");
        Files.walkFileTree(rootPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println("文件：" + file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                System.out.println("目录：" + dir);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                System.out.println("访问文件失败：" + file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                System.out.println("访问目录结束：" + dir);
                return FileVisitResult.CONTINUE;
            }
        });
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
                        // toJava(targetFile);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

    private static void copyHbFiles2(Path sourceDir, Path targetDir) throws IOException {
        Files.walkFileTree(sourceDir, new SimpleFileVisitor<Path>() {
            private boolean hasHbFile = false;

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                hasHbFile = false; // 重置标志
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.toString().endsWith(".hb")) {
                    hasHbFile = true;
                    // 计算目标文件路径
                    Path targetFile = targetDir.resolve(sourceDir.relativize(file));
                    Path targetSubDir = targetFile.getParent();
                    if (!Files.exists(targetSubDir)) {
                        Files.createDirectories(targetSubDir);
                    }
                    Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                    // 转为 Java 文件
                    // toJava(targetFile);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if (!hasHbFile) {
                    // 如果当前目录没有 .hb 文件，删除已创建的空目录
                    Path targetSubDir = targetDir.resolve(sourceDir.relativize(dir));
                    if (Files.exists(targetSubDir) && Files.isDirectory(targetSubDir) && Files.list(targetSubDir).count() == 0) {
                        Files.delete(targetSubDir);
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static void copyHbFiles3(Path sourceDir, Path targetDir) throws IOException {
        Files.walkFileTree(sourceDir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                // 创建目标目录
                Path targetSubDir = targetDir.resolve(sourceDir.relativize(dir));
                if (!Files.exists(targetSubDir)) {
                    Files.createDirectories(targetSubDir);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.toString().endsWith(".hb")) {
                    // 计算目标文件路径
                    Path targetFile = targetDir.resolve(sourceDir.relativize(file));
                    Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

}
