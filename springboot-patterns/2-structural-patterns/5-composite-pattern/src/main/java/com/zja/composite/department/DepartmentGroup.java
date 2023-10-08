/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 17:09
 * @Since:
 */
package com.zja.composite.department;

import java.util.ArrayList;
import java.util.List;

/**
 * 组合节点-部门组
 *
 * @author: zhengja
 * @since: 2023/10/08 17:09
 */
public class DepartmentGroup implements Department {
    private String name;
    private List<Department> departments;

    public DepartmentGroup(String name) {
        this.name = name;
        departments = new ArrayList<>();
    }

    public void add(Department department) {
        departments.add(department);
    }

    public void remove(Department department) {
        departments.remove(department);
    }

    @Override
    public void display() {
        System.out.println("Department Group: " + name);
        for (Department department : departments) {
            department.display();
        }
    }
}