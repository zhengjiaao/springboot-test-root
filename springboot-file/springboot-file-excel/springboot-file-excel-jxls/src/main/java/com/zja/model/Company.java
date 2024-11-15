package com.zja.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 公司
 *
 * @Author: zhengja
 * @Date: 2024-11-15 16:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    private String companyName; // 员工所属公司
    private String departmentName; // 员工所属部门
    private List<Employee> employeeList; // 员工列表
}
