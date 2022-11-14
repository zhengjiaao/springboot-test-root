/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-14 16:52
 * @Since:
 */
package com.zja.repositorys;

import com.zja.entity.Phone;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class PhoneRepositoryTests {

    @Resource
    PhoneRepository phoneRepository;

    @Test
    public void test1() {
        Phone p = new Phone();
        p.setId(0L);
        p.setPhoneNo("155****1111");
        p.setUsers(Lists.newArrayList());
        phoneRepository.save(p);
    }

    @Test
    public void test2() {
        List<Phone> phoneList = (List<Phone>) phoneRepository.findAll();
        System.out.println(phoneList);
    }

    @Test
    public void test3() {
        //仅删除所有的Phone，不会删除User
        phoneRepository.deleteAll();
    }

}
