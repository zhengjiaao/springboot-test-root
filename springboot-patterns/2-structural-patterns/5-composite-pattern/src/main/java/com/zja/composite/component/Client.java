/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 16:58
 * @Since:
 */
package com.zja.composite.component;

/**
 * 使用组合模式构建组织架构的层次结构
 *
 * @author: zhengja
 * @since: 2023/10/08 16:58
 */
public class Client {
    public static void main(String[] args) {
        // 创建员工
        Component employee1 = new Employee("John Doe", "Manager");
        Component employee2 = new Employee("Jane Smith", "Engineer");
        Component employee3 = new Employee("Bob Johnson", "Designer");

        // 创建部门
        Component department1 = new Department("Engineering");
        Component department2 = new Department("Design");

        // 创建公司
        Component company = new Company("ABC Company");

        // 组合部门和员工
        department1.add(employee1);
        department1.add(employee2);
        department2.add(employee3);
        company.add(department1);
        company.add(department2);

        // 显示组织架构
        company.display();

        //输出结果：
        //Company: ABC Company
        //Department: Engineering
        //Employee: John Doe, Position: Manager
        //Employee: Jane Smith, Position: Engineer
        //Department: Design
        //Employee: Bob Johnson, Position: Designer
    }
}
