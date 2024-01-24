package com.zja.log.log4j2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author: zhengja
 * @since: 2024/01/23 15:31
 */
@SpringBootTest
public class Log4j2ServiceTest {

    @Autowired
    Log4j2Service log4j2Service;

    @Test
    public void test() {
        log4j2Service.get();
    }
}
