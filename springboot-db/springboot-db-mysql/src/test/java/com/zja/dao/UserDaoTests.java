/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-18 16:22
 * @Since:
 */
package com.zja.dao;

import com.zja.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UserDaoTests {

    @Autowired
    UserDao userDao;

    @Test
    public void save_test() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("李四");
        UserEntity entity = userDao.save(userEntity);
        System.out.println(entity);
    }

    @Test
    public void findAll_test() {
        List<UserEntity> entityList = userDao.findAll();
        System.out.println(entityList);
    }
}
