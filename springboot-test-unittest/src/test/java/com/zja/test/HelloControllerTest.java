package com.zja.test;

import com.zja.base.BaseTest;
import com.zja.controller.HelloController;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-10-12 13:41
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@Slf4j
public class HelloControllerTest extends BaseTest {

    @Resource
    private HelloController helloController;

    @Before
    public void first() {
        this.setClassName("HelloController");
        this.setLog(LoggerFactory.getLogger(HelloControllerTest.class));
    }

    @Test
    public void hello() {
        Integer id = 6179;
        try {
            String hello = this.helloController.hello();
            this.sayStr(hello, "hello");
        } catch (Exception e) {
            this.sayException(e, "hello");
        }
    }

}
