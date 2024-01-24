package com.zja.log.logback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author: zhengja
 * @since: 2024/01/23 15:31
 */
@Slf4j
@Service
public class LogbackService {

    // 测试显示级别
    public void get() {
        log.trace("log=trace");

        log.debug("log=debug");

        log.info("log=info");

        log.warn("log=warn");

        log.error("log=error");
    }
}
