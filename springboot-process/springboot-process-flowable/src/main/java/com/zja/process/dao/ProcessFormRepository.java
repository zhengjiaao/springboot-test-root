package com.zja.process.dao;

import com.zja.process.model.ProcessForm;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ProcessForm SQL
 *
 * @author: zhengja
 * @since: 2025/08/15 14:46
 */
@Repository
public interface ProcessFormRepository extends JpaRepository<ProcessForm, String>, CrudRepository<ProcessForm, String>,
        JpaSpecificationExecutor<ProcessForm> {

    List<ProcessForm> findByProcessInstanceId(String processInstanceId);
}