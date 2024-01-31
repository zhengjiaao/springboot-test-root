package com.zja.obfuscated.allatori.service.impl;

import com.zja.obfuscated.allatori.dao.UserRepo;
import com.zja.obfuscated.allatori.manager.UserManager;
import com.zja.obfuscated.allatori.mapper.UserMapper;
import com.zja.obfuscated.allatori.model.dto.PageData;
import com.zja.obfuscated.allatori.model.dto.UserDTO;
import com.zja.obfuscated.allatori.model.entity.User;
import com.zja.obfuscated.allatori.model.request.UserPageSearchRequest;
import com.zja.obfuscated.allatori.model.request.UserRequest;
import com.zja.obfuscated.allatori.model.request.UserUpdateRequest;
import com.zja.obfuscated.allatori.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhengja
 * @since: 2024/01/26 15:33
 */
@Slf4j
@Validated
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepo repo;

    @Autowired
    UserMapper mapper;

    @Autowired
    UserManager manager;

    @Override
    public UserDTO findById(String id) {
        User entity = repo.findById(id).orElseThrow(() -> new RuntimeException("传入的 id 有误！"));
        return mapper.map(entity);
    }

    @Override
    public PageData<UserDTO> pageList(UserPageSearchRequest request) {
        int page = request.getPage();
        int size = request.getSize();
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");

        // 查询条件
        Specification<User> spec = buildQuery(request);
        // 分页查询
        Page<User> sourcePage = repo.findAll(spec, PageRequest.of(page, size, sort));

        return PageData.of(mapper.mapList(sourcePage.getContent()), page, size, sourcePage.getTotalElements());
    }

    private Specification<User> buildQuery(UserPageSearchRequest request) {
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
    public UserDTO add(UserRequest request) {
        User entity = mapper.map(request);
        repo.save(entity);
        return mapper.map(entity);
    }

    @Override
    public UserDTO update(String id, UserUpdateRequest request) {
        User entity = repo.findById(id).orElseThrow(() -> new RuntimeException("传入的 id 有误！"));
        // 更新属性
        BeanUtils.copyProperties(request, entity);
        entity = repo.save(entity);
        return mapper.map(entity);
    }

    @Override
    public void deleteById(String id) {
        if (!repo.findById(id).isPresent()) {
            return;
        }
        repo.deleteById(id);
    }

}