package com.zja.dao;
import com.zja.entity.Project;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Project SQL
 * @author: zhengja
 * @since: 2024/09/27 9:28
 */
@Repository
public interface ProjectRepo extends JpaRepository<Project, String>, CrudRepository<Project, String>,
        JpaSpecificationExecutor<Project> {

    Optional<Project> findByName(String name);
}