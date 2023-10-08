/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-06-14 16:06
 * @Since:
 */
package com.zja.task;

/**
 * 抽象观察者
 * @author: zhengja
 * @since: 2023/06/14 16:06
 */
public interface Observer {
    void response(Long taskId);
}
