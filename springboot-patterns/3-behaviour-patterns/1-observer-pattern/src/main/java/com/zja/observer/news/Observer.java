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
// 观察者接口
interface Observer {
    void update(String category, String news);
}
