package com.zja.repositorys;

import com.zja.entity.User;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-08-25 10:13
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
public interface UserRepository extends Neo4jRepository<User, Long> {

    /**
     * 使用cypher语法查询 自定义查询
     */
    @Query("MATCH (n:User) where n.name contains $name RETURN n")
    List<User> getUserByName(@Param("name") String name);

}
