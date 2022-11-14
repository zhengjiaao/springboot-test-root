package com.zja.repositorys;

import com.zja.entity.Have;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-08-25 11:27
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@Repository
public interface HaveRepository extends Neo4jRepository<Have, Long> {
}
