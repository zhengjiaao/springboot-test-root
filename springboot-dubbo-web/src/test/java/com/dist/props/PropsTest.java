package com.dist.props;

import com.dist.dto.MyProps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/9/9 15:45
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PropsTest {

    @Autowired
    private MyProps myProps;

    @Test
    public void propsTest() {
        System.out.println("simpleProp: " + myProps.toString());
    }
}
