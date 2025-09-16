package com.zja.easyexcel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zhengja
 * @Date: 2025-09-16 19:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentSummary {
    private String deptName;
    private Integer employeeCount;
}
