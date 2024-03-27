package com.zja.juc.thread;

import java.io.File;

/**
 * @author: zhengja
 * @since: 2024/03/20 17:23
 */
public class FileThreadUtil {

    private FileThreadUtil() {
    }

    public static void asyncDeleteFile(String filePath) {
        // 创建并启动新线程进行删除操作
        Thread thread = new Thread(new FileDeleter(filePath));
        thread.start();
    }

    static class FileDeleter implements Runnable {
        private String filePath;

        public FileDeleter(String filePath) {
            this.filePath = filePath;
        }

        @Override
        public void run() {
            File file = new File(filePath);
            if (file.exists()) {
                if (file.isDirectory()) {
                    // 删除目录及其内容
                    deleteDirectory(file);
                } else {
                    // 删除文件
                    file.delete();
                }
            }
        }

        private void deleteDirectory(File directory) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        // 递归删除子目录
                        deleteDirectory(file);
                    } else {
                        // 删除文件
                        file.delete();
                    }
                }
            }

            // 删除空目录
            directory.delete();
        }
    }
}
