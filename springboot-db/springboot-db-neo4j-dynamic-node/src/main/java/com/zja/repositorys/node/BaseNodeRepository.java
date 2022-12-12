/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-12-12 10:43
 * @Since:
 */
package com.zja.repositorys.node;

import com.zja.entity.BaseNode;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

/**
 *
 */
public interface BaseNodeRepository extends Neo4jRepository<BaseNode, Long> {

    @Query("MATCH (n:BaseNode) WHERE n.name=$0 RETURN n")
    List<BaseNode> findByName(String name);

}
