package com.dist.daotest;

import com.dist.base.BaseTest;
import com.dist.entity.duke.DukeUserEntity;
import com.dist.entity.gxtz.GxtzUserEntity;
import com.dist.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/7/9 10:41
 */
public class TestDao extends BaseTest{

    private final Logger log = LoggerFactory.getLogger(TestDao.class);

    @Resource
    private UserService userService;

    @Before
    public void first(){
        this.setClassName("UserService");
        this.setLog(log);
    }

    @Test
    public void saveDukeUser(){
        DukeUserEntity dukeUserEntity = new DukeUserEntity();
        dukeUserEntity.setName("lisi");
        dukeUserEntity.setAge("23");
        dukeUserEntity.setCreateTime(new Date());
        dukeUserEntity.setGuid("1");
        DukeUserEntity saveDukeUser = userService.saveDukeUser(dukeUserEntity);
        this.sayEntity(saveDukeUser,"DukeUserEntity","saveDukeUser");
    }

    @Test
    public void saveGxtzUser(){
        GxtzUserEntity gxtzUserEntity = new GxtzUserEntity();
        gxtzUserEntity.setName("zhangsan");
        gxtzUserEntity.setAge("24");
        gxtzUserEntity.setCreateTime(new Date());
        gxtzUserEntity.setGuid("1");
        GxtzUserEntity saveGxtzUser = userService.saveGxtzUser(gxtzUserEntity);
        this.sayEntity(saveGxtzUser,"GxtzUserEntity","saveGxtzUser");
    }

    @Test
    public void getAllDukeUser(){
        List<DukeUserEntity> dukeUserEntityList = new ArrayList<>();
        this.sayList(dukeUserEntityList,"dukeUserEntityList","getAllDukeUser");
    }

    @Test
    public void getAllGxtzUser(){
        List<GxtzUserEntity> gxtzUserEntityList = new ArrayList<>();
        this.sayList(gxtzUserEntityList,"gxtzUserEntityList","getAllGxtzUser");
    }
}
