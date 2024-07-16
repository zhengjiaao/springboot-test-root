package com.zja.postgis.service;

import com.zja.postgis.dao.PointEntityRepo;
import com.zja.postgis.mapper.PointMapper;
import com.zja.postgis.model.PointEntity;
import com.zja.postgis.model.dto.PageData;
import com.zja.postgis.model.dto.PointDTO;
import com.zja.postgis.model.request.PointPageRequest;
import com.zja.postgis.model.request.PointRequest;
import com.zja.postgis.model.request.PointUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import javax.persistence.criteria.Predicate;

/**
 * Point 业务处理服务实现层
 * @author: zhengja
 * @since: 2024/07/15 13:38
 */
@Slf4j
@Service
@Transactional
public class PointServiceImpl implements PointService {

    @Autowired
    PointEntityRepo repo;

    @Autowired
    PointMapper mapper;

    private PointEntity getPointById(String id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("传入的 id 有误，id=" + id));
    }

    @Override
    public PointDTO findById(String id) {
        PointEntity entity = this.getPointById(id);
        return mapper.map(entity);
    }

    @Override
    public PageData<PointDTO> pageList(PointPageRequest request) {
        int page = request.getPage();
        int size = request.getSize();
        Sort sort = Sort.by(Sort.Direction.DESC, "name");

        // 查询条件
        Specification<PointEntity> spec = buildQuery(request);
        // 分页查询
        Page<PointEntity> sourcePage = repo.findAll(spec, PageRequest.of(page, size, sort));

        return PageData.of(mapper.mapList(sourcePage.getContent()), page, size, sourcePage.getTotalElements());
    }

    private Specification<PointEntity> buildQuery(PointPageRequest request) {
        // 构建查询条件
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 关键词
            if (!StringUtils.isEmpty(request.getName())) {
                predicates.add(cb.equal(root.get("name"), request.getName()));
            }
            // 将条件连接在一起
            return query.where(predicates.toArray(new Predicate[0])).getRestriction();
        };
    }
    
    @Override
    public PointDTO add(PointRequest request) {
        PointEntity entity = mapper.map(request);
        entity = repo.save(entity);

        return mapper.map(entity);
    }

    @Override
    public List<PointDTO> addBatch(List<PointRequest> PointRequests) {
        return PointRequests.stream().map(this::add).collect(Collectors.toList());
    }

    @Override
    public PointDTO update(String id, PointUpdateRequest request) {
        // 校验存在
        PointEntity entity = this.getPointById(id);

        // 更新
        BeanUtils.copyProperties(request, entity);
        entity = repo.save(entity);

        return mapper.map(entity);
    }

    @Override
    public boolean deleteById(String id) {
        try {
            if (repo.findById(id).isPresent()) {
                repo.deleteById(id);
            }
            return true;
        } catch (Exception e) {
            log.error("Failed to delete entity with ID: {}", id, e);
            return false;
        }
    }

    public void deleteBatch(List<String> ids) {
        ids.forEach(this::deleteById);
    }

}