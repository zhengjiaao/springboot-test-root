package com.zja.base;

import com.zja.RemoteConsumerApplication;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 单元测试基类
 * @desc 统一入口，如此便不用多次使用注解、域的设置以及日志的手动记录
 * @author gaohj
 * @date 2019/03/14.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RemoteConsumerApplication.class})
public class BaseTest {
    private static Logger LOG = LoggerFactory.getLogger(BaseTest.class);

    @Before
    public void init(){
        LOG.info("===================单元测试初始化===================");
    }
}
