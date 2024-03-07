/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-18 16:22
 * @Since:
 */
package com.zja.pool.druid.dao;

import com.zja.pool.druid.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 *
 */
public interface UserRepo extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

}
