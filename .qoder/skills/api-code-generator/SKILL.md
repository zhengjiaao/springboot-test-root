---
name: api-code-generator
description: generator code SpringBoot CRUD Controller
---

# Parameters
- author: zhengja
- project-model: springboot-db/springboot-db-postgresql

# SpringBoot CRUD Controller 代码生成规范

## 1. 整体架构规范

### 1.1 项目结构
```
{{project-model}}/src/main/java/com/zja/
├── controller/          # 控制层
├── service/             # 服务层
├── impl/                # 服务实现层
├── dao/                 # 数据访问层
├── entity/              # 实体类
├── model/               # 数据模型
│   ├── base/            # 基础模型
│   ├── dto/             # 数据传输对象
│   └── request/         # 请求参数
└── mapper/              # 映射器
```

### 1.2 包命名规范
- 控制层: `com.zja.controller`
- 服务层: `com.zja.service`
- 服务实现层: `com.zja.service.impl`
- 数据访问层: `com.zja.dao`
- 实体类: `com.zja.entity`
- 数据模型: `com.zja.model`
- 映射器: `com.zja.mapper`

## 2. Entity实体类规范

### 2.1 基础注解
```java
@Entity
@Table(name = "t_{entity}_info")
@EntityListeners(value = AuditingEntityListener.class)
@Data
```

### 2.2 类注释模板
```java
/**
 * {实体中文名} 实体类
 *
 * @author: {{author}}
 * @since: {当前日期} {当前时间}
 */
```

### 2.3 字段注解规范

#### 2.3.1 ID字段
```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private String id;
```

#### 2.3.2 普通字段
```java
/**
 * {字段中文名}
 */
@Column(name = "{fieldName}", nullable = false)
private {FieldType} {fieldName};
```

#### 2.3.3 时间字段
```java
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
```

### 2.4 完整实体类示例
```java
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * {实体中文名} 实体类
 *
 * @author: {{author}}
 * @since: {当前日期} {当前时间}
 */
@Getter
@Setter
@Entity
@Table(name = "t_{entity}_info")
@EntityListeners(value = AuditingEntityListener.class)
public class {Entity} {

    @Id
    private String id;

    /**
     * {字段中文名}
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * {字段中文名}
     */
    @Column(name = "address")
    private String address;

    /**
     * {字段中文名}
     */
    @Column(name = "email")
    private String email;

    /**
     * {字段中文名}
     */
    @Column(name = "age")
    private Integer age;

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

## 3. Dao层规范

### 3.1 基础注解
```java
@Repository
```

### 3.2 接口继承规范
```java
public interface {Entity}Dao extends JpaRepository<{Entity}, String>, CrudRepository<{Entity}, String>, JpaSpecificationExecutor<{Entity}> {

    Optional<{Entity}> findByName(String name);
}
```

### 3.3 基本特性

#### 3.3.1 命名规范
- 接口名称以 `Dao` 或 `Repository` 结尾
- 例如：`UserDao`、`ProductRepository`

#### 3.3.2 继承规范
- 主要继承 `JpaRepository` 提供基本的CRUD操作
- 继承 `JpaSpecificationExecutor` 支持动态查询
- 可选择性继承 `CrudRepository` 扩展基本操作

#### 3.3.3 注解使用
- 使用 `@Repository` 注解标记数据访问层
- 对于自定义查询可以使用 `@Query` 注解

#### 3.3.4 自定义查询方法
```java
// 根据名称查询
Optional<{Entity}> findByName(String name);

// 根据其他字段查询
Optional<{Entity}> findByCode(String code);

// 根据条件查询列表
List<{Entity}> findByStatus(Integer status);

// 根据多个条件查询
List<{Entity}> findByNameAndStatus(String name, Integer status);
```

#### 3.3.5 JPQL查询注解
```java
// 使用JPQL查询
@Query("SELECT e FROM {Entity} e WHERE e.name = :name AND e.status = :status")
Optional<{Entity}> findByNameAndStatusCustom(@Param("name") String name, @Param("status") Integer status);

