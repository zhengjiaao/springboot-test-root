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
// 具体观察者类A
class ObserverA implements Observer {
    @Override
    public void update(Subject subject) {
        ConcreteSubject concreteSubject = (ConcreteSubject) subject;
        System.out.println("Observer A: Received update. New state: " + concreteSubject.getState());
    }

}

