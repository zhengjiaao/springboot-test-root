/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 16:57
 * @Since:
 */
package com.zja.composite.component;

/**
 * 叶子节点 员工（Employee）
 *
 * @author: zhengja
 * @since: 2023/10/08 16:57
 */
public class Employee implements Component {
    private String name;
    private String position;

    public Employee(String name, String position) {
        this.name = name;
        this.position = position;
    }

    @Override
    public void add(Component component) {

    }

    @Override
    public void remove(Component component) {

    }

    @Override
    public void display() {
        System.out.println("Employee: " + name + ", Position: " + position);
    }
}