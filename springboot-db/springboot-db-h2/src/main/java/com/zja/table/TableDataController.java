package com.zja.table;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2025-10-21 13:55
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // 仅用于演示，生产环境请配置具体域名
public class TableDataController {

    @Autowired
    private TableDataService tableDataService;

    @GetMapping("/data")
    public ResponseEntity<List<TableDataDto>> getAllData() {
        List<TableDataDto> data = tableDataService.getAllData();
        return ResponseEntity.ok(data);
    }

    @PostMapping("/data")
    public ResponseEntity<?> saveData(@RequestBody TableDataDto dto) {
        try {
            if (dto.getId() == null || dto.getId() <= 0) {
                // 如果ID为null或<=0，认为是新增
                TableDataDto created = tableDataService.createData(dto);
                return ResponseEntity.ok(created);
            } else {
                // 否则是更新
                TableDataDto updated = tableDataService.saveData(dto);
                return ResponseEntity.ok(updated);
            }
        } catch (OptimisticLockException e) {
            return ResponseEntity.status(409).body(new ErrorResponse(e.getMessage())); // 409 Conflict
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("服务器内部错误"));
        }
    }

    @PutMapping("/data")
    public ResponseEntity<?> updateData(@RequestBody TableDataDto dto) {
        try {
            if (dto.getId() == null || dto.getId() <= 0) {
                // 如果ID为null或<=0，认为是新增
                TableDataDto created = tableDataService.createData(dto);
                return ResponseEntity.ok(created);
            } else {
                // 否则是更新
                TableDataDto updated = tableDataService.saveData(dto);
                return ResponseEntity.ok(updated);
            }
        } catch (OptimisticLockException e) {
            return ResponseEntity.status(409).body(new ErrorResponse(e.getMessage())); // 409 Conflict
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("服务器内部错误"));
        }
    }

    @DeleteMapping("/data/{id}")
    public ResponseEntity<?> deleteData(@PathVariable Long id) {
        try {
            tableDataService.deleteData(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("删除失败"));
        }
    }

    // 内部辅助类
    static class ErrorResponse {
        public final String error;

        public ErrorResponse(String error) {
            this.error = error;
        }
    }
}