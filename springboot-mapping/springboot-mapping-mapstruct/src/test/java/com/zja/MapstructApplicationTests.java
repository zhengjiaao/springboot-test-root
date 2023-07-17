/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-10 16:32
 * @Since:
 */
package com.zja;

import com.zja.mapper.UserMapper;
import com.zja.entity.User;
import com.zja.model.dto.UserDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class) //注入 spring bean 对象
@SpringBootTest
public class MapstructApplicationTests {

    @Autowired
    UserMapper userMapper;

    @Test
    public void test() {
        User user = new User();
        user.setUsername("李四");
        user.setPassword("123456");
        user.setSex("男");

//        UserDTO userDTO1 = UserMapper.INSTANCE.map(user);
//        System.out.println("userDTO1=" + userDTO1);

        //需要注释掉上面，多次拷贝会抛出空指针异常
        UserDTO userDTO2 = userMapper.map(user);
        System.out.println("userDTO2=" + userDTO2);
    }
}
