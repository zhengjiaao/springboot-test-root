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
    public void test2() {
        List<User> userList = (List<User>) userRepository.findAll();
        System.out.println(userList);
    }

    @Test
    public void test3() {
        List<User> userList = userRepository.getUserByName("李四");
        System.out.println(userList);
    }

    @Test
    public void deleteAllTest() throws Exception {
        //仅删除所有的User，不会删除Phone
        userRepository.deleteAll();
    }
}
