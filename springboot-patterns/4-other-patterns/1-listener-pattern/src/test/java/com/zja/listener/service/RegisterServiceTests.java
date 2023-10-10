/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-02-16 9:30
 * @Since:
 */
package com.zja.listener.service;

import com.zja.listener.DesignmodeListenerApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: zhengja
 * @since: 2023/02/16 9:30
 */
public class RegisterServiceTests extends DesignmodeListenerApplicationTests {

    @Autowired
    RegisterService registerService;

    @Test
    public void register_test() {
        registerService.register("李四");
    }

}
