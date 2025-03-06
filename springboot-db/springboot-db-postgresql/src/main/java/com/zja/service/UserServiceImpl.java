package com.zja.service;

import com.zja.dao.UserRepo;
import com.zja.entity.User;
import com.zja.mapper.UserMapper;
import com.zja.model.base.PageData;
import com.zja.model.dto.UserDTO;
import com.zja.model.request.UserPageRequest;
import com.zja.model.request.UserRequest;
import com.zja.model.request.UserUpdateRequest;
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
 * User 业务处理服务实现层
 *
 * @author: zhengja
 * @since: 2025/03/05 14:03
 */
@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepo repo;

    @Autowired
    UserMapper mapper;

    private User getUserById(String id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException(String.format("传入的 id=%s 有误！", id)));
    }

    @Override
    public UserDTO queryById(String id) {
        User entity = this.getUserById(id);
        return mapper.map(entity);
    }

    @Override
    public List<UserDTO> list() {
        // 查询条件
        // Specification<User> spec = buildQuery(request);
        // 列表查询
        List<User> list = repo.findAll();
        return mapper.mapList(list);
    }

    @Override
    public PageData<UserDTO> pageList(UserPageRequest request) {
        int page = request.getPage();
        int size = request.getSize();
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");

        // 查询条件
        Specification<User> spec = buildQuery(request);
        // 分页查询
        Page<User> sourcePage = repo.findAll(spec, PageRequest.of(page, size, sort));

        return PageData.of(mapper.mapList(sourcePage.getContent()), page, size, sourcePage.getTotalElements());
    }

    private Specification<User> buildQuery(UserPageRequest request) {
        // 构建查询条件
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 关键词
            if (!StringUtils.isEmpty(request.getName())) {
                predicates.add(cb.like(root.get("name"), request.getName() + "%"));
                // predicates.add(cb.equal(root.get("name"), request.getName()));
            }
            // 将条件连接在一起
            return query.where(predicates.toArray(new Predicate[0])).getRestriction();
        };
    }

    @Override
    public Boolean existName(String name) {
        Optional<User> byName = repo.findByName(name);
        return byName.isPresent();
    }

    @Override
    public UserDTO add(UserRequest request) {
        // 检验名字已存在
        if (existName(request.getName())) {
            throw new UnsupportedOperationException("名称不能重复");
        }

        User entity = mapper.map(request);
        entity = repo.save(entity);

        return mapper.map(entity);
    }

    @Override
    public List<UserDTO> addBatch(List<UserRequest> requests) {
        return requests.stream().map(this::add).collect(Collectors.toList());
    }

    @Override
    public UserDTO update(String id, UserUpdateRequest request) {
        // 校验存在
        User entity = this.getUserById(id);

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