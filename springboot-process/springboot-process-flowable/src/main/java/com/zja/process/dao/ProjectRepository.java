package com.zja.process.dao;

import com.zja.process.model.Project;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Project SQL
 *
 * @author: zhengja
 * @since: 2025/10/23 15:07
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>, CrudRepository<Project, Long>,
        JpaSpecificationExecutor<Project> {

    Optional<Project> findByProjectName(String projectName);
}