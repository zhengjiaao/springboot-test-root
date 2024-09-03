package com.zja.java.io;

import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Paths;

/**
 * @author: zhengja
 * @since: 2024/01/22 16:55
 */
public class RandomAccessFileExample {

    // 文件路径 .dat 是一个二进制文件格式
    static final String filePath_dat = Paths.get("target", "example_io_random.dat").toString();
    // 文件路径 .txt
    static final String filePath_txt = Paths.get("target", "example_io_random.txt").toString();
    static final String filePath_txt_destination = Paths.get("target", "example_io_random_destination.txt").toString();

    @Test
    public void test_WriteRead() {
        String content = "Hello, World!";

        // 创建 RandomAccessFile 对象，以读写模式打开文件
        try (RandomAccessFile raf = new RandomAccessFile(filePath_txt, "rw")) {
            // 写入内容
            raf.writeBytes(content);

            // 移动文件指针到文件开头
            raf.seek(0);

            // 完整读取内容到字节数组
            byte[] buffer = new byte[content.length()];
            raf.readFully(buffer);
            String readContent = new String(buffer);

            System.out.println("Written content: " + content);
            System.out.println("Read content: " + readContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 在文件中定位并读取特定位置的数据
    @Test
    public void test_Read_Seek() {
        try (RandomAccessFile file = new RandomAccessFile(filePath_txt, "r")) {
            // file.seek(0); // 将文件指针移动到第10个字节的位置
            file.seek(10); // 将文件指针移动到第10个字节的位置
            byte[] buffer = new byte[1024];
            int bytesRead = file.read(buffer);
            while (bytesRead != -1) {
                bytesRead = file.read(buffer);
            }
            String readContent = new String(buffer);
            System.out.println("Read Seek content: " + readContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用RandomAccessFile读取.dat每行记录
     */
    @Test
    public void test_WriteRead_dat() {
        // 写入数据
        try (RandomAccessFile raf = new RandomAccessFile(filePath_dat, "rw")) {
            raf.writeInt(123);         // 写入整数 123
            raf.writeUTF("Hello");     // 写入字符串 "Hello"
            raf.writeDouble(45.67);    // 写入双精度数 45.67
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 读取数据
        try (RandomAccessFile raf = new RandomAccessFile(filePath_dat, "r")) {
            raf.seek(0);  // 移动到文件开头
            int num = raf.readInt();              // 读取整数
            String str = raf.readUTF();            // 读取字符串
            double d = raf.readDouble();           // 读取双精度数

            System.out.println("Integer: " + num);
            System.out.println("String: " + str);
            System.out.println("Double: " + d);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 读取文件中的数据
    @Test
    public void test_ReadAndWrite_txt() {

        // 向文件中写入数据
        try (RandomAccessFile file = new RandomAccessFile(filePath_txt, "rw")) {
            file.seek(file.length()); // 将文件指针移动到文件末尾
            file.writeBytes("Hello, World!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 读取文件中的数据
        try (RandomAccessFile file = new RandomAccessFile(filePath_txt, "r")) {
            byte[] buffer = new byte[1024];
            int bytesRead = file.read(buffer);
            while (bytesRead != -1) {
                System.out.write(buffer, 0, bytesRead);
                bytesRead = file.read(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建 RandomAccessFile 对象：
     * sourceFile：以只读模式 ("r") 打开源文件。
     * destFile：以读写模式 ("rw") 打开目标文件，如果文件不存在，则会创建新文件。
     */
    @Test
    public void test_copy() {
        // 定义读取的起始位置和长度
        long startPosition = 0; // 起始位置
        long length = 10; // 读取长度

        try (RandomAccessFile sourceFile = new RandomAccessFile(filePath_txt, "r"); RandomAccessFile destFile = new RandomAccessFile(filePath_txt_destination, "rw")) {

            // 移动文件指针到指定的起始位置
            sourceFile.seek(startPosition);

            // 读取指定长度的内容
            byte[] buffer = new byte[(int) length];
            int bytesRead = sourceFile.read(buffer, 0, (int) length);

            // 将读取的内容写入到另一个文件,参数解释：写入字节数组、写入偏移量、读取的字节数
            destFile.write(buffer, 0, bytesRead); // 写入指定长度的内容,默认从0开始

            System.out.println("Content copied successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 分块读取文件进行拷贝(下载)
    @Test
    public void test_copy_block() {
        // String sourceFilePath = "largefile.txt"; // 源文件路径
        // String destinationFilePath = "downloadedfile.txt"; // 目标文件路径

        String sourceFilePath = Paths.get("D:\\temp\\zip", "建筑方案.zip").toString();
        String destinationFilePath = Paths.get("target", "example_io_random_downloadedfile.zip").toString();

        int blockSize = 1024 * 1024; // 每个块的大小为 1MB

        try (RandomAccessFile sourceFile = new RandomAccessFile(sourceFilePath, "r"); FileOutputStream destFile = new FileOutputStream(destinationFilePath)) {

            long fileSize = sourceFile.length(); // 获取源文件大小
            int totalBlocks = (int) Math.ceil((double) fileSize / blockSize); // 计算总块数，用文件大小除以块大小，结果向上取整，确保最后一块也能被完整处理。
            System.out.println("fileSize=" + fileSize);
            System.out.println("blockSize=" + blockSize);
            System.out.println("totalBlocks=" + totalBlocks);

            System.out.println("----------------------------------");

            // 循环遍历每个块
            for (int i = 0; i < totalBlocks; i++) {
                long currentBlock = (i + 1); // 当前块的序号
                long currentPosition = (long) i * blockSize; // 当前块的起始位置
                System.out.println("currentBlock=" + currentBlock + " , currentPosition=" + currentPosition);

                // 移动文件指针到当前块的起始位置
                sourceFile.seek(currentPosition);

                // 创建缓冲区并读取当前块的内容
                byte[] buffer = new byte[blockSize];
                int bytesRead = sourceFile.read(buffer);

                // 将读取的内容写入到目标文件
                destFile.write(buffer, 0, bytesRead);

                // 输出每个块的处理进度
                System.out.println("Processed block " + currentBlock + " of " + totalBlocks);
            }

            // 输出文件下载成功的提示信息
            System.out.println("File downloaded successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
