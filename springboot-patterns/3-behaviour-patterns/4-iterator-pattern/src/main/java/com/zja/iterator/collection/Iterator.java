/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 13:41
 * @Since:
 */
package com.zja.iterator.collection;

/**
 * @author: zhengja
 * @since: 2023/10/09 13:41
 */
// 迭代器接口
interface Iterator {
    boolean hasNext();
    Object next();
}