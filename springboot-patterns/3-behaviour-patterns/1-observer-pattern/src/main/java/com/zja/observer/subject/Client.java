/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 10:57
 * @Since:
 */
package com.zja.observer.subject;

/**
 * @author: zhengja
 * @since: 2023/10/09 10:57
 */
public class Client {
    public static void main(String[] args) {
        ConcreteSubject subject = new ConcreteSubject();
        ObserverA observerA = new ObserverA();
        ObserverB observerB = new ObserverB();

        subject.registerObserver(observerA);
        subject.registerObserver(observerB);

        subject.setState(5);
        subject.setState(10);

        subject.unregisterObserver(observerA);

        subject.setState(15);

        //输出结果：
        //Observer A: Received update. New state: 5
        //Observer B: Received update. New state: 5
        //Observer A: Received update. New state: 10
        //Observer B: Received update. New state: 10
        //Observer B: Received update. New state: 15
    }
}
