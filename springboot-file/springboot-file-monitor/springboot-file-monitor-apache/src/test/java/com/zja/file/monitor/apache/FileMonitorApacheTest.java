package com.zja.file.monitor.apache;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * @author: zhengja
 * @since: 2024/01/22 16:10
 */
public class FileMonitorApacheTest {


    // 会消耗部分GPU：1%~2%
    @Test
    public void test() throws Exception {
        // 指定要监控的目录
        String directory = "D:\\temp\\pdf";

        // 创建FileAlterationObserver对象
        FileAlterationObserver observer = new FileAlterationObserver(directory);

        // 注册FileAlterationListener来处理文件变动事件
        observer.addListener(new FileAlterationListener() {
            @Override
            public void onStart(FileAlterationObserver observer) {
                System.out.println("文件监控已启动...");
            }

            @Override
            public void onDirectoryCreate(File directory) {
                System.out.println("目录创建：" + directory.getAbsolutePath());
            }

            @Override
            public void onDirectoryChange(File directory) {
                System.out.println("目录修改：" + directory.getAbsolutePath());
            }

            @Override
            public void onDirectoryDelete(File directory) {
                System.out.println("目录删除：" + directory.getAbsolutePath());
            }

            @Override
            public void onFileCreate(File file) {
                System.out.println("文件创建：" + file.getAbsolutePath());
            }

            @Override
            public void onFileChange(File file) {
                System.out.println("文件修改：" + file.getAbsolutePath());
            }

            @Override
            public void onFileDelete(File file) {
                System.out.println("文件删除：" + file.getAbsolutePath());
            }

            @Override
            public void onStop(FileAlterationObserver observer) {
                System.out.println("文件监控已停止...");
            }
        });

        // 创建FileAlterationMonitor对象并设置间隔时间
        long interval = 1000; // 监控间隔，单位：毫秒
        FileAlterationMonitor monitor = new FileAlterationMonitor(interval);
        monitor.addObserver(observer);

        // 启动监控
        monitor.start();

        // 监控持续一段时间后停止
        Thread.sleep(1000 * 300); // 监控持续时间，单位：毫秒

        // 停止监控
        monitor.stop();
    }


}
