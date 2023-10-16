/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-13 16:45
 * @Since:
 */
package com.zja.dao;

import com.zja.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * // 使用 @DataJpaTest 注解来创建一个嵌入式的数据库，并加载相关的实体类和存储库。
 *
 * @author: zhengja
 * @since: 2023/10/13 16:45
 */
@DataJpaTest
//在注解中设置 replace 属性为 Replace.NONE，以禁用自动替换数据源。这样，测试将使用实际的数据库配置而不是嵌入式数据库。
//@AutoConfigureTestDatabase // 自动配置测试数据库(默认)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)  // 禁用自动配置的嵌入式数据库，以便使用外部的数据库
//@TestPropertySource(locations = "classpath:test.properties") // 使用自定义的测试属性文件
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveUser() {
        // 创建一个用户对象
        User user = new User();
        user.setName("John");

        // 保存用户到数据库
        User savedUser = userRepository.save(user);

        // 从数据库中查询用户
        User retrievedUser = entityManager.find(User.class, savedUser.getId());

        // 验证用户是否保存成功
        assertThat(retrievedUser).isNotNull();
        assertThat(retrievedUser.getName()).isEqualTo("John");
    }
}