// 原生SQL查询
@Query(value = "SELECT * FROM t_{entity}_info WHERE name LIKE %:keyword%", nativeQuery = true)
List<{Entity}> findByNameContaining(@Param("keyword") String keyword);
```

### 3.4 完整Dao层示例
```java
import com.zja.entity.{Entity};
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * {实体中文名} 数据访问层
 *
 * @author: {{author}}
 * @since: {当前日期} {当前时间}
 */
@Repository
public interface {Entity}Dao extends JpaRepository<{Entity}, String>, CrudRepository<{Entity}, String>, JpaSpecificationExecutor<{Entity}> {

    /**
     * 根据{实体}名称查询{实体}
     *
     * @param name {实体}名称
     * @return Optional<{Entity}>
     */
    Optional<{Entity}> findByName(String name);

    /**
     * 根据{实体}编号查询{实体}
     *
     * @param code {实体}编号
     * @return Optional<{Entity}>
     */
    Optional<{Entity}> findByCode(String code);
    
    /**
     * 根据状态查询{实体}列表
     
     * @param status 状态
     * @return List<{Entity}>
     */
    List<{Entity}> findByStatus(Integer status);
    
    /**
     * 根据名称和状态查询{实体}
     
     * @param name {实体}名称
     * @param status 状态
     * @return Optional<{Entity}>
     */
    Optional<{Entity}> findByNameAndStatus(String name, Integer status);
}
```

## 4. Model层规范

### 4.1 DTO类规范
```java
/**
 * {Entity} 数据传输
 *
 * @author: {{author}}
 * @since: {当前日期} {当前时间}
 */
@Data
@ApiModel("{Entity}DTO")
public class {Entity}DTO implements Serializable {
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("{字段中文名}")
    private {FieldType} {fieldName};
    // ... 其他字段
}
```

### 4.2 Request类规范
```java
/**
 * {实体} 请求参数
 *
 * @author: {{author}}
 * @since: {当前日期} {当前时间}
 */
@Data
@ApiModel("{Entity}Request 新增 或 更新 {实体}信息")
public class {Entity}Request implements Serializable {

    @ApiModelProperty("{字段中文名}")
    private {FieldType} {fieldName};
    // ... 其他字段
}
```

### 4.3 UpdateRequest类规范
```java
/**
 * {实体} 更新请求参数
 *
 * @author: {{author}}
 * @since: {当前日期} {当前时间}
 */
@Data
@ApiModel("{Entity}UpdateRequest 更新 {实体}信息")
public class {Entity}UpdateRequest implements Serializable {

    @ApiModelProperty("{字段中文名}")
    private {FieldType} {fieldName};
    // ... 其他字段
}
```

### 4.4 PageRequest类规范
```java
/**
 * {实体} 分页参数
 *
 * @author: {{author}}
 * @since: {当前日期} {当前时间}
 */
@Getter
@Setter
@ApiModel("{Entity} 分页参数")
public class {Entity}PageRequest extends BasePageRequest {
    @ApiModelProperty("{字段中文名}")
    private {FieldType} {fieldName};
    // ... 其他查询条件字段
}
```

## 5. 基础类规范

### 5.1 BasePageRequest
```java
/**
 * 分页请求（通用的）
 *
 * @author: {{author}}
 * @since: 2024/09/27 9:31
 */
@Setter
@ApiModel("BasePageRequest")
public class BasePageRequest implements Serializable {
    @ApiModelProperty("页码 从第1页开始")
    private Integer page = 1;
    @Getter
    @ApiModelProperty("每页数量 默认 10")
    private Integer size = 10;

    public Integer getPage() {
        return page - 1; // 注：jpa page 从 0 开始
    }
}
```

### 5.2 PageData
```java
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.function.Function;

/**
 * 分页数据传输
 *
 * @author: {{author}}
 * @since: 2024/09/27 9:31
 */
