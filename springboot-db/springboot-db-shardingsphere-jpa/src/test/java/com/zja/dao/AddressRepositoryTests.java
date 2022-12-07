/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-12-06 16:22
 * @Since:
 */
package com.zja.dao;

import com.zja.entity.AddressEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
public class AddressRepositoryTests {

    @Autowired
    AddressRepository addressRepository;

    //广播表规则列表，表结构和表中的数据在每个数据库中均完全⼀致
    @Test
    public void test() {
        IntStream.rangeClosed(1, 5).forEach(i -> {
            AddressEntity entity = new AddressEntity();
            entity.setAddressName("李四-" + i);
            System.out.println(entity);
            addressRepository.save(entity);
        });

        List<AddressEntity> entityList = addressRepository.findAll();
        entityList.forEach(s -> System.out.println(s));
    }
}
