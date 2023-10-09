/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 16:31
 * @Since:
 */
package com.zja.visitor.book;

/**
 * 运费
 *
 * @author: zhengja
 * @since: 2023/10/09 16:31
 */
public class ShippingVisitor implements Visitor {
    @Override
    public void visit(Book book) {
        // 计算书籍的运费逻辑
        double shippingCost = book.getWeight() * 0.5;
        System.out.println("Shipping cost for book " + book.getName() + ": $" + shippingCost);
    }

    @Override
    public void visit(Electronics electronics) {
        // 计算电子产品的运费逻辑
        double shippingCost = electronics.getWeight() * 1.2;
        System.out.println("Shipping cost for electronics " + electronics.getName() + ": $" + shippingCost);
    }
}