package com.zja.service.Impl;

import com.zja.service.ScheduledTaskJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ScheduledTask01JobImpl implements ScheduledTaskJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTask01JobImpl.class);

    @Override
    public void run() {
        LOGGER.info("ScheduledTask => 01  当前线程名称 {} ", Thread.currentThread().getName() + " 执行时间：" + new Date());
    }
}
