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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * 加载【嵌入式数据库】 H2
 * <p>
 * 使用 @DataJpaTest 注解来创建一个嵌入式的数据库，并加载相关的实体类和存储库。
 * </p>
 * 使用  @DataJpaTest 注解注意事项：
 * 1.默认数据库问题：默认使用嵌入式内存数据库，如H2数据库。 解决方案：使用 @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) 注解来指定一个外部数据库
 * 2.事务管理问题：@DataJpaTest具有事务性，会自动回滚插入数据。解决方案：在测试方法上添加 @Commit(true) 注解，确保插入的数据被提交到数据库中。
 * 3.配置问题：需要确保相关的配置正确无误。例如，需要正确配置数据源、JPA实体类、Repository等。如果配置不正确，可能会导致测试失败或出现意外的行为。
 * 4.依赖问题：需要确保相关的依赖被正确地引入到项目中。例如，需要引入Spring Boot Test、Spring Data JPA等依赖。如果缺少必要的依赖，可能会导致测试失败或出现异常。
 *
 * @author: zhengja
 * @since: 2023/10/13 16:45
 */
@Deprecated
@DataJpaTest // todo 无法自动创建表
// @AutoConfigureTestDatabase // 自动配置测试数据库(默认)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)  // 设置 replace 属性为 Replace.NONE，禁用自动配置的嵌入式数据库，以便使用外部的数据库
//@TestPropertySource(locations = "classpath:test.properties") // 使用自定义的测试属性文件
// @TestPropertySource(locations = "classpath:db-h2.yml") // 使用自定义的测试属性文件
public class UserRepositoryH2Test {

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