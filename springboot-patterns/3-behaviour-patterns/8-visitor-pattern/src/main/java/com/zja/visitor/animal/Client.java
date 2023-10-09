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
public class Client {
    public static void main(String[] args) {
        // 创建具体动物对象
        Dog dog = new Dog("Buddy");
        Cat cat = new Cat("Whiskers");
        Bird bird = new Bird("Tweety");

        // 创建具体访问者对象
        Visitor zooVisitor = new ZooVisitor();

        // 应用访问者操作
        dog.accept(zooVisitor);
        cat.accept(zooVisitor);
        bird.accept(zooVisitor);

        //输出结果：
        //Feeding the dog: Buddy
        //Cleaning the cat: Whiskers
        //Observing the bird: Tweety
    }
}
