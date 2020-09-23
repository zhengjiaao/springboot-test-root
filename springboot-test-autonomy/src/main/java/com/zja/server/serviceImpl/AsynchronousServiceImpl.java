package com.zja.server.serviceImpl;

import com.zja.server.AsynchronousService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/5/7 14:05
 */
@Service
public class AsynchronousServiceImpl implements AsynchronousService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 异步方法
     * 有@Async注解的方法，默认就是异步执行的，会在默认的线程池中执行，但是此方法不能在本类调用；启动类需添加直接开启异步执行@EnableAsync
     */
    @Async
    @Override
    public String testAsynchronous() {

        String msg = "123";
        synchronized (msg) {
            try {
                for (int i = 1; i <= 100; i++) {
                    logger.info(Thread.currentThread().getName() + "----------异步：>" + i);
                    msg.wait(200);
                }
                return "执行异步任务完毕";
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return Thread.currentThread().getName() + "执行完毕";
    }


    /**
     * 同步方法
     */
    @Override
    public void testSynchronize() {

        String msg = "123";

        synchronized (msg) {
            try {
                for (int i = 1; i <= 50; i++) {
                    logger.info(Thread.currentThread().getName() + "----------同步：>" + i);
                    msg.wait(200);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 使用自定义线程池 ThreadPoolConfig配置线程池
     *
     * @return
     */
    @Async("asyncPoolTaskExecutor")
    @Override
    public void asyncEvent() throws InterruptedException {
        //休眠1s
        Thread.sleep(1000);
        logger.info("异步方法内部线程名称：{}!", Thread.currentThread().getName());
    }

    /**
     * 异步回调返回结果及超时处理
     *
     * @throws InterruptedException
     */
    @Async("asyncPoolTaskExecutor")
    @Override
    public Future<String> asyncFuture() throws InterruptedException {

        for (int i = 1; i <= 50; i++) {
            logger.info("异步方法内部线程名称: ", Thread.currentThread().getName() + "------异步：>" + i);
        }
        //休眠1s=1000
        Thread.sleep(100); //测试30s=30000超时异常
        return new AsyncResult<>("异步方法返回值");
    }
}