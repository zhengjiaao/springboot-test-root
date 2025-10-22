package com.zja.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zhengja
 * @Date: 2025-10-21 13:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableDataDto {
    private Long id;
    private String name;
    private Integer age;
    private String email;
    private Long version; // 包含版本号用于乐观锁
}
