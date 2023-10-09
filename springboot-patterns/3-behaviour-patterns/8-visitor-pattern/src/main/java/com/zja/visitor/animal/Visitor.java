/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 16:54
 * @Since:
 */
package com.zja.visitor.animal;

/**
 * @author: zhengja
 * @since: 2023/10/09 16:54
 */
public interface Visitor {
    void visit(Dog dog);
    void visit(Cat cat);
    void visit(Bird bird);
    // 添加其他动物类型的访问方法
}