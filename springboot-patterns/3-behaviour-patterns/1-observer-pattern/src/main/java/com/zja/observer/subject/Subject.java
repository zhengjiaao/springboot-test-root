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
// 主题接口
interface Subject {
    void registerObserver(Observer observer);
    void unregisterObserver(Observer observer);
    void notifyObservers();
}