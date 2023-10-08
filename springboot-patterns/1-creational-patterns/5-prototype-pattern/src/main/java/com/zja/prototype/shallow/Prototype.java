/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 11:23
 * @Since:
 */
package com.zja.prototype.shallow;

/**
 * @author: zhengja
 * @since: 2023/10/08 11:23
 */
// 原型接口
interface Prototype extends Cloneable {
    Prototype clone();
}