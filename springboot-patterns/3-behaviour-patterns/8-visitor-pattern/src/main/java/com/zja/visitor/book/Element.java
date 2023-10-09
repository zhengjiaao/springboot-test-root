/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 16:32
 * @Since:
 */
package com.zja.visitor.book;

/**
 * @author: zhengja
 * @since: 2023/10/09 16:32
 */
public interface Element {
    void accept(Visitor visitor);
}