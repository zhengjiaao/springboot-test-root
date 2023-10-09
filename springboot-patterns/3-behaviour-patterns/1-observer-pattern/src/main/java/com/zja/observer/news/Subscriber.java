/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 11:06
 * @Since:
 */
package com.zja.observer.news;

/**
 * @author: zhengja
 * @since: 2023/10/09 11:06
 */
// 具体观察者类
class Subscriber implements Observer {
    private String name;

    public Subscriber(String name) {
        this.name = name;
    }

    @Override
    public void update(String category, String news) {
        System.out.println(name + " received news in category " + category + ": " + news);
    }
}
