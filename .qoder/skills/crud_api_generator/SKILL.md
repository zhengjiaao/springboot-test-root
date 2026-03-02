---
name: crud_api_generator
description: 快速生成CRUD+接口API代码的技能，基于项目现有的分层架构模式
---

# API代码生成器

此技能用于快速生成Spring Boot项目的CRUD（创建、读取、更新、删除）接口API代码。基于项目现有的架构模式，包含Entity、Repository、Service、ServiceImpl、Controller、Mapper以及DTO/Request/Response等组件。

## 组件结构

生成的代码遵循以下架构模式：

```
Entity (JPA Entity)
  ↓
Repository (JpaRepository)
  ↓
Service (Interface)
  ↓
ServiceImpl (Implementation)
  ↓
Mapper (MapStruct Mapper)
  ↓
DTO/Request/Response (Data Transfer Objects)
  ↓
Controller (REST Controller)
```

## 代码模板

### 1. Entity类模板

```java
package com.zja.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * {EntityName} 实体类
 *
 * @author: zhengja
 * @since: {date} {time}
 */
@Getter
@Setter
@Entity
@Table(name = "{table_name}")
@EntityListeners(value = AuditingEntityListener.class)
public class {EntityName} {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "VARCHAR(36)")
    private String id;

    /**
     * {field_name}
     */
    @Column(name = "{column_name}", nullable = false)
    private String {fieldName};

    /**
     * 创建时间
     */
    @Column(name = "create_time", nullable = false)
    @CreatedDate
    private LocalDateTime createTime;

    /**
     * 最后一次修改时间
     */
    @Column(name = "last_modified_date")
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
}
```

### 2. Repository接口模板

```java
package com.zja.dao;

import com.zja.entity.{EntityName};
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * {EntityName} 数据访问层
 *
 * @author: zhengja
 * @since: {date} {time}
 */
@Repository
public interface {EntityName}Dao extends JpaRepository<{EntityName}, String>, 
        CrudRepository<{EntityName}, String>, JpaSpecificationExecutor<{EntityName}> {

    /**
     * 根据{field_name}查询{entity_name}
     *
     * @param {fieldName} {field_name}
     * @return Optional<{EntityName}>
     */
    Optional<{EntityName}> findBy{FieldName}(String {fieldName});
}
```

### 3. Service接口模板

```java
package com.zja.service;

import com.zja.model.base.PageData;
import com.zja.model.dto.{EntityName}DTO;
import com.zja.model.request.{EntityName}PageRequest;
import com.zja.model.request.{EntityName}Request;
import com.zja.model.request.{EntityName}UpdateRequest;

import java.util.List;

/**
 * {EntityName} 服务层
 *
 * @author: zhengja
 * @since: {date} {time}
 */
public interface {EntityName}Service {

    /**
     * 查询{entity_name}
     *
     * @param id {entity_name}id
     */
    {EntityName}DTO queryById(String id);

    /**
     * 查询{entity_name}列表
     */
    List<{EntityName}DTO> list();

    /**
     * 分页查询{entity_name}
     */
    PageData<{EntityName}DTO> pageList({EntityName}PageRequest request);

    /**
     * 校验{field_name}是否可用
     *
     * @param {fieldName} {field_name}
     * @return Boolean
     */
    Boolean exist{FieldName}(String {fieldName});

    /**
     * 新增{entity_name}
     */
    {EntityName}DTO add({EntityName}Request request);

    /**
     * 批量添加{entity_name}
     */
    List<{EntityName}DTO> addBatch(List<{EntityName}Request> requests);

    /**
     * 更新{entity_name}
     *
     * @param id {entity_name}id
     */
    {EntityName}DTO update(String id, {EntityName}UpdateRequest request);

    /**
     * 删除{entity_name}
     *
     * @param id {entity_name}id
     */
    boolean deleteById(String id);

    /**
     * 批量删除{entity_name}
     *
     * @param ids {entity_name}ids
     */
    void deleteBatch(List<String> ids);
}
```

### 4. Service实现类模板

