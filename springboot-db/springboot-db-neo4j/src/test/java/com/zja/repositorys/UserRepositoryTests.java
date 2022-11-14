package com.zja.repositorys;

import com.zja.entity.Phone;
import com.zja.entity.User;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-08-25 10:14
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@SpringBootTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    /**
     * 运行一切正常的话，数据库将新建User-[OWN]->Phone：2个节点和1个关系。
     */
    @Test
    public void saveTest() throws Exception {

        Phone phone = new Phone();
        phone.setPhoneNo("13822223456");
        Phone phone2 = new Phone();
        phone2.setPhoneNo("15811116234");

        User user = new User();
        user.setName("Jack");
        user.setPhones(Lists.newArrayList(phone, phone2));

        userRepository.save(user);
    }

    @Test
    public void deleteAllTest() throws Exception {

        //仅删除user，不会删除Phone
        userRepository.deleteAll();
    }

    @Test
    public void findAllTest() throws Exception {
        List<User> userList = (List<User>) userRepository.findAll();
        for (User user : userList) {
            //查询时产生了死循环或无限递归
            //java.lang.StackOverflowError
            System.out.println(user);
            userRepository.delete(user);
        }
    }
}
