package com.zja.table;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author: zhengja
 * @Date: 2025-10-21 13:53
 */
@Service
public class TableDataService {

    @Autowired
    private TableDataRepository repository;

    public List<TableDataDto> getAllData() {
        return repository.findAll().stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    public TableDataDto saveData(TableDataDto dto) {
        Optional<TableDataEntity> existingOpt = repository.findById(dto.getId());
        if (existingOpt.isPresent()) {
            TableDataEntity existing = existingOpt.get();
            // 检查版本号
            if (!existing.getVersion().equals(dto.getVersion())) {
                throw new OptimisticLockException("数据已被其他用户修改，请刷新后重试。");
            }
            // 更新实体
            existing.setName(dto.getName());
            existing.setAge(dto.getAge());
            existing.setEmail(dto.getEmail());
            // JPA会自动处理版本号递增
            TableDataEntity saved = repository.save(existing);
            return entityToDto(saved);
        } else {
            // 如果ID不存在，可能是新增操作，但前端传了ID，这里按更新处理
            throw new RuntimeException("数据不存在，无法更新。");
        }
    }

    public TableDataDto createData(TableDataDto dto) {
        TableDataEntity entity = new TableDataEntity();
        entity.setName(dto.getName());
        entity.setAge(dto.getAge());
        entity.setEmail(dto.getEmail());
        // 新增时版本号由JPA自动设置为0
        TableDataEntity saved = repository.save(entity);
        return entityToDto(saved);
    }

    public void deleteData(Long id) {
        repository.deleteById(id);
    }

    private TableDataDto entityToDto(TableDataEntity entity) {
        return new TableDataDto(
                entity.getId(),
                entity.getName(),
                entity.getAge(),
                entity.getEmail(),
                entity.getVersion()
        );
    }
}