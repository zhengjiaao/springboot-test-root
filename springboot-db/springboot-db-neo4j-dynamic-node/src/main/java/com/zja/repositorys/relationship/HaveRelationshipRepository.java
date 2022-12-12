package com.zja.repositorys.relationship;

import com.zja.entity.relationship.HaveRelationship;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-08-25 11:16
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：拥有关系=HAVE
 */
public interface HaveRelationshipRepository extends Neo4jRepository<HaveRelationship, Long> {

    List<HaveRelationship> findByRelName(String relName);

    List<HaveRelationship> findByRelType(Integer relType);

    @Query("MATCH (n:Customer)-[r]->(m:Car) WHERE m.carBrand=$0 RETURN n,r,m")
    List<HaveRelationship> queryByCarBrand(String carBrand);

    /**
     * 获取与该节点相关的关系
     */
    @Query("MATCH (n:Customer)-[r]->(m) WHERE n.customerName=$0 RETURN n,r,m")
    List<HaveRelationship> queryAllByCustomerName(String customerName);

    @Query("MATCH (n:Customer)-[r]->(m:Car) WHERE n.customerName=$0 RETURN n,r,m")
    List<HaveRelationship> queryCarNodeAllByCustomerName(String customerName);

}
