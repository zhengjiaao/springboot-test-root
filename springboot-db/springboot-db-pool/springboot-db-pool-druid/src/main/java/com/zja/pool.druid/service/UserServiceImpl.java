/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-08-07 17:34
 * @Since:
 */
package com.zja.pool.druid.service;

import com.zja.pool.druid.dao.UserRepo;
import com.zja.pool.druid.entity.User;
import com.zja.pool.druid.mapper.UserMapper;
import com.zja.pool.druid.model.dto.PageData;
import com.zja.pool.druid.model.dto.UserDTO;
import com.zja.pool.druid.model.request.UserPageSearchRequest;
import com.zja.pool.druid.model.request.UserRequest;
import com.zja.pool.druid.model.request.UserUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhengja
 * @since: 2023/08/07 17:34
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepo repo;

    @Autowired
    UserMapper mapper;


    @Override
    public UserDTO findById(String id) {
        User entity = repo.findById(id).orElseThrow(() -> new RuntimeException("传入的 id 有误！"));
        return mapper.map(entity);
    }

    @Override
    public PageData<UserDTO> pageList(UserPageSearchRequest pageSearchRequest) {

        //分页参数
        int page = pageSearchRequest.getPage();
        int size = pageSearchRequest.getSize();
        //排序 and 分页
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");

        Pageable pageable = PageRequest.of(page, size, sort);
        Specification<User> spec = buildSpecification(pageSearchRequest);
        Page<User> sourcePage = repo.findAll(spec, pageable);

        return PageData.of(mapper.mapList(sourcePage.getContent()), page, size, sourcePage.getTotalElements());
    }

    private Specification<User> buildSpecification(UserPageSearchRequest pageSearchRequest) {
        return (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 关键词
            if (!StringUtils.isEmpty(pageSearchRequest.getUsername())) {
                predicates.add(cb.equal(root.get("username"), pageSearchRequest.getUsername()));
            }
            // 将条件连接在一起
            return criteriaQuery.where(predicates.toArray(new Predicate[0])).getRestriction();
        };
    }

    @Override
    public UserDTO save(UserRequest request) {
        User entity = mapper.map(request);
        entity.setCreateTime(LocalDateTime.now());
        repo.save(entity);
        return mapper.map(entity);
    }

    @Override
    public UserDTO update(String id, UserUpdateRequest updateRequest) {
        User entity = repo.findById(id).orElseThrow(() -> new RuntimeException("传入的 id 有误！"));
        //更新属性
        BeanUtils.copyProperties(updateRequest, entity);
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