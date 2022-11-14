package com.zja.repositorys;

import com.zja.entity.Car;
import com.zja.entity.Customer;
import com.zja.entity.Have;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-08-25 11:28
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@SpringBootTest
public class HaveRepositoryTests {

    @Autowired
    private HaveRepository haveRepository;

    @Test
    void contextLoads() {
        Customer customer = new Customer();
        customer.setName("Tom");

        Car car = new Car();
        car.setBrand("BMW");

        Have have = new Have();
        have.setCustomer(customer);
        have.setCar(car);
        have.setCreateTime(LocalDateTime.now().toString());

        haveRepository.save(have);
    }
}
