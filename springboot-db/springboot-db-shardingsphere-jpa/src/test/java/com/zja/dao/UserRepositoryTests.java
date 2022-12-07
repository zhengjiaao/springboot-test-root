/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-12-06 13:16
 * @Since:
 */
package com.zja.dao;

import com.zja.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@SpringBootTest
public class UserRepositoryTests {

    @Autowired
    UserRepository userRepository;

    @Test
    public void test() {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        IntStream.rangeClosed(1, 5).forEach(i -> {
            UserEntity entity = new UserEntity();
            entity.setUsername("李四-" + Math.abs(random.nextInt()));
            entity.setPwd(String.valueOf(i));

            System.out.println(entity);
            userRepository.save(entity);
        });

        List<UserEntity> entityList = userRepository.findAll();
        entityList.forEach(s -> System.out.println(s));
    }
}
