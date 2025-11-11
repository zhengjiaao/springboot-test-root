package com.zja.tree.dirtree.repository;

import com.zja.tree.dirtree.entity.DirectoryNode;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * DirectoryNode SQL
 *
 * @author: zhengja
 * @since: 2025/11/06 10:42
 */
@Repository
public interface DirectoryNodeRepository extends JpaRepository<DirectoryNode, Long>, CrudRepository<DirectoryNode, Long>,
        JpaSpecificationExecutor<DirectoryNode> {

    // 查找指定业务下的根节点 (parentId = -1)
    List<DirectoryNode> findByBusinessTypeAndParentId(String businessType, Long parentId);

    default List<DirectoryNode> findRootNodesByBusinessType(String businessType) {
        return findByBusinessTypeAndParentId(businessType, -1L);
    }

    // 根据父节点查找子节点
    List<DirectoryNode> findByParentId(Long parentId);

    // // 查找特定业务类型和父节点的子节点
    // List<DirectoryNode> findByBusinessTypeAndParentId(String businessType, Long parentId);

    // 检查业务ID是否存在
    boolean existsByBusinessTypeAndBusinessId(String businessType, String businessId);

    // 根据业务类型查找所有节点
    List<DirectoryNode> findByBusinessType(String businessType);

    // 根据业务类型统计节点数量
    long countByBusinessType(String businessType);

    // 根据父节点ID列表查找
    List<DirectoryNode> findByParentIdIn(List<Long> parentIds);

    // 查找指定业务类型下具有特定业务ID的节点
    Optional<DirectoryNode> findByBusinessTypeAndBusinessId(String businessType, String businessId);

    // 查找指定父节点的直接子节点数量
    long countByParentId(Long parentId);

    // 检查是否存在指定父节点的子节点
    boolean existsByParentId(Long parentId);

    // 使用原生查询获取子树（性能优化）
    @Query(value = "WITH RECURSIVE subtree AS (" +
            "    SELECT * FROM directory_node WHERE id = :nodeId " +
            "    UNION ALL " +
            "    SELECT dn.* FROM directory_node dn " +
            "    INNER JOIN subtree st ON dn.parent_id = st.id" +
            ") SELECT * FROM subtree", nativeQuery = true)
    List<DirectoryNode> findSubtree(@Param("nodeId") Long nodeId);

    // 查找指定业务类型下深度为0的节点（根节点）
    @Query("SELECT dn FROM DirectoryNode dn WHERE dn.businessType = :businessType AND dn.depth = 0")
    List<DirectoryNode> findRootNodesByBusinessTypeAndDepth(@Param("businessType") String businessType);
}