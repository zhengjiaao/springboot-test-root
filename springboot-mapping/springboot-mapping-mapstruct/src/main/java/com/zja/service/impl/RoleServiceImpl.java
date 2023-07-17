/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-07-17 13:39
 * @Since:
 */
package com.zja.service.impl;

import com.zja.entity.Role;
import com.zja.mapper.RoleMapper;
import com.zja.model.dto.RoleDTO;
import com.zja.model.request.RolePageSearchRequest;
import com.zja.model.request.RoleRequest;
import com.zja.model.request.RoleUpdateRequest;
import com.zja.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @author: zhengja
 * @since: 2023/07/17 13:39
 */
@Slf4j
@Validated
@Service
public class RoleServiceImpl implements RoleService {

//    @Autowired
//    RoleRepo repo;

    @Autowired
    RoleMapper mapper;

    /**
     * 生成模拟数据
     */
    private Role generateAnalogData() {
        Role role = new Role();
        role.setId(UUID.randomUUID().toString());
        role.setName("角色" + new Random().nextInt(100));
        role.setRemarks("角色描述");
        return role;
    }

    @Override
    public RoleDTO findById(String id) {
        Role entity = generateAnalogData();
        return mapper.map(entity);
    }

    @Override
    public List<RoleDTO> pageList(RolePageSearchRequest pageSearchRequest) {

//        Page<Role> sourcePage = repo.findAll(spec, pageable);

        List<Role> roleList = Arrays.asList(
                generateAnalogData()
                , generateAnalogData()
                , generateAnalogData()
        );

        return mapper.mapList(roleList);
    }

    @Override
    public RoleDTO save(RoleRequest request) {
        Role entity = mapper.map(request);
//        repo.save(entity);
        return mapper.map(entity);
    }

    @Override
    public RoleDTO update(String id, RoleUpdateRequest updateRequest) {
//        Role entity = repo.findById(id).orElseThrow(() -> new ArgumentNotValidException("传入的 id 有误！"));
        Role entity = generateAnalogData();
        //更新属性
        BeanUtils.copyProperties(updateRequest, entity);
//        entity = repo.save(entity);
        return mapper.map(entity);
    }

    @Override
    public void deleteById(String id) {
//        if (!repo.findById(id).isPresent()) {
//            return;
//        }
//        repo.deleteById(id);
    }

}