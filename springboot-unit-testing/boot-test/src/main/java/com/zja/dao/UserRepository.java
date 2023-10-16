/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-13 16:46
 * @Since:
 */
package com.zja.dao;

import com.zja.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author: zhengja
 * @since: 2023/10/13 16:46
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 可以定义其他自定义查询方法
}