```java
package com.zja.service.impl;

import com.zja.dao.{EntityName}Dao;
import com.zja.entity.{EntityName};
import com.zja.mapper.{EntityName}Mapper;
import com.zja.model.base.PageData;
import com.zja.model.dto.{EntityName}DTO;
import com.zja.model.request.{EntityName}PageRequest;
import com.zja.model.request.{EntityName}Request;
import com.zja.model.request.{EntityName}UpdateRequest;
import com.zja.service.{EntityName}Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * {EntityName} 业务处理服务实现层
 *
 * @author: zhengja
 * @since: {date} {time}
 */
@Slf4j
@Service
@Transactional
public class {EntityName}ServiceImpl implements {EntityName}Service {

    @Autowired
    {EntityName}Dao dao;

    @Autowired
    {EntityName}Mapper mapper;

    private {EntityName} get{EntityName}ById(String id) {
        return dao.findById(id).orElseThrow(() -> new RuntimeException(String.format("传入的 id=%s 有误！", id)));
    }

    @Override
    public {EntityName}DTO queryById(String id) {
        {EntityName} entity = this.get{EntityName}ById(id);
        return mapper.toDto(entity);
    }

    @Override
    public List<{EntityName}DTO> list() {
        List<{EntityName}> list = dao.findAll();
        return mapper.toDtoList(list);
    }

    @Override
    public PageData<{EntityName}DTO> pageList({EntityName}PageRequest request) {
        int page = request.getPage();
        int size = request.getSize();
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");

        // 查询条件
        Specification<{EntityName}> spec = buildQuery(request);
        // 分页查询
        Page<{EntityName}> sourcePage = dao.findAll(spec, PageRequest.of(page, size, sort));

        return PageData.of(sourcePage.map(mapper::toDto));
    }

    private Specification<{EntityName}> buildQuery({EntityName}PageRequest request) {
        // 构建查询条件
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // 根据实际需求添加查询条件
            if (request.get{FieldName}() != null && !request.get{FieldName}().isEmpty()) {
                predicates.add(cb.like(root.get("{fieldName}"), "%" + request.get{FieldName}() + "%"));
            }
            
            // 将条件连接在一起
            return query.where(predicates.toArray(new Predicate[0])).getRestriction();
        };
    }

    @Override
    public Boolean exist{FieldName}(String {fieldName}) {
        Optional<{EntityName}> by{FieldName} = dao.findBy{FieldName}({fieldName});
        return by{FieldName}.isPresent();
    }

    @Override
    @Transactional
    public {EntityName}DTO add({EntityName}Request request) {
        // 检验名字已存在
        if (exist{FieldName}(request.get{FieldName}())) {
            throw new UnsupportedOperationException("{field_name}不能重复");
        }

        {EntityName} entity = mapper.toEntity(request);
        entity = dao.save(entity);

        return mapper.toDto(entity);
    }

    @Override
    @Transactional
    public List<{EntityName}DTO> addBatch(List<{EntityName}Request> requests) {
        return requests.stream().map(this::add).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public {EntityName}DTO update(String id, {EntityName}UpdateRequest request) {
        // 校验存在
        {EntityName} entity = this.get{EntityName}ById(id);

        // 更新
        mapper.updateEntityFromRequest(request, entity);
        entity = dao.save(entity);

        return mapper.toDto(entity);
    }

    @Override
    @Transactional
    public boolean deleteById(String id) {
        try {
            if (dao.findById(id).isPresent()) {
                dao.deleteById(id);
            }
            return true;
        } catch (Exception e) {
            log.error("Failed to delete entity with ID: {}", id, e);
            return false;
        }
    }

    @Override
    @Transactional
    public void deleteBatch(List<String> ids) {
        ids.forEach(this::deleteById);
    }
}
```

### 5. Mapper接口模板

```java
package com.zja.mapper;

import com.zja.entity.{EntityName};
import com.zja.model.dto.{EntityName}DTO;
import com.zja.model.request.{EntityName}Request;
import com.zja.model.request.{EntityName}UpdateRequest;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * {EntityName}映射器
 *
 * @author: zhengja
 * @since: {date} {time}
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface {EntityName}Mapper {

    {EntityName}Mapper INSTANCE = Mappers.getMapper({EntityName}Mapper.class);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "lastModifiedDate", ignore = true)
    })
    {EntityName} toEntity({EntityName}Request request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest({EntityName}UpdateRequest request, @MappingTarget {EntityName} entity);

    @Mappings({
            @Mapping(target = "createTime", dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(target = "lastModifiedDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    })
    {EntityName}DTO toDto({EntityName} entity);

    List<{EntityName}DTO> toDtoList(List<{EntityName}> entities);
}
```

### 6. DTO类模板

```java
package com.zja.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * {EntityName} 数据传输
 *
 * @author: zhengja
 * @since: {date} {time}
 */
@Data
@ApiModel("{EntityName}DTO")
public class {EntityName}DTO implements Serializable {
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("{field_name}")
    private String {fieldName};

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("最后一次修改时间")
    private LocalDateTime lastModifiedDate;
}
```

### 7. Request类模板

```java
package com.zja.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * {EntityName} 请求参数
 *
 * @author: zhengja
 * @since: {date} {time}
 */
@Data
@ApiModel("{EntityName}Request 新增 或 更新 {entity_name}信息")
public class {EntityName}Request implements Serializable {

    @ApiModelProperty(value = "{field_name}", required = true)
    @NotBlank(message = "{field_name}不能为空")
    private String {fieldName};
}
```

### 8. UpdateRequest类模板

```java
package com.zja.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * {EntityName} 更新请求参数
 *
 * @author: zhengja
 * @since: {date} {time}
 */
@Data
@ApiModel("{EntityName}UpdateRequest 更新 {entity_name}信息")
public class {EntityName}UpdateRequest implements Serializable {

    @ApiModelProperty("{field_name}")
    private String {fieldName};
}
```

