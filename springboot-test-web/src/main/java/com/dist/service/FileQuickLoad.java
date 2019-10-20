package com.dist.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Date;

/**大文件快速加载
 * @author zhengja@dist.com.cn
 * @data 2019/8/27 18:35
 */
@Async("taskExecutor")  //使用自定义线程池：定义异步任务-并行定时任务执行，默认串行的定时任务
@Component
public class FileQuickLoad {

    public byte[] quickLoad(int threadId, int startPos, int endPos,String saveFile) throws Exception {
        System.out.println(Thread.currentThread().getName()+"=====>>>>>"+"开始时间："+new Date()+"-"+(System.currentTimeMillis()/1000));
        System.out.println("bytes=" + startPos + "-" + endPos);

        int len = 0;
        byte[] buf = new byte[1024];
        RandomAccessFile raf = new RandomAccessFile(saveFile,"r");
        raf.seek(startPos);
        byte readByte = raf.readByte();
        while((len = raf.read(buf)) > 0) {
            raf.write(buf, 0, len);
            System.out.println("buf.toString 1="+Arrays.toString(buf));
        }
        System.out.println("buf.toString="+Arrays.toString(buf));
        System.out.println("线程" + threadId + "下载完毕！！");

        System.out.println(Thread.currentThread().getName()+"=====>>>>>"+"结束时间："+new Date()+"-"+(System.currentTimeMillis()/1000));
        return null;
    }

}
