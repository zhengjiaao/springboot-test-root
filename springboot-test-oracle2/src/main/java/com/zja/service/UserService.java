package com.zja.service;

import com.zja.entity.duke.DukeUserEntity;
import com.zja.entity.gxtz.GxtzUserEntity;

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
