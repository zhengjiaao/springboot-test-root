package com.zja.postgis.service;

import com.zja.postgis.dao.PolygonEntityRepo;
import com.zja.postgis.mapper.PolygonMapper;
import com.zja.postgis.model.PolygonEntity;
import com.zja.postgis.model.dto.PageData;
import com.zja.postgis.model.dto.PolygonDTO;
import com.zja.postgis.model.request.PolygonPageRequest;
import com.zja.postgis.model.request.PolygonRequest;
import com.zja.postgis.model.request.PolygonUpdateRequest;
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
 * Polygon 业务处理服务实现层
 * @author: zhengja
 * @since: 2024/07/15 13:40
 */
@Slf4j
@Service
@Transactional
public class PolygonServiceImpl implements PolygonService {

    @Autowired
    PolygonEntityRepo repo;

    @Autowired
    PolygonMapper mapper;

    private PolygonEntity getPolygonById(String id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("传入的 id 有误，id=" + id));
    }

    @Override
    public PolygonDTO findById(String id) {
        PolygonEntity entity = this.getPolygonById(id);
        return mapper.map(entity);
    }

    @Override
    public PageData<PolygonDTO> pageList(PolygonPageRequest request) {
        int page = request.getPage();
        int size = request.getSize();
        Sort sort = Sort.by(Sort.Direction.DESC, "name");

        // 查询条件
        Specification<PolygonEntity> spec = buildQuery(request);
        // 分页查询
        Page<PolygonEntity> sourcePage = repo.findAll(spec, PageRequest.of(page, size, sort));

        return PageData.of(mapper.mapList(sourcePage.getContent()), page, size, sourcePage.getTotalElements());
    }

    private Specification<PolygonEntity> buildQuery(PolygonPageRequest request) {
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
    public PolygonDTO add(PolygonRequest request) {
        PolygonEntity entity = mapper.map(request);
        entity = repo.save(entity);

        return mapper.map(entity);
    }

    @Override
    public List<PolygonDTO> addBatch(List<PolygonRequest> PolygonRequests) {
        return PolygonRequests.stream().map(this::add).collect(Collectors.toList());
    }

    @Override
    public PolygonDTO update(String id, PolygonUpdateRequest request) {
        // 校验存在
        PolygonEntity entity = this.getPolygonById(id);

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