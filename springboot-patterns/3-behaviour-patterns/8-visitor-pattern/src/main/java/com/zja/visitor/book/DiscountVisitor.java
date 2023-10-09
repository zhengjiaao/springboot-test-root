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
 * 折扣
 *
 * @author: zhengja
 * @since: 2023/10/09 16:31
 */
public class DiscountVisitor implements Visitor {
    @Override
    public void visit(Book book) {
        // 对书籍应用折扣逻辑
        double discountedPrice = book.getPrice() * 0.9;
        book.setPrice(discountedPrice);
        System.out.println("Applied discount to book: " + book.getName());
    }

    @Override
    public void visit(Electronics electronics) {
        // 对电子产品应用折扣逻辑
        double discountedPrice = electronics.getPrice() * 0.8;
        electronics.setPrice(discountedPrice);
        System.out.println("Applied discount to electronics: " + electronics.getName());
    }
}