@Data
@ApiModel("PageData")
public final class PageData<T> implements Serializable {
    @ApiModelProperty("数据")
    private List<T> data;
    @ApiModelProperty("当前页号")
    private int index;
    @ApiModelProperty("页大小")
    private int size;
    @ApiModelProperty("查询到的数据量")
    private int length;
    @ApiModelProperty("总数据量")
    private long count;
    @ApiModelProperty("总页数")
    private int pages;
    @ApiModelProperty("是否第一页")
    private boolean first;
    @ApiModelProperty("是否最后一页")
    private boolean last;
    @ApiModelProperty("是否有下一页")
    private boolean hasNext;
    @ApiModelProperty("是否有上一页")
    private boolean hasPrev;

    public static <T> PageData<T> of(Page<T> page) {
        return of(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements());
    }

    public static <T> PageData<T> of(List<T> data, int pageIndex, int pageSize, long totalSize) {
        PageData<T> dp = new PageData<>();
        dp.data = data;
        dp.index = pageIndex;
        dp.size = pageSize;
        dp.length = data.size();
        dp.count = totalSize;
        dp.pages = BigDecimal.valueOf(totalSize).divide(BigDecimal.valueOf(pageSize), RoundingMode.UP).intValue();
        dp.first = pageIndex == 0;
        dp.hasPrev = !dp.first;
        dp.hasNext = (dp.pages - dp.index) > 1;
        dp.last = !dp.hasNext;
        return dp;
    }

    public <R> PageData<R> map(Function<List<T>, List<R>> mapper) {
        return of(mapper.apply(this.getData()), this.getIndex(), this.getSize(), this.getCount());
    }
}
```

### 5.3 基础类自动创建机制

代码生成器应当具备自动检测和创建基础类的能力：

1. **自动检测**：检查 `com.zja.model.base` 包下是否存在 `BasePageRequest` 和 `PageData` 类
2. **自动创建**：如果基础类不存在，则自动创建它们
3. **智能导入**：在需要的地方正确导入基础类

## 6. Service层规范

### 6.1 接口方法规范
```java
/**
 * {实体中文名} 服务层
 *
 * @author: {{author}}
 * @since: {当前日期} {当前时间}
 */
public interface {Entity}Service {

    /**
     * 查询{实体}
     *
     * @param id {实体}id
     */
    {Entity}DTO queryById(String id);

    /**
     * 查询{实体}列表
     */
    List<{Entity}DTO> list();

    /**
     * 分页查询{实体}
     */
    PageData<{Entity}DTO> pageList({Entity}PageRequest request);

    /**
     * 校验{实体}名称是否可用
     *
     * @param name {实体}名称
     * @return Boolean
     */
    Boolean existName(String name);

    /**
     * 新增{实体}
     */
    {Entity}DTO add({Entity}Request request);

    /**
     * 批量添加{实体}
     */
    List<{Entity}DTO> addBatch(List<{Entity}Request> requests);

    /**
     * 更新{实体}
     *
     * @param id {实体}id
     */
    {Entity}DTO update(String id, {Entity}UpdateRequest request);

    /**
     * 删除{实体}
     *
     * @param id {实体}id
     */
    boolean deleteById(String id);

    /**
     * 批量删除{实体}
     *
     * @param ids {实体}ids
     */
    void deleteBatch(List<String> ids);
}
```

## 7. ServiceImpl层规范

ServiceImpl层是服务层的具体实现，负责业务逻辑处理和数据访问。

### 7.1 基础注解
```java
@Slf4j
@Service
@Transactional
```

### 7.2 类注释模板
```java
/**
 * {实体中文名} 业务处理服务实现层
 *
 * @author: {{author}}
 * @since: {当前日期} {当前时间}
 */
```

### 7.3 依赖注入
```java
@Autowired
{Entity}Dao dao;

