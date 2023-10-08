/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 17:09
 * @Since:
 */
package com.zja.composite.department;

/**组合节点-员工
 * @author: zhengja
 * @since: 2023/10/08 17:09
 */
public class Employee implements Department {
    private String name;

    public Employee(String name) {
        this.name = name;
    }

    @Override
    public void display() {
        System.out.println("Employee: " + name);
    }
}