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
// 主题接口
interface Subject {
    void registerObserver(String category, Observer observer);

    void unregisterObserver(String category, Observer observer);

    void notifyObservers(String category, String news);
}
