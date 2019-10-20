package com.dist.service.Impl;

import com.dist.service.ScheduledTaskJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

/**实现父接口，重写run方法
 * 注意：ScheduledTask02与ScheduledTask03与当前ScheduledTask01 一致
 * @author zhengja@dist.com.cn
 * @data 2019/8/14 11:14
 */
@Service
public class ScheduledTask01JobImpl implements ScheduledTaskJob{
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTask01JobImpl.class);

    @Override
    public void run() {
        LOGGER.info("ScheduledTask => 01  run  当前线程名称 {} ", Thread.currentThread().getName()+" 执行时间："+new Date());
    }
}
