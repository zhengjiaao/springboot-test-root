/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 16:53
 * @Since:
 */
package com.zja.visitor.animal;

/**
 * @author: zhengja
 * @since: 2023/10/09 16:53
 */
public interface AnimalElement {
    void accept(Visitor visitor);
}