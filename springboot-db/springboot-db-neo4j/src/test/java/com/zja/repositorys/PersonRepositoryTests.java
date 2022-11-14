package com.zja.repositorys;

import com.zja.entity.Person;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-08-25 13:04
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@SpringBootTest
public class PersonRepositoryTests {

    @Resource
    private PersonRepository personRepository;

    @Test
    void save() {
        Person person = new Person();
        person.setId(1L);
        person.setName("lisi");
        personRepository.save(person);
    }

    @Test
    void findAll() {
        List<Person> all = (List<Person>)personRepository.findAll();
        System.out.println(all);
    }

    @Test
    void findByName() {
        List<Person> all = (List<Person>)personRepository.findByName("lisi");
        System.out.println(all);
    }

}