### 9. PageRequest类模板

```java
package com.zja.model.request;

import com.zja.model.base.BasePageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * {EntityName} 分页参数
 *
 * @author: zhengja
 * @since: {date} {time}
 */
@Getter
@Setter
@ApiModel("{EntityName}PageRequest")
public class {EntityName}PageRequest extends BasePageRequest {
    @ApiModelProperty("{field_name}")
    private String {fieldName};
}
```

### 10. Controller类模板

```java
package com.zja.controller;

import com.zja.model.base.PageData;
import com.zja.model.dto.{EntityName}DTO;
import com.zja.model.request.{EntityName}PageRequest;
import com.zja.model.request.{EntityName}Request;
import com.zja.model.request.{EntityName}UpdateRequest;
import com.zja.service.{EntityName}Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * {entity_name} 接口层（一般与页面、功能对应）
 *
 * @author: zhengja
 * @since: {date} {time}
 */
@CrossOrigin
@RestController
@RequestMapping("/rest/{entity_name}")
@Api(tags = {"{entity_name}管理页面"})
public class {EntityName}Controller {

    @Autowired
    {EntityName}Service service;

    @GetMapping("/query/{id}")
    @ApiOperation("查询单个{entity_name}详情")
    public {EntityName}DTO queryById(@NotBlank @PathVariable("id") String id) {
        return service.queryById(id);
    }

    @GetMapping("/list")
    @ApiOperation("查询{entity_name}列表")
    public List<{EntityName}DTO> list() {
        return service.list();
    }

    @GetMapping("/page/list")
    @ApiOperation("分页查询{entity_name}列表")
    public PageData<{EntityName}DTO> pageList(@Valid {EntityName}PageRequest pageRequest) {
        return service.pageList(pageRequest);
    }

    @PostMapping("/add")
    @ApiOperation("添加{entity_name}")
    public {EntityName}DTO add(@Valid @RequestBody {EntityName}Request request) {
        return service.add(request);
    }

    @PostMapping("/add/batch")
    @ApiOperation("批量添加{entity_name}")
    public List<{EntityName}DTO> add(@Valid @RequestBody List<{EntityName}Request> requests) {
        return service.addBatch(requests);
    }

    @PutMapping("/update/{id}")
    @ApiOperation("更新{entity_name}")
    public {EntityName}DTO update(@NotBlank @PathVariable("id") String id,
                                 @Valid @RequestBody {EntityName}UpdateRequest updateRequest) {
        return service.update(id, updateRequest);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除{entity_name}")
    public boolean deleteById(@NotBlank @PathVariable("id") String id) {
        return service.deleteById(id);
    }

    @DeleteMapping("/delete/batch")
    @ApiOperation("批量删除{entity_name}")
    public void deleteBatch(@RequestBody List<String> ids) {
        service.deleteBatch(ids);
    }
}
```

### 11. BasePageRequest类模板

- 若 `BasePageRequest` 类不存在，请创建它。否则请跳过此步骤。

```java
package com.dist.xdata.ai.model.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 分页请求（通用的）
 *
 * @author: zhengja
 * @since: {date} {time}
 */
@Setter
@Getter
@ApiModel("BasePageRequest")
public class BasePageRequest implements Serializable {
    @ApiModelProperty("页码 从第1页开始 默认 1")
    private Integer page = 1;
    @ApiModelProperty("每页数量 默认 10")
    private Integer size = 10;

    public Integer getPage() {
        // 确保返回值最小为0，避免负数
        return Math.max(0, page - 1); // 注：jpa page 从 0 开始
    }
}
```

## 使用说明

1. 根据需要替换模板中的占位符：
   - `{EntityName}`: 实体类名（首字母大写）
   - `{entity_name}`: 实体类名（小写）
   - `{field_name}`: 字段名称（中文）
   - `{fieldName}`: 字段名称（驼峰命名）
   - `{FieldName}`: 字段名称（首字母大写）
   - `{table_name}`: 数据库表名
   - `{date}`: 当前日期
   - `{time}`: 当前时间

2. 按照以下顺序创建组件：
   - Entity → Repository → Service → ServiceImpl → Mapper → DTO/Request → Controller

3. 配置必要的依赖：
   - Spring Data JPA
   - MapStruct
   - Validation
   - Swagger (io.springfox或springdoc)

4. 确保在启动类上添加 `@EnableJpaAuditing` 注解以启用审计功能。

## 注意事项

1. Entity类使用UUID作为主键生成策略
2. 时间字段使用 `@CreatedDate` 和 `@LastModifiedDate` 进行自动填充
3. 所有接口方法都有相应的文档注释
4. 添加了输入验证注解如 `@NotBlank`、`@Valid`
5. 分页查询使用 `PageRequest` 和 `Specification` 实现动态查询
6. Service层实现事务管理
7. 使用MapStruct进行对象转换，避免手动编写转换代码