package com.zja.obfuscated.allatori.dao;

import com.zja.obfuscated.allatori.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author: zhengja
 * @since: 2024/01/26 15:33
 */
@Repository
public interface UserRepo extends
        JpaRepository<User, String>,
        CrudRepository<User, String>,
        JpaSpecificationExecutor<User> {

}
