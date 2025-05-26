package com.zja.db.kingbase8.dao;

import com.zja.db.kingbase8.model.Project;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Project SQL
 *
 * @author: zhengja
 * @since: 2025/05/26 15:40
 */
@Repository
public interface ProjectRepo extends JpaRepository<Project, String>, CrudRepository<Project, String>,
        JpaSpecificationExecutor<Project> {

    Optional<Project> findByName(String name);
}