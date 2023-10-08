/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 16:56
 * @Since:
 */
package com.zja.composite.component;

/**
 * 组件接口 Component
 *
 * @author: zhengja
 * @since: 2023/10/08 16:56
 */
public interface Component {

    void add(Component component);

    void remove(Component component);

    void display();
}
