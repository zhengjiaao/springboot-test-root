/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 16:54
 * @Since:
 */
package com.zja.visitor.animal;

/**
 * @author: zhengja
 * @since: 2023/10/09 16:54
 */
public class ZooVisitor implements Visitor {
    @Override
    public void visit(Dog dog) {
        System.out.println("Feeding the dog: " + dog.getName());
    }

    @Override
    public void visit(Cat cat) {
        System.out.println("Cleaning the cat: " + cat.getName());
    }

    @Override
    public void visit(Bird bird) {
        System.out.println("Observing the bird: " + bird.getName());
    }

    // 添加其他动物类型的访问方法的具体实现
}