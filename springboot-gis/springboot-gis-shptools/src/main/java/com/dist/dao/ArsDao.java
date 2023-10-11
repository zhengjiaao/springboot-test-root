package com.zja.dao;

import com.zja.entity.ArsModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/8/29 11:04
 */
public interface ArsDao extends JpaRepository<ArsModel,Long>{

    @Query(value = "select ars.id as id,ars.value as value,ars.REVIEWTASKID as reviewTaskId,ars.REVIEWPOINTID as reviewPointId  from ARS_MODEL_CALC_RESULT ars where ars.KEY = 'differentExtent' and ars.REVIEWPOINTID = 249466 and ars.REVIEWTASKID = 2748465",nativeQuery = true)
    List<ArsModel> getData();

    List<ArsModel> findAllByReviewTaskIdAndReviewPointIdAndKey(Long reviewTaskId, Long reviewPointId, String key);

    //快速查询数据总数
    @Query(value = "select /*+ INDEX(ARS_MODEL_CALC_RESULT ID) */ count(*)  from ARS_MODEL_CALC_RESULT t where KEY = 'differentExtent' and REVIEWPOINTID = 249466 and REVIEWTASKID = 2748465",nativeQuery = true)
    Integer getConut();

    Page<ArsModel> findByKeyAndReviewPointIdAndReviewTaskId(String key, Long reviewPointId, Long reviewTaskId, Pageable pageable);

    @Query(value = "SELECT ars2.value FROM (SELECT ROWNUM AS rowno, ars.value as value\n" +
            "FROM ARS_MODEL_CALC_RESULT  ars\n" +
            "WHERE ars.KEY = 'differentExtent' and ars.REVIEWPOINTID = 249466 and ars.REVIEWTASKID = 2748465 and ROWNUM <= 10) ars2\n" +
            "WHERE ars2.rowno >= 0",nativeQuery = true)
    List<String> queValue();
}