@Autowired
{Entity}Mapper mapper;
```

### 7.4 私有辅助方法
```java
private {Entity} get{Entity}ById(String id) {
    return dao.findById(id).orElseThrow(() -> new RuntimeException(String.format("传入的 id=%s 有误！", id)));
}
```

### 7.5 CRUD方法实现规范

#### 7.5.1 查询单个实体
```java
@Override
public {Entity}DTO queryById(String id) {
    {Entity} entity = this.get{Entity}ById(id);
    return mapper.toDto(entity);
}
```

#### 7.5.2 查询列表
```java
@Override
public List<{Entity}DTO> list() {
    List<{Entity}> list = dao.findAll();
    return mapper.toDtoList(list);
}
```

#### 7.5.3 分页查询
```java
@Override
public PageData<{Entity}DTO> pageList({Entity}PageRequest request) {
    int page = request.getPage();
    int size = request.getSize();
    Sort sort = Sort.by(Sort.Direction.DESC, "createTime");

    // 查询条件
    Specification<{Entity}> spec = buildQuery(request);
    // 分页查询
    Page<{Entity}> sourcePage = dao.findAll(spec, PageRequest.of(page, size, sort));

    return PageData.of(sourcePage);
}
```

#### 7.5.4 构建查询条件
```java
private Specification<{Entity}> buildQuery({Entity}PageRequest request) {
    // 构建查询条件
    return (root, query, cb) -> {
        List<Predicate> predicates = new ArrayList<>();
        // 关键词
        if (!StringUtils.isEmpty(request.getName())) {
            predicates.add(cb.like(root.get("name"), request.getName() + "%"));
        }
        if (request.get{FieldName}() != null) {
            predicates.add(cb.equal(root.get("{fieldName}"), request.get{FieldName}()));
        }
        // 将条件连接在一起
        return query.where(predicates.toArray(new Predicate[0])).getRestriction();
    };
}
```

#### 7.5.5 名称存在性检查
```java
@Override
public Boolean existName(String name) {
    Optional<{Entity}> byName = dao.findByName(name);
    return byName.isPresent();
}
```

#### 7.5.6 新增实体
```java
@Override
@Transactional
public {Entity}DTO add({Entity}Request request) {
    // 检验名字已存在
    if (existName(request.getName())) {
        throw new UnsupportedOperationException("名称不能重复");
    }

    {Entity} entity = mapper.toEntity(request);
    entity = dao.save(entity);

    return mapper.toDto(entity);
}
```

#### 7.5.7 批量新增
```java
@Override
@Transactional
public List<{Entity}DTO> addBatch(List<{Entity}Request> requests) {
    return requests.stream().map(this::add).collect(Collectors.toList());
}
```

#### 7.5.8 更新实体
```java
@Override
@Transactional
public {Entity}DTO update(String id, {Entity}UpdateRequest request) {
    // 校验存在
    {Entity} entity = this.get{Entity}ById(id);

    // 更新
    BeanUtils.copyProperties(request, entity, "id", "createTime");
    entity = dao.save(entity);

    return mapper.toDto(entity);
}
```

#### 7.5.9 删除单个实体
```java
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
```

#### 7.5.10 批量删除
```java
@Override
@Transactional
public void deleteBatch(List<String> ids) {
    ids.forEach(this::deleteById);
}
```

## 8. Controller层规范

### 8.1 基础注解
```java
@CrossOrigin
@Validated
@RestController
@RequestMapping("/rest/{entity}")
@Api(tags = {"{实体}管理页面"})
```

### 8.2 类注释模板
```java
/**
 * {实体中文名} 接口层（一般与页面、功能对应）
 *
 * @author: {{author}}
 * @since: {当前日期} {当前时间}
 */
