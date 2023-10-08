/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 17:10
 * @Since:
 */
package com.zja.composite.department;

/**
 * 我们有两个部门和两个员工。DepartmentGroup表示部门，Employee表示员工。
 * 通过使用组合模式，我们可以将员工和部门组合成一个树形结构，并对整个组织结构进行统一的操作和遍历。
 *
 * @author: zhengja
 * @since: 2023/10/08 17:10
 */
public class Client {
    public static void main(String[] args) {
        // 创建员工
        Department employee1 = new Employee("John");
        Department employee2 = new Employee("Jane");

        // 创建部门
        DepartmentGroup department1 = new DepartmentGroup("Department 1");
        DepartmentGroup department2 = new DepartmentGroup("Department 2");

        // 组合员工和部门
        department1.add(employee1);
        department2.add(employee2);
        department2.add(department1);

        // 显示组织结构
        department2.display();

        //输出结果：
        //Department Group: Department 2
        //Employee: Jane
        //Department Group: Department 1
        //Employee: John
    }
}
