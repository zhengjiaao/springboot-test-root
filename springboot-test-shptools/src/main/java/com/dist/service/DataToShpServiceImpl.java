package com.dist.service;

import com.dist.dao.ArsDao;
import com.dist.entity.ArsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/8/29 11:18
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DataToShpServiceImpl implements DataToShpService {

    @Autowired
    private ArsDao arsDao;

    @Override
    public List<ArsModel> getData(){
        List<ArsModel> arsModels = arsDao.findAllByReviewTaskIdAndReviewPointIdAndKey(2748465L,249466L ,"differentExtent");
        ////List<ArsModel> arsModels = arsDao.getData();
        System.out.println("arsModels="+arsModels);
        return arsModels;
    }

    @Override
    public Integer getCount(){
        Integer count = arsDao.getConut();
        System.out.println("count="+count);
        return count;
    }

    @Override
    public Page<ArsModel> getPageData(Integer PageIndex, Integer PageSize){
        //PageIndex当前页 0  ,  PageSize每页显示多少条
        Pageable pageable = PageRequest.of(PageIndex,PageSize);
        Page<ArsModel> arsModelPage = arsDao.findByKeyAndReviewPointIdAndReviewTaskId("differentExtent", 249466L, 2748465L, pageable);
        return arsModelPage;
    }

    @Override
    public List<String> getValue(){
        return arsDao.queValue();
    }
}
