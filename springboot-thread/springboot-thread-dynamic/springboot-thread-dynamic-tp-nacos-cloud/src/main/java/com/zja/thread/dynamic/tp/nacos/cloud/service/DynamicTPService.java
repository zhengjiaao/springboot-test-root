package com.zja.thread.dynamic.tp.nacos.cloud.service;

import lombok.extern.slf4j.Slf4j;
import org.dromara.dynamictp.core.DtpRegistry;
import org.dromara.dynamictp.core.executor.DtpExecutor;
import org.dromara.dynamictp.core.support.task.runnable.NamedRunnable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: zhengja
 * @since: 2024/02/22 13:36
 */
@Slf4j
@Service
public class DynamicTPService {

    @Resource
    private ThreadPoolExecutor jucThreadPoolExecutor; // 普通juc线程池
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor; // spring线程池

    // DynamicTP 提供的增强线程池 DtpExecutor
    @Resource
    private ThreadPoolExecutor dtpExecutor0;  // 增强线程池 DtpExecutor
    // @Resource
    // private DtpExecutor dtpExecutor1; // 增强线程池 DtpExecutor
    @Resource
    private ThreadPoolExecutor eagerDtpExecutor; // 适用于处理io密集型任务场景
    @Resource
    private ThreadPoolExecutor orderedDtpExecutor; // 适用于处理有序任务场景
    @Resource
    private ThreadPoolExecutor scheduledDtpExecutor;  // 适用于处理定时任务场景

    public void jucThreadPoolExecutor_taskExample() throws InterruptedException {
        for (int i = 0; i < 500; i++) {
            // Thread.sleep(100);
            jucThreadPoolExecutor.execute(() -> {
                try {
                    Thread.sleep(1000);
                    log.info("i am jucThreadPoolExecutor_taskExample task");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    // throw new RuntimeException(e);
                }
            });
        }
    }

    public void threadPoolTaskExecutor_taskExample() throws InterruptedException {
        for (int i = 0; i < 500; i++) {
            // Thread.sleep(100);
            threadPoolTaskExecutor.execute(() -> {
                try {
                    Thread.sleep(1000);
                    log.info("i am threadPoolTaskExecutor_taskExample task");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    // throw new RuntimeException(e);
                }
            });
        }
    }

    // 通过Bean 获取dtp执行器
    public void dtpExecutor0_taskExample() throws InterruptedException {
        for (int i = 0; i < 500; i++) {
            // Thread.sleep(100);
            dtpExecutor0.execute(() -> {
                try {
                    Thread.sleep(1000);
                    log.info("i am dynamic-tp-test-0 task");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    // throw new RuntimeException(e);
                }
            });
        }
    }

    // 通过DtpRegistry.getDtpExecutor("") 获取dtp执行器
    public void dtpExecutor1_taskExample() throws InterruptedException {
        // 获取dtp执行器
        DtpExecutor dtpExecutor1 = DtpRegistry.getDtpExecutor("dtpExecutor1");
        for (int i = 0; i < 200; i++) {
            // Thread.sleep(100);
            dtpExecutor1.execute(NamedRunnable.of(() -> {
                try {
                    Thread.sleep(1000);
                    log.info("i am dynamic-tp-test-1 task");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    // throw new RuntimeException(e);
                }
            }, "task-" + i));
        }
    }

    public void eagerDtpExecutor_taskExample() throws InterruptedException {
        for (int i = 0; i < 500; i++) {
            // Thread.sleep(100);
            eagerDtpExecutor.execute(() -> {
                try {
                    Thread.sleep(1000);
                    log.info("i am eagerDtpExecutor_taskExample task");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    // throw new RuntimeException(e);
                }
            });
        }
    }

    public void orderedDtpExecutor_taskExample() throws InterruptedException {
        for (int i = 0; i < 500; i++) {
            // Thread.sleep(100);
            orderedDtpExecutor.execute(() -> {
                try {
                    Thread.sleep(1000);
                    log.info("i am orderedDtpExecutor_taskExample task");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    // throw new RuntimeException(e);
                }
            });
        }
    }

    public void scheduledDtpExecutor_taskExample() throws InterruptedException {
        for (int i = 0; i < 500; i++) {
            // Thread.sleep(100);
            scheduledDtpExecutor.execute(() -> {
                try {
                    Thread.sleep(1000);
                    log.info("i am scheduledDtpExecutor_taskExample task");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    // throw new RuntimeException(e);
                }
            });
        }
    }

}
