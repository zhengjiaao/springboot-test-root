package com.zja.table;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: zhengja
 * @Date: 2025-10-21 13:51
 */
@Repository
public interface TableDataRepository extends JpaRepository<TableDataEntity, Long> {
}