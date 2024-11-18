package com.zja.hanbian.dao;

import com.zja.hanbian.entity.项目;
import com.zja.hanbian.封装.注解.数据库.存储库;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * 项目 SQL
 *
 * @作者: zhengja
 * @时间: 2024/11/11 11:00
 */
@存储库
公共 接口 项目存储库 extends JpaRepository<项目, String>, CrudRepository<项目, String>,
        JpaSpecificationExecutor<项目> {

    Optional<项目> findByName(String name);
}