```

### 8.3 依赖注入
```java
@Autowired
{Entity}Service service;
```

### 8.4 CRUD接口规范

#### 8.4.1 查询单个实体
```java
@GetMapping("/query/{id}")
@ApiOperation("{操作描述}")
public {Entity}DTO queryById(@NotBlank @PathVariable("id") String id) {
    return service.queryById(id);
}
```

#### 8.4.2 查询列表
```java
@GetMapping("/list")
@ApiOperation("查询{实体}列表")
public List<{Entity}DTO> list() {
    return service.list();
}
```

#### 8.4.3 分页查询
```java
@GetMapping("/page/list")
@ApiOperation("分页查询{实体}列表")
public PageData<{Entity}DTO> pageList(@Valid {Entity}PageRequest pageRequest) {
        return service.pageList(pageRequest);
}
```

#### 8.4.4 新增单个实体
```java
@PostMapping("/add")
@ApiOperation("添加{实体}")
public {Entity}DTO add(@Valid @RequestBody {Entity}Request request) {
        return service.add(request);
}
```

#### 8.4.5 批量新增
```java
@PostMapping("/add/batch")
@ApiOperation("批量添加{实体}")
public List<{Entity}DTO> add(@Valid @RequestBody List<{Entity}Request> requests) {
        return service.addBatch(requests);
}
```

#### 8.4.6 更新实体
```java
@PutMapping("/update/{id}")
@ApiOperation("更新{实体}")
public {Entity}DTO update(@NotBlank @PathVariable("id") String id,
                          @Valid @RequestBody {Entity}UpdateRequest updateRequest) {
        return service.update(id, updateRequest);
}
```

#### 8.4.7 删除单个实体
```java
@DeleteMapping("/delete/{id}")
@ApiOperation("删除{实体}")
public boolean deleteById(@NotBlank @PathVariable("id") String id) {
    return service.deleteById(id);
}
```

#### 8.4.8 批量删除
```java
@DeleteMapping("/delete/batch")
@ApiOperation("批量删除{实体}")
public void deleteBatch(@RequestBody List<String> ids) {
    service.deleteBatch(ids);
}
```

## 9. 代码生成变量说明

### 9.1 占位符变量
- `{Entity}`: 实体类名（如User）
- `{entity}`: 实体类名小写（如user）
- `{实体}`: 实体中文名（如用户）
- `{实体中文名}`: 实体完整中文描述
- `{字段中文名}`: 字段中文描述
- `{FieldType}`: 字段类型
- `{fieldName}`: 字段名
- `{操作描述}`: 具体操作描述
- `{当前日期}`: 当前日期（格式：yyyy/MM/dd）
- `{当前时间}`: 当前时间（格式：HH:mm）

### 9.2 常用实体字段模板
```
@ApiModelProperty("id")
private String id;

@ApiModelProperty("名称")
private String name;

@ApiModelProperty("创建时间")
private LocalDateTime createTime;

@ApiModelProperty("最后一次修改时间")
private LocalDateTime lastModifiedDate;
```

## 10. 使用示例

### 10.1 生成UserController的变量替换
```
{Entity} -> User
{entity} -> user
{实体} -> 用户
{实体中文名} -> 用户
{操作描述} -> 查询单个用户详情
```

### 10.2 生成完整Controller的步骤
1. 替换所有占位符变量
2. 根据实际业务添加/删除字段
3. 调整API路径和描述
4. 补充业务逻辑注释
5. 验证代码规范和格式

## 11. 注意事项

### 11.1 代码规范
- 使用Lombok注解简化代码
- 统一使用Swagger注解
- 保持注释格式一致
- 遵循SpringBoot最佳实践

### 11.2 安全规范
- 使用@Valid进行参数校验
- 使用@NotBlank等注解校验必填字段
- 统一异常处理机制
- 接口权限控制

### 11.3 性能规范
- 合理使用分页查询
- 避免N+1查询问题
- 及时释放资源
- 优化数据库查询

## 12. 最佳实践

### 12.1 事务管理
- 在服务层方法上使用 `@Transactional` 注解确保数据一致性
- 对于只读操作，可以使用 `@Transactional(readOnly = true)` 提高性能
- 合理设置事务传播行为和隔离级别

### 12.2 异常处理
- 统一异常处理机制，通过 `@ControllerAdvice` 和 `@ExceptionHandler` 处理全局异常
- 自定义业务异常类，提供有意义的错误信息
- 记录必要的错误日志便于排查问题

### 12.3 参数校验
- 使用 `@Valid` 和 `@Validated` 进行参数校验
- 定义校验组，针对不同场景应用不同的校验规则
- 自定义校验注解满足特定业务需求

### 12.4 缓存策略
- 在适当的方法上使用 `@Cacheable`、`@CacheEvict` 等注解
- 合理设计缓存键，避免缓存冲突
- 设置合适的缓存过期时间和容量限制