package com.zja.service;

import com.zja.entity.ArsModel;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/8/29 11:18
 */
public interface DataToShpService {
    List<ArsModel> getData();
    Integer getCount();
    Page<ArsModel> getPageData(Integer PageIndex, Integer PageSize);

    List<String> getValue();
}
