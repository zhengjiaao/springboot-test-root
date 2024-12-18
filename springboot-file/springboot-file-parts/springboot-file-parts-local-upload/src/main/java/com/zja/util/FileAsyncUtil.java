package com.zja.util;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author: zhengja
 * @since: 2019/9/17 16:24
 */
@Component
public class FileAsyncUtil {

    /**异步方法
     * 有@Async注解的方法，默认就是异步执行的，会在默认的线程池中执行，但是此方法不能在本类调用；启动类需添加直接开启异步执行@EnableAsync
     * @param bytes
     * @param localFileName
     */
    @Async("taskExecutor")
    public void downLoadFile(byte[] bytes, String localFileName){
        FileOutputStream output = null;
        try {
            File storeFile = new File(localFileName);
            output = new FileOutputStream(storeFile);
            System.out.println("[ "+localFileName+" ]文件下载中......"+" 线程名称: "+Thread.currentThread().getName());
            // 得到资源的字节数组,并写入文件
            output.write(bytes);
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(output != null){
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
