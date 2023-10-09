/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 11:06
 * @Since:
 */
package com.zja.observer.news;

import java.util.*;

/**
 * @author: zhengja
 * @since: 2023/10/09 11:06
 */
// 具体主题类
class NewsPublisher implements Subject {
    private Map<String, List<Observer>> observersMap = new HashMap<>();

    public void publishNews(String category, String news) {
        System.out.println("Publishing news: " + news);
        notifyObservers(category, news);
    }

    @Override
    public void registerObserver(String category, Observer observer) {
        List<Observer> observers = observersMap.getOrDefault(category, new ArrayList<>());
        observers.add(observer);
        observersMap.put(category, observers);
    }

    @Override
    public void unregisterObserver(String category, Observer observer) {
        List<Observer> observers = observersMap.getOrDefault(category, new ArrayList<>());
        observers.remove(observer);
        observersMap.put(category, observers);
    }

    @Override
    public void notifyObservers(String category, String news) {
        List<Observer> observers = observersMap.getOrDefault(category, new ArrayList<>());
        for (Observer observer : observers) {
            observer.update(category, news);
        }
    }
}

