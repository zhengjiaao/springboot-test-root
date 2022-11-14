package com.zja.repositorys;

import com.zja.entity.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-08-25 9:17
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
public interface PersonRepository extends Neo4jRepository<Person, Long> {

    List<Person> findByName(String name);
}
