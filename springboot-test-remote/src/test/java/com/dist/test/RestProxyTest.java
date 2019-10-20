package com.dist.test;

import com.dist.base.BaseTest;
import com.dist.dto.UserDTO;
import com.dist.server.rest.RestProxyService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/7/10 10:13
 */
public class RestProxyTest extends BaseTest {

    private final Logger LOG =  LoggerFactory.getLogger(RestProxyTest.class);

    @Autowired
    private RestProxyService restProxyService;

    @Autowired
    private Environment environment;

    //获取yml配置的属性值
    @Test
    public void propertiesTest()throws Exception{
        System.out.println("getProperty="+environment.getProperty("spring.server_url.get_url_userdto"));
    }

    @Test
    public void getUserDTO(){
        UserDTO userDTO3 = this.restProxyService.getUserDTO3();
        System.out.println("userDTO3="+userDTO3);
    }

}
