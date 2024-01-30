package com.zja.obfuscated.proguard.manager;

import com.zja.obfuscated.proguard.dao.UserRepo;
import com.zja.obfuscated.proguard.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author: zhengja
 * @since: 2024/01/30 13:39
 */
@Component
public class UserManager {

    @Autowired
    UserRepo repo;


    public Optional<User> findById(String id) {
        return repo.findById(id);
    }

    public User existById(String id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("传入的 id 有误！"));
    }

    public User save(User entity) {
        return repo.save(entity);
    }

    public void deleteById(String id) {
        if (!repo.findById(id).isPresent()) {
            return;
        }
        repo.deleteById(id);
    }

}