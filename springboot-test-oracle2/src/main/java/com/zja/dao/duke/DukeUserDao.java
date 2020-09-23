package com.zja.dao.duke;

import com.zja.entity.duke.DukeUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @program: springbootdemo
 * @Date: 2018/12/26 10:03
 * @Author: Mr.Zheng
 * @Description:
 */
public interface DukeUserDao extends JpaRepository<DukeUserEntity, Long>{
}
