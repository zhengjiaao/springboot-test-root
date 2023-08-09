/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-06-14 16:06
 * @Since:
 */
package com.zja.task;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象目标
 * @author: zhengja
 * @since: 2023/06/14 16:06
 */
public abstract  class Subject {
    protected List<Observer> observers = new ArrayList<Observer>();

    // 增加观察者方法
    public void add(Observer observer) {
        observers.add(observer);
    }

    // 删除观察者方法
    public void remove(Observer observer) {
        observers.remove(observer);
    }

    // 通知观察者方法
    public void notifyObserver(Long taskId) {
        for (Observer observer : observers) {
            observer.response(taskId);
        }
    }
}
