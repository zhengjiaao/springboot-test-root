package com.zja.log.logback;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author: zhengja
 * @since: 2024/01/23 15:31
 */
@SpringBootTest
public class LogbackServiceTest {

    @Autowired
    LogbackService logbackService;

    @Test
    public void test() {
        logbackService.get();
    }
}
