package com.zja.webflux.dao;

import com.zja.webflux.model.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @Author: zhengja
 * @Date: 2025-04-16 11:20
 */
public interface UserRepository extends ReactiveCrudRepository<User, Long> {
    @Query("SELECT * FROM users WHERE name LIKE :name ORDER BY id LIMIT :size OFFSET :offset")
    Flux<User> findByNameContaining(String name, int offset, int size);

    @Query("SELECT COUNT(*) FROM users WHERE name LIKE :name")
    Mono<Long> countByNameContaining(String name);
}