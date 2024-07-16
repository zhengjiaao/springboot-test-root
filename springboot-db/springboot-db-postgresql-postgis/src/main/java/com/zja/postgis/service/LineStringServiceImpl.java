package com.zja.postgis.service;

import com.zja.postgis.dao.LineStringEntityRepo;
import com.zja.postgis.model.LineStringEntity;
import com.zja.postgis.mapper.LineStringMapper;
import com.zja.postgis.model.dto.LineStringDTO;
import com.zja.postgis.model.dto.PageData;
import com.zja.postgis.model.request.LineStringPageRequest;
import com.zja.postgis.model.request.LineStringRequest;
import com.zja.postgis.model.request.LineStringUpdateRequest;
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
 * LineString 业务处理服务实现层
 * @author: zhengja
 * @since: 2024/07/15 13:41
 */
@Slf4j
@Service
@Transactional
public class LineStringServiceImpl implements LineStringService {

    @Autowired
    LineStringEntityRepo repo;

    @Autowired
    LineStringMapper mapper;

    private LineStringEntity getLineStringById(String id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("传入的 id 有误，id=" + id));
    }

    @Override
    public LineStringDTO findById(String id) {
        LineStringEntity entity = this.getLineStringById(id);
        return mapper.map(entity);
    }

    @Override
    public PageData<LineStringDTO> pageList(LineStringPageRequest request) {
        int page = request.getPage();
        int size = request.getSize();
        Sort sort = Sort.by(Sort.Direction.DESC, "name");

        // 查询条件
        Specification<LineStringEntity> spec = buildQuery(request);
        // 分页查询
        Page<LineStringEntity> sourcePage = repo.findAll(spec, PageRequest.of(page, size, sort));

        return PageData.of(mapper.mapList(sourcePage.getContent()), page, size, sourcePage.getTotalElements());
    }

    private Specification<LineStringEntity> buildQuery(LineStringPageRequest request) {
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
    public LineStringDTO add(LineStringRequest request) {
        LineStringEntity entity = mapper.map(request);
        entity = repo.save(entity);

        return mapper.map(entity);
    }

    @Override
    public List<LineStringDTO> addBatch(List<LineStringRequest> LineStringRequests) {
        return LineStringRequests.stream().map(this::add).collect(Collectors.toList());
    }

    @Override
    public LineStringDTO update(String id, LineStringUpdateRequest request) {
        // 校验存在
        LineStringEntity entity = this.getLineStringById(id);

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