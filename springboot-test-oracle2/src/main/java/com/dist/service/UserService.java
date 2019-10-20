package com.dist.service;

import com.dist.entity.duke.DukeUserEntity;
import com.dist.entity.gxtz.GxtzUserEntity;

import java.util.List;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/7/9 10:43
 */
public interface UserService {

    List<DukeUserEntity> getAllDukeUser();

    List<GxtzUserEntity> getAllGxtzUser();

    DukeUserEntity saveDukeUser(DukeUserEntity dukeUserEntity);

    GxtzUserEntity saveGxtzUser(GxtzUserEntity gxtzUserEntity);
}
