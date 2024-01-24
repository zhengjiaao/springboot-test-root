package com.zja.log.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author: zhengja
 * @since: 2024/01/23 15:38
 */
@Slf4j
@Service
public class UserService {

    // 测试显示级别
    @Scheduled(fixedRate = 5000) // 每隔5秒执行一次
    public void add() {
        log.trace("log=trace");

        log.debug("log=debug");

        log.info("log=info");

        log.warn("log=warn");

        log.error("log=error");
    }

}
