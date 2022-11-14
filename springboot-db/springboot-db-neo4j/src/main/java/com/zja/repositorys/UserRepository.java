package com.zja.repositorys;

import com.zja.entity.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-08-25 10:13
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
public interface UserRepository extends Neo4jRepository<User, Long> {
}
