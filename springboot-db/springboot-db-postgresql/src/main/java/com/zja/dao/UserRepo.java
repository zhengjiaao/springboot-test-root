package com.zja.dao;

import com.zja.entity.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * User SQL
 *
 * @author: zhengja
 * @since: 2026/02/06 10:03
 */
@Repository
public interface UserRepo extends JpaRepository<User, String>, CrudRepository<User, String>,
        JpaSpecificationExecutor<User> {

    Optional<User> findByName(String